# Prototypes - Rocketman

[![Home](../images/home.ico)](https://github.com/htl-leonding-project/rocketman/blob/master/README.md)

- **mqtt-backend**
    - Quarkus application that receives the rocket data values via MQTT and saves them locally.
  
- **mqtt-test**
    - Quarkus application that simulates the rockets data values.
  

- **actuator-controller**
    - Reads the position of an analog joystick and sends it to a mqtt-broker that is running on localhost. 
  