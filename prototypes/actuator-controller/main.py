#!/usr/bin/python
# For further information see the readme file
import spidev
import time
import paho.mqtt.client as mqtt
import os

# Open SPI bus
spi = spidev.SpiDev()
spi.open(0, 0)
spi.max_speed_hz = 1000000

# create instance of mqtt client and connect to local broker
client = mqtt.Client('joystick_reader')
client.connect('localhost', port=1883, keepalive=600, bind_address="")


# Function to read SPI data from MCP3008 chip
# Channel must be an integer 0-7
def read_channel(channel):
    adc = spi.xfer2([1, (8 + channel) << 4, 0])
    data = ((adc[1] & 3) << 8) + adc[2]
    return data


# Define sensor channels
# (channels 3 to 7 unused)
swt_channel = 0
vrx_channel = 1
vry_channel = 2

# Define delay between readings (s)
delay = 0.5

while True:
    # Read the joystick position data
    vrx_pos = read_channel(vrx_channel)
    vry_pos = read_channel(vry_channel)

    # Read switch state
    swt_val = read_channel(swt_channel)

    # Print out results
    print("--------------------------------------------")
    print("X : {}  Y : {}  Switch : {}".format(vrx_pos, vry_pos, swt_val))

    client.publish('xaxis', vrx_pos)
    client.publish('yaxis', vry_pos)
    client.publish('swt_val', swt_val)

    # Wait before repeating loop
    time.sleep(delay)
