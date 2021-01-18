#!/bin/sh

sqlite3 test_db << 'END_SQL'
.timeout 2000

DROP TABLE test;

CREATE TABLE test (
  transid INTEGER NOT NULL, 
  id INTEGER NOT NULL, 
  x REAL NOT NULL,
  y REAL NOT NULL,
  z REAL NOT NULL
);

select 'Table Test created';

BEGIN TRANSACTION;
INSERT INTO test
    WITH RECURSIVE
      cnt( transid, id, x, y, z) AS (
      VALUES(1, 1 , random(), random(), random()) UNION ALL 
      SELECT 1, id+1,random(),random(), random() FROM cnt WHERE ID<1000)
    select * from cnt;

COMMIT;

select 'Transaction 1 finished';

BEGIN TRANSACTION;
INSERT INTO test
    WITH RECURSIVE
      cnt( transid, id, x, y, z) AS (
      VALUES(2, 1 , random(), random(), random()) UNION ALL 
      SELECT 2, id+1,random(),random(), random() FROM cnt WHERE ID<1000000)
    select * from cnt;

COMMIT;

select 'Transaction 2 finished';

END_SQL


