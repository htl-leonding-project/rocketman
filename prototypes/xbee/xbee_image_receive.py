import serial
import base64
from PIL import Image

PORT = '/dev/ttyUSB1'
BAUD = 460800

ser = serial.Serial(PORT, BAUD)
data = []


print("Waiting for data...\n")
while True:
    data = ser.readline()
    print('Reveived data!')
    data = data.replace('\n'.encode(), ''.encode())
    imgdata = base64.b64decode(data.decode("utf-8"))
    filename = 'received.png'  # I assume you have a way of picking unique filenames
    with open(filename, 'wb') as f:
        f.write(imgdata)
    print('Wrote image to ' + filename)
    try:
        Image.open(filename).verify()
        ser.write('OK\n'.encode())
        print('SUCCESS')
        print('Waiting for more data...')
    except:
        ser.write('ERROR\n'.encode())
        print('ERROR')



ser.close()