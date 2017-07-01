FROM postgres

COPY sql/postgres-db.sql /docker-entrypoint-initdb.d/postgres-db.sql
COPY sql/postgres-table.sql /docker-entrypoint-initdb.d/postgres-table.sql
COPY sql/postgres-user.sql /docker-entrypoint-initdb.d/postres-user.sql

EXPOSE 5432

FROM pointslope/datomic-pro-starter:0.9.5561

COPY config/transactor.properties config/transactor.properties

CMD ["config/transactor.properties"]
