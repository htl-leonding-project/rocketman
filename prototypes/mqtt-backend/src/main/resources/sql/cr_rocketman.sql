CREATE TABLE data_set (
    ds_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    ds_description varchar NOT NULL,
    ds_value varchar NOT NULL,
    ds_unit varchar NOT NULL,
    ds_timestamp TIMESTAMP,
    ds_st_id number NOT NULL,
    FOREIGN KEY (ds_st_id) REFERENCES start(st_id)
);

CREATE TABLE start (
    st_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    st_comment varchar,
    st_startDate Date NOT NULL,
    st_endDate Date
);