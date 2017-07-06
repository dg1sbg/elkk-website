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
LABEL version="A.01.00"
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
  default-jre

# --- GLOBAL ENV SETUP ---

# Nothing yet

# --- ENV VAR SETUP: ELKKWEB  ---

ENV ELKKWEB_HOME /opt/elkkweb
ENV ELKKWEB_DATA_ROOT /var/data/www/elkkweb
ENV ELKKWEB_INFOLETTER_DIR ${ELKKWEB_DATA_ROOT}/newsletters
ENV ELKKWEB_RESOURCES_DIR ${ELKKWEB_DATA_ROOT}/resources
ENV ELKKWEB_LIB_DIR ${ELKKWEB_HOME}/lib
ENV ELKKWEB_SERVER_PORT 3003
ENV ELKKWEB_DATOMIC_URI datomic:sql://elkkwebdb?jdbc:postgresql://datomic-postgres.datomic-postgres.elkkweb.github10.node.intern:5432/datomic?user=datomic&password=dt619pa7_ie4viydt__
ENV ELKKWEB_USER elkkweb
ENV ELKKWEB_GROUP elkkweb
ENV ELKKWEB_JARFILE_VERSION 0.1.0
ENV ELKKWEB_JARFILE server-${ELKKWEB_JARFILE_VERSION}-SNAPSHOT-standalone.jar

# --- DIRECTORY SETUP ---

# Directory Layout in Docker Container:
#
# ROOT DIRECTORY FOR DATA: /var/data
#
# /var/data/db/datomic/data -> Datomic DB data root directory

RUN mkdir -p ${ELKKWEB_DATA_ROOT}
RUN mkdir -p ${ELKKWEB_INFOLETTER_DIR}
RUN mkdir -p ${ELKKWEB_RESOURCES_DIR}
RUN mkdir -p ${ELKKWEB_HOME}

# --- USER & GROUP SETUP ---

RUN addgroup --gid 50003 ${ELKKWEB_GROUP}
RUN adduser  --uid 50003 --gid 50003 --disabled-password --disabled-login ${ELKKWEB_USER}

RUN chown -R ${ELKKWEB_USER}:${ELKKWEB_GROUP}  ${ELKKWEB_DATA_ROOT}

# --- SETUP CONTENT ---

RUN mkdir ${ELKKWEB_HOME}/lib

COPY resources/elkk_res/public ${ELKKWEB_RESOURCES_DIR}
COPY src ${ELKKWEB_HOME}/src
COPY build/${ELKKWEB_JARFILE} ${ELKKWEB_HOME}/lib/${ELKKWEB_JARFILE}


RUN chown -R ${ELKKWEB_USER}:${ELKKWEB_GROUP}  ${ELKKWEB_HOME}

# --- EXPOSURE ---

VOLUME ${ELKKWEB_DATA_ROOT}
# VOLUME ${ELKKWEB_INFOLETTER_DIR}
# VOLUME ${ELKKWEB_RESOURCES_DIR}
# VOLUME ${ELKKWEB_HOME}

EXPOSE 3003

USER ${ELKKWEB_USER}

# --- RUN ---

WORKDIR ${ELKKWEB_HOME}
# ENTRYPOINT ["java", "-jar"]

# ----------------------------------------------------------------------------
#  START RUNTIME
# ----------------------------------------------------------------------------

# --- ELKKWEB STARTUP ---

CMD java -jar ${ELKKWEB_LIB_DIR}/${ELKKWEB_JARFILE}
