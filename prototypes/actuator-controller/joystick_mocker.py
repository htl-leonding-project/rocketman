#!/usr/bin/python
import atexit
import paho.mqtt.client as mqtt
import json
import time
import random

JOYSTICK_DATA_TOPIC_NAME = 'joystick-values'
USER_MODE_MQTT_TOPIC_NAME = 'user_mode'
MQTT_CLIENT_NAME = 'actuator-controller-simulator'

joystick_data_file = open('joystick_mock_data.csv', 'r')
user_modes = ['VIEWER_MODE', 'USER_MODE', 'ADMIN_MODE']
all_lines = joystick_data_file.readlines()


def exit_handler():
    joystick_data_file.close()
    client.disconnect()


# register method to safely close files and other connections
atexit.register(exit_handler)

# create instance of mqtt client and connect to local broker
client = mqtt.Client(MQTT_CLIENT_NAME)
try:
    client.connect('localhost', port=1883, keepalive=600, bind_address='')
except ConnectionRefusedError:
    exit('MQTT-Broker not started, check readme on info how to start it')

while True:
    splitted = random.choice(all_lines).split(';')

    data_set = {'x-axis': splitted[0], 'y-axis': splitted[1], 'switch_value': splitted[2].replace('\n', '')}
    print(data_set)
    json_dump = json.dumps(data_set)

    client.publish(JOYSTICK_DATA_TOPIC_NAME, json_dump)
    client.publish(USER_MODE_MQTT_TOPIC_NAME, random.choice(user_modes))

    # Wait before repeating loop
    time.sleep(5)
