# actuator-control
## Nav
[![Home](../../images/home.ico)](https://github.com/htl-leonding-project/rocketman/blob/master/README.md)
## Sources
* Code for reading joystick input: https://www.raspberrypi-spy.co.uk/2014/04/using-a-joystick-on-the-raspberry-pi-using-an-mcp3008/
* Wiring for joystick and mcp3008: https://www.youtube.com/watch?v=eQaBFLbYMNY
* mqtt client in python: http://www.steves-internet-guide.com/into-mqtt-python-client/

## Description
This program reads the position of an analog joystick using a MCP3008 ADC and sends it to a mqtt-broker that is running on localhost. Additional functionality might be added in the future.

## Wiring

| MCP3008          | RASPBERRY PI  |
|------------------|---------------|
| pin 16 (Vdd)     | pin 1 (3.3v)  |
| pin 15 (Vref)    | pin 1 (3.3v)  |
| pin 14 (AGND)    | pin 6 (GND)   |
| pin 13 (CLK)     | pin 23 (SCLK) |
| pin 12 (Dout)    | pin 21 (MISO) |
| pin 11 (Din)     | pin 19 (MOSI) |
| pin 10 (cs/SHDN) | pin 24 (CEO)  |
| pin 9 (DGND)     | pin 6 (GND)   |

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

You can simply run the **setup_python_modules.sh** script in the prototypes' directory. This will install all required 
python modules.

```shell
./../setup_python_modules.sh
``` 

Or install manually:

```shell
pip3 install paho-mqtt
pip3 install spidev
```

### Start MQTT broker
First navigate to the mqtt-backend folder then execute the following command:
```shell
docker-compose -f docker-compose.mqtt.yml up
```

### Launch the python program

If you want to save the joystick data to a file call the program with the parameter "true":
```shell
python3 main.py true
```

If not then just execute the following command:
```shell
python3 main.py
```
Or just run it in PyCharm.