# mqtt and joystick readings
echo "Installing libraries needed for joystick readings and to send mqtt data"
pip3 install paho-mqtt
pip3 install spidev
pip3 install RPi.GPIO

# LoRa
echo "Installing libraries needed for LoRa transmission"
pip3 install pyLoRa

# xBee
echo "Installing libraries needed for xBee transmission"
pip3 install pyserial
pip3 install opencv-python 
