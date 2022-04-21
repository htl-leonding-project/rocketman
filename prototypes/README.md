# Prototypes - Rocketman

[![Home](../images/home.ico)](https://github.com/htl-leonding-project/rocketman/blob/master/README.md)

- **mqtt-backend**
    - Quarkus application that receives the rocket data values via MQTT and saves them locally.
  
- **mqtt-test**
    - Quarkus application that simulates the rockets data values.
  

- **actuator-controller**
    - Reads the position of an analog joystick and sends it to a mqtt-broker that is running on localhost. 
    
- **xbee**
    - Contains the files for xBee video streaming.
    
    
# Running the project
  
To run the project on a Raspberry Pi you need to execute the docker-compose file with Dockerimges compiled for *aarch64* architecture.

**The OS on the Raspberry Pi needs to be 64-bit**

```shell
docker-compose -f docker-compose.aarch64.yml up [-d]
```

If you want to run it on a device with *amd64* architecture there is a docker compose file availible with Dockerimges compiled for *amd64* architecture.

```shell
docker-compose up [-d]
```
