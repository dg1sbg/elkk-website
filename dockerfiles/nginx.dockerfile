# ============================================================================
#  DOCKERFILE for NGINX (for ELKKWEB)
# ============================================================================

# ----------------------------------------------------------------------------
#  BASIC IMAGE REFERENCE
# ----------------------------------------------------------------------------

FROM nginx

# ----------------------------------------------------------------------------
#  IMAGE METADATA
# ----------------------------------------------------------------------------

LABEL title="NGINX Dockefile for ELKKWEB"
LABEL description="Dockerfile for setting up NGINX for the ELKKWEB website."
LABEL version="A.01.00"
LABEL maintainer="Frank Goenninger <frankn.goenninger@goenninger.net>"
LABEL disclaimer="Tbis packages is copyright Goenninger B&T UG, Germany. German law applies in all cases and under all circumstances. By using this package you agree to these terms."

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
ENV ELKKWEB_RESOURCE_DIR ${ELKKWEB_DATA_ROOT}/resources
ENV ELKKWEB_LIB_DIR ${ELKKWEB_HOME}/lib
ENV ELKKWEB_PORT 3000
ENV ELKKWEB_DATOMIC_URI
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

RUN mkdir -p ${ELKKWB_DATA_ROOT}
RUN mkdir -p ${ELKKWEB_INFOLETTER_DIR}
RUN mkdir -p ${ELKKWEB_RESOURCE_DIR}
RUN mkdir -p ${ELKKWB_HOME}

# --- USER & GROUP SETUP ---

RUN addgroup --gid 50003 ${ELKKWEB_GROUP}
RUN adduser  --uid 50003 --gid 50003 --disabled-password --disabled-login ${ELKKWEB_USER}

RUN chown -R ${ELKKWEB_USER}:${ELKKWEB_GROUP}  ${ELKKWEB_DATA_ROOT}

# --- SETUP CONTENT ---

COPY resources ${ELKKWEB_HOME}/resources
COPY src ${ELKKWEB_HOME}/src
COPY build/${ELKKWEB_JARFILE} ${ELKKWEB_HOME}/lib/${ELKKWEB_JARFILE}

ONBUILD ADD ${ELKKWEB_HOME}/lib

RUN chown -R ${ELKKWEB_USER}:${ELKKWEB_GROUP}  ${ELKKWEB_HOME}

# --- EXPOSURE ---

VOLUME ${ELKKWEB_DATA_ROOT}

EXPOSE 3000

USER ${ELKKWEB_USER}

ENTRYPOINT ["java", "-jar"]

# --- RUN ---

WORKDIR ${ELKKWEB_HOME}

# ----------------------------------------------------------------------------
#  START RUNTIME
# ----------------------------------------------------------------------------

# --- ELKKWEB STARTUP ---

CMD ["${ELKKWEB_LIBDIR}/${ELKKWEB_JARFILE}"]
