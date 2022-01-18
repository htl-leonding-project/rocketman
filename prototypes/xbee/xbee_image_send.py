import serial
from timeit import default_timer as timer
from PIL import Image
import PIL
import base64

PORT = '/dev/ttyUSB0'
BAUD = 460800
ser = serial.Serial(PORT, BAUD)

# compression
start = timer()
picture = Image.open('img.png')
picture.save('img_comp.png',optimize=True,quality=100)
with open("img_comp.png", "rb") as imageFile:
    str = base64.b64encode(imageFile.read())
end = timer()

print('Compression took: ', end='')
print(end - start)

print('Sending image string...')
# Send data as bytes
while True:
    start = timer()
    ser.write(str)
    ser.write('\n'.encode())
    end = timer()
    print('Finished sending, it took ', end='')
    print(end - start)
    response = ser.readline()
    if response.decode() == 'OK\n':
        print('SUCCESS')
        break
    print('ERROR, trying again...')
ser.close()
