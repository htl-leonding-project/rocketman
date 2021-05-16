#!/usr/bin/python
# For further information see the readme file
import json
import spidev
import time
import paho.mqtt.client as mqtt
import RPi.GPIO as GPIO

VIEWER_MODE_PIN = 16
USER_MODE_PIN = 18
JOYSTICK_DATA_TOPIC_NAME = 'joystick-values'
USERMODE_MQTT_TOPIC_NAME = 'user_mode'
MQTT_CLIENT_NAME = 'actuator-controller'

# Define sensor channels
# (channels 3 to 7 unused)
JOYSTICK_SWITCH_CHANNEL = 0
JOYSTICK_X_CHANNEL = 1
JOYSTICK_Y_CHANNEL = 2

# Define delay between readings (s)
delay = 0.5

current_usermode = ''

GPIO.setwarnings(False)  # Ignore warning for now
GPIO.setmode(GPIO.BOARD)  # Use physical pin numbering
GPIO.setup(VIEWER_MODE_PIN, GPIO.IN,
           pull_up_down=GPIO.PUD_DOWN)  # Set pin 10 to be an input pin and set initial value to be pulled low (off)
GPIO.setup(USER_MODE_PIN, GPIO.IN,
           pull_up_down=GPIO.PUD_DOWN)  # Set pin 12 to be an input pin and set initial value to be pulled low (off)

# Open SPI bus
spi = spidev.SpiDev()
spi.open(0, 0)
spi.max_speed_hz = 1000000

# create instance of mqtt client and connect to local broker
client = mqtt.Client(MQTT_CLIENT_NAME)
try:
    client.connect('localhost', port=1883, keepalive=600, bind_address='')
except ConnectionRefusedError:
    exit('MQTT-Broker not started, check readme on info how to start it')


# Function to read SPI data from MCP3008 chip
# Channel must be an integer 0-7
def read_channel(channel):
    adc = spi.xfer2([1, (8 + channel) << 4, 0])
    data = ((adc[1] & 3) << 8) + adc[2]
    return data


def toggle_to_viewer_mode(channel):
    global current_usermode
    current_usermode = 'VIEWER_MODE'


def toggle_to_user_mode(channel):
    global current_usermode
    current_usermode = 'USER_MODE'


def toggle_to_admin_mode(channel):
    global current_usermode
    current_usermode = 'ADMIN_MODE'


if GPIO.input(USER_MODE_PIN) == GPIO.LOW and GPIO.input(VIEWER_MODE_PIN) == GPIO.LOW:
    toggle_to_admin_mode(0)
elif GPIO.input(USER_MODE_PIN) == GPIO.HIGH:
    toggle_to_user_mode(0)
elif GPIO.input(VIEWER_MODE_PIN) == GPIO.HIGH:
    toggle_to_viewer_mode(0)

GPIO.add_event_detect(VIEWER_MODE_PIN, GPIO.RISING, callback=toggle_to_viewer_mode)
GPIO.add_event_detect(USER_MODE_PIN, GPIO.RISING, callback=toggle_to_user_mode)

while True:
    if current_usermode != 'ADMIN_MODE' and (GPIO.input(USER_MODE_PIN) == GPIO.LOW and
                                             GPIO.input(VIEWER_MODE_PIN) == GPIO.LOW):
        toggle_to_admin_mode(0)

    # Read the joystick position data
    vrx_pos = read_channel(JOYSTICK_X_CHANNEL)
    vry_pos = read_channel(JOYSTICK_Y_CHANNEL)
    # Read switch state
    swt_val = read_channel(JOYSTICK_SWITCH_CHANNEL)

    # Print out results
    print("--------------------------------------------")
    print("X : {}  Y : {}  Switch : {}".format(vrx_pos, vry_pos, swt_val))
    print("Usermode: " + current_usermode)

    data_set = {"x-axis": vrx_pos, "y-axis": vry_pos, "switch_value": swt_val}
    json_dump = json.dumps(data_set)

    client.publish(JOYSTICK_DATA_TOPIC_NAME, json_dump)
    client.publish(USERMODE_MQTT_TOPIC_NAME, current_usermode)

    # Wait before repeating loop
    time.sleep(delay)
