#!/bin/bash

psql -v ON_ERROR_STOP=1 <<-EOSQL
    CREATE DATABASE datomic
     WITH OWNER = postgres
          TEMPLATE template0
          ENCODING = 'UTF8'
          TABLESPACE = pg_default
          LC_COLLATE = 'en_US.UTF-8'
          LC_CTYPE = 'en_US.UTF-8'
          CONNECTION LIMIT = -1;
    CREATE ROLE datomic LOGIN PASSWORD 'dt619pa7_ie4viydt__';
EOSQL

psql -d datomic -v ON_ERROR_STOP=1 <<-EOSQLA
    CREATE TABLE datomic_kvs
    (
     id text NOT NULL,
     rev integer,
     map text,
     val bytea,
     CONSTRAINT pk_id PRIMARY KEY (id )
    )
    WITH (
     OIDS=FALSE
    );
    ALTER TABLE datomic_kvs
     OWNER TO postgres;
    GRANT ALL ON TABLE datomic_kvs TO postgres;
    GRANT ALL ON TABLE datomic_kvs TO public;
EOSQLA
