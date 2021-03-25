# actuator-control
## Sources
* Code for reading joystick input: https://www.raspberrypi-spy.co.uk/2014/04/using-a-joystick-on-the-raspberry-pi-using-an-mcp3008/
* Wiring for joystick and mcp3008: https://www.youtube.com/watch?v=eQaBFLbYMNY
* mqtt client in python: http://www.steves-internet-guide.com/into-mqtt-python-client/

## Description
This program reads the position of an analog joystick using a MCP3008 ADC and sends it to a mqtt-broker that is running on localhost.

## Running the application
### Raspberry Pi setup
Make sure to enable SPI and I2C.
```shell
sudo raspi-config
```
Navigate to **3 Interface Options**

Then enable both **P4 SPI** and **P5 I2C**

To apply the changes and load the kernel modules reboot the Raspberry Pi.
```shell
reboot
```

These changes can also be made via the GUI 
* navigate to the start menu -> Preferences -> Raspberry Pi Configuration -> Interfaces
* Then enable both **SPI** and **I2C**
* To apply the changes and load the kernel modules reboot the Raspberry Pi.

### Dependencies
```shell
pip3 install paho-mqtt
pip3 install spidev
```

### Start MQTT broker
First navigate to the mqtt-backend folder () then execute the following command:
```shell
docker-compose -f docker-compose.mqtt.yml up
```

### Launch the python program
In this folder execute the following command:
```shell
python3 main.py
```
Or just run it in PyCharm.