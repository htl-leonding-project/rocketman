DROP TABLE data_set;
CREATE TABLE data_set (
    ds_id INT NOT NULL
        CONSTRAINT DATASET_PK PRIMARY KEY
        GENERATED ALWAYS AS IDENTITY,
    ds_description varchar NOT NULL ,
    ds_value varchar NOT NULL ,
    ds_unit varchar NOT NULL ,
    ds_timestamp TIMESTAMP NOT NULL
);
