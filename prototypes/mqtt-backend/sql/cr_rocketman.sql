DROP TABLE temperature;
CREATE TABLE temperature (
    timestamp TIMESTAMP NOT NULL ,
    temperature NUMERIC NOT NULL
);

DROP TABLE atmospheric_pressure;
CREATE TABLE atmospheric_pressure (
     timestamp TIMESTAMP NOT NULL ,
     atmospheric_pressure NUMERIC NOT NULL
);