# ============================================================================
#  DOCKERFILE for ELKKWEB
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
LABEL version="A.02.00"
LABEL maintainer="Marian Goenninger <marian.goenninger@goenninger.net>"
LABEL disclaimer="This website is displaying the project description and general information of a not-for-profit charity organization that supports street children in Kenya. The website is hosted in Germany. German law applies in all cases and under all circumstances. By visiting this website you agree to these regulations."

# ----------------------------------------------------------------------------
#  SETUP
# ----------------------------------------------------------------------------
# Note: For directory layout info see section "--- DIRECTORY SETUP ----"
# below!

# --- SYSTEM PACKAGE INSTALL ---

RUN apt-get update && apt-get install -y \
  net-tools \
  telnet \
  netcat \
  default-jre \
  && rm -rf /var/lib/apt/lists/*

# --- GLOBAL ENV SETUP ---

# Nothing yet

# --- ENV VAR SETUP: ELKKWEB  ---

ENV ELKKWEB_HOME /opt/elkkweb
ENV ELKKWEB_DATA_ROOT /var/data/www/elkkweb
ENV ELKKWEB_INFOLETTER_DIR ${ELKKWEB_DATA_ROOT}/newsletters
ENV ELKKWEB_RESOURCES_DIR ${ELKKWEB_DATA_ROOT}/resources
ENV ELKKWEB_LIB_DIR ${ELKKWEB_HOME}/lib
ENV ELKKWEB_SERVER_PORT 3003
ENV ELKKWEB_DATOMIC_URI datomic:ddb://eu-central-1/eldoret-kids-net-website-db/elkkweb
ENV ELKKWEB_USER elkkweb
ENV ELKKWEB_GROUP elkkweb
ENV ELKKWEB_JARFILE_VERSION 0.1.0
ENV ELKKWEB_JARFILE server-${ELKKWEB_JARFILE_VERSION}-SNAPSHOT-standalone.jar
ENV ELKKWEB_DOCKER_INSTALL_LOG /tmp/docker-install.log

# --- DIRECTORY SETUP ---

# Directory Layout in Docker Container:
#
# ROOT DIRECTORY FOR DATA: /var/data
#
# /var/data/db/datomic/data -> Datomic DB data root directory

RUN mkdir -p ${ELKKWEB_DATA_ROOT}

# --- DIRECTORY SETUP - CONT'D ---

RUN mkdir -p ${ELKKWEB_INFOLETTER_DIR}
RUN mkdir -p ${ELKKWEB_RESOURCES_DIR}
RUN mkdir -p ${ELKKWEB_HOME}

# --- USER & GROUP SETUP ---

RUN addgroup --gid 50003 ${ELKKWEB_GROUP}
RUN adduser  --uid 50003 --gid 50003 --disabled-password --disabled-login --defaults ${ELKKWEB_USER}
RUN chown -R ${ELKKWEB_USER}:${ELKKWEB_GROUP} ${ELKKWEB_DATA_ROOT}

# --- SETUP CONTENT ---

RUN mkdir -p ${ELKKWEB_HOME}/lib

COPY resources/elkk_res/public/* ${ELKKWEB_RESOURCES_DIR}/
COPY src ${ELKKWEB_HOME}/src
COPY build/${ELKKWEB_JARFILE} ${ELKKWEB_HOME}/lib/${ELKKWEB_JARFILE}

RUN chown -R ${ELKKWEB_USER}:${ELKKWEB_GROUP}  ${ELKKWEB_HOME}

# --- EXPOSURE ---

VOLUME ${ELKKWEB_DATA_ROOT}
EXPOSE 3003

# --- RUN ---

USER ${ELKKWEB_USER}
WORKDIR ${ELKKWEB_HOME}

# ----------------------------------------------------------------------------
#  START RUNTIME
# ----------------------------------------------------------------------------

# --- ELKKWEB STARTUP ---

CMD java -jar ${ELKKWEB_LIB_DIR}/${ELKKWEB_JARFILE}
