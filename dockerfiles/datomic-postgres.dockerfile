# ============================================================================
#  DOCKERFILE for POSTGRES + DATOMIC
# ============================================================================

# ----------------------------------------------------------------------------
#  BASIC IMAGE REFERENCE
# ----------------------------------------------------------------------------

FROM debian:stable

# ----------------------------------------------------------------------------
#  IMAGE METADATA
# ----------------------------------------------------------------------------

LABEL title="ElKK WEB - http://www.eldoret-kids.de"
LABEL description="Web Site of Eldoret Kids Kenia e.V., Bempflingen, Germany"
LABEL version="A.01.00"
LABEL maintainer="Marian Goenninger <marian.goenninger@goenninger.net>"
LABEL disclaimer="This website is displaying the project description and general information of a not-for-profit charity organization that supports street children in Kenya. The website is hosted in Germany. German law applies in all cases and under all circumstances. By visiting this website you agree to these regulations."

# ----------------------------------------------------------------------------
#  SETUP
# ----------------------------------------------------------------------------
# Note: For directory layout info see section "--- DIRECTORY SETUP ----"
# below!

# --- GLOBAL ENV SETUP ---

# Nothing yet

# --- ENV VAR SETUP: POSTGRES ---

ENV PGHOSTADDR 127.0.0.1
ENV PGPORT 5432
ENV POSTGRES_PASSWORD __ie4viydt_dt619pa7

# --- ENV VAR SETUP: DATOMIC  ---

# Nothing to declare

# --- DIRECTORY SETUP ---

# Directory Layout in Docker Container:
#
# ROOT DIRECTORY FOR DATA: /var/data
#
# /var/data/db/datomic/data -> Datomic DB data root directory
# /var/data/db/postgres/data -> PostgreSQL DB data root directory

ENV POSTGRES_DATA_ROOT  /var/data/db/postgres
ENV POSTGRES_DATA_DB_DIR ${POSTGRES_DATA_ROOT}/data
ENV POSTGRES_GROUP postgres
ENV POSTGRES_USER postgres
ENV PGDATA ${POSTGRES_DATA_DB_DIR}

RUN mkdir -p ${POSTGRES_DATA_ROOT}
RUN mkdir -p ${POSTGRES_DATA_DB_DIR}

RUN addgroup --gid 50001 ${POSTGRES_GROUP}
RUN adduser  --uid 50001 --gid 50001 --disabled-password --disabled-login ${POSTGRES_USER}

RUN chown -R ${POSTGRES_USER}:${POSTGRES_GROUP}  ${POSTGRES_DATA_ROOT}

VOLUME /var/data/db/postgres

# --- POSTGRES SETUP ---

FROM postgres

COPY db/datomic/sql/postgres-db.sql /docker-entrypoint-initdb.d/postgres-db.sql
COPY db/datomic/sql/postgres-table.sql /docker-entrypoint-initdb.d/postgres-table.sql
COPY db/datomic/sql/postgres-user.sql /docker-entrypoint-initdb.d/postres-user.sql

EXPOSE 5432

# --- DATOMIC SETUP ---

FROM debian:stable

ENV DATOMIC_DATA_ROOT  /var/data/db/datomic
ENV DATOMIC_DATA_DB_DIR ${DATOMIC_DATA_ROOT}/data
ENV DATOMIC_GROUP datmoic
ENV DATOMIC_USER datomic

RUN mkdir -p ${DATOMIC_DATA_ROOT}
RUN mkdir -p ${DATOMIC_DATA_DB_DIR}

RUN addgroup --gid 50002 ${DATOMIC_GROUP}
RUN adduser  --uid 50002 --gid 50002 --disabled-password --disabled-login ${DATOMIC_USER}

RUN chown -R ${DATOMIC_USER}:${DATOMIC_GROUP}  ${DATOMIC_DATA_ROOT}

VOLUME /var/data/db/datomic

ENV DATOMIC_VERSION 0.9.5561.50
ENV DATOMIC_HOME /opt/datomic-pro-$DATOMIC_VERSION
ENV DATOMIC_DATA $DATOMIC_DATA_DB_DIR

RUN apt-get update && apt-get install -y apt-utils zip curl

# Datomic Pro Starter as easy as 1-2-3
# 1. Create a .credentials file containing user:pass
# for downloading from my.datomic.com
COPY db/datomic/.credentials /tmp/.credentials

# 2. Make sure to have a config/ folder in the same folder as your
# Dockerfile containing the transactor property file you wish to use
ONBUILD ADD config $DATOMIC_HOME/config

#COPY datomic/datomic-pro-$DATOMIC_VERSION.zip /tmp
#RUN unzip /tmp/datomic-pro-$DATOMIC_VERSION.zip -d /opt
#RUN rm /tmp/datomic-pro-$DATOMIC_VERSION.zip

RUN curl -u $(cat /tmp/.credentials) -SL https://my.datomic.com/repo/com/datomic/datomic-pro/$DATOMIC_VERSION/datomic-pro-$DATOMIC_VERSION.zip -o /tmp/datomic.zip \
  && unzip /tmp/datomic.zip -d /opt \
  && rm -f /tmp/datomic.zip

WORKDIR $DATOMIC_HOME
RUN echo DATOMIC HOME: $DATOMIC_HOME
RUN echo DATOMIC HOME CONTENTS: `ls $DATOMIC_HOME`
RUN echo DATOMIC TRANSACTOR BINARY: `ls $DATOMIC_HOME/bin/transactor`
# ENTRYPOINT ["$DATOMIC_HOME/bin/transactor"]

# 3. Provide a CMD argument with the relative path to the
# transactor.properties file it will supplement the ENTRYPOINT
VOLUME $DATOMIC_DATA

EXPOSE 4334 4335 4336

COPY db/datomic/config/transactor.properties config/transactor.properties

# ----------------------------------------------------------------------------
#  START RUNTIME
# ----------------------------------------------------------------------------

# --- DATOMIC STARTUP ---

CMD  ["config/transactor.properties"]
