# ============================================================================
#  DOCKERFILE for DATOMIC
# ============================================================================

# ----------------------------------------------------------------------------
#  BASIC IMAGE REFERENCE
# ----------------------------------------------------------------------------

FROM debian:stable

# ----------------------------------------------------------------------------
#  IMAGE METADATA
# ----------------------------------------------------------------------------

LABEL title="GBT DATOMIC PRO"
LABEL description="A GBT supplied Dockerfile for DATOMIC PRO."
LABEL version="A.01.00"
LABEL maintainer="Frank Goenninger <frank.goenninger@goenninger.net>"
LABEL disclaimer="Tbis packages is copyright Goenninger B&T UG, Germany. German law applies in all cases and under all circumstances. By using this package you agree to these terms."

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
ENV POSTGRES_PASSWORD dt619pa7_ie4viydt__

# --- ENV VAR SETUP: DATOMIC  ---

# Nothing to declare

# --- DIRECTORY SETUP ---

# Directory Layout in Docker Container:
#
# ROOT DIRECTORY FOR DATA: /var/data
#
# /var/data/db/datomic/data -> Datomic DB data root directory

# --- DATOMIC SETUP ---

ENV DATOMIC_DATA_ROOT  /var/data/db/datomic
ENV DATOMIC_DATA_DB_DIR ${DATOMIC_DATA_ROOT}/data
ENV DATOMIC_GROUP datmoic
ENV DATOMIC_USER datomic

RUN mkdir -p ${DATOMIC_DATA_ROOT}
RUN mkdir -p ${DATOMIC_DATA_DB_DIR}

RUN addgroup --gid 50002 ${DATOMIC_GROUP}
RUN adduser  --uid 50002 --gid 50002 --disabled-password --disabled-login ${DATOMIC_USER}

RUN chown -R ${DATOMIC_USER}:${DATOMIC_GROUP}  ${DATOMIC_DATA_ROOT}

# VOLUME /var/data/db/datomic

ENV DATOMIC_VERSION 0.9.5561
ENV DATOMIC_HOME /opt/datomic-pro-$DATOMIC_VERSION
ENV DATOMIC_DATA $DATOMIC_DATA_DB_DIR

RUN apt-get update && apt-get install -y zip curl default-jre

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

# 3. Provide a CMD argument with the relative path to the
# transactor.properties file it will supplement the ENTRYPOINT

COPY db/datomic/config/transactor.properties config/transactor.properties

RUN chown -R ${DATOMIC_USER}:${DATOMIC_GROUP}  ${DATOMIC_HOME}

RUN echo DATOMIC HOME: $DATOMIC_HOME
RUN echo DATOMIC TRANSACTOR: `ls ${DATOMIC_HOME}/bin/transactor`

USER ${DATOMIC_USER}

ENTRYPOINT ["bin/transactor"]

# --- EXPOSURE ---

# VOLUME $DATOMIC_DATA

EXPOSE 4334 4335 4336 ${PGPORT}

# ----------------------------------------------------------------------------
#  START RUNTIME
# ----------------------------------------------------------------------------

# --- DATOMIC STARTUP ---

CMD  ["config/transactor.properties"]
