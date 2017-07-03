FROM postgres

COPY sql/postgres-user.sql /docker-entrypoint-initdb.d/postres-user.sql
COPY sql/postgres-db.sql /docker-entrypoint-initdb.d/postgres-db.sql
COPY sql/postgres-table.sql /docker-entrypoint-initdb.d/postgres-table.sql

EXPOSE 5432
