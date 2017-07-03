
-- DROP ROLE :datomic

CREATE ROLE datomic LOGIN PASSWORD 'dt619pa7_ie4viydt__';
GRANT ALL ON TABLE datomic_kvs TO datomic;
COMMIT;
