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

LABEL title="ELKKWEB"
LABEL description="Dockerfile for Eldoret Kids Kenia e.V. Website at https://www.eldoret-kids.de"
LABEL version="A.01.00"
LABEL maintainer="Frank Goenninger <frank.goenninger@goenninger.net>"

# ----------------------------------------------------------------------------
#  SETUP
# ----------------------------------------------------------------------------
# Note: For directory layout info see section "--- DIRECTORY SETUP ----"
# below!

# --- GLOBAL ENV SETUP ---

# Nothing yet

# --- ENV VAR SETUP: ELKKWEB  ---

ENV ELKKWEB_DATA_ROOT /var/data/www/elkkweb
ENV ELKKWEB_INFOLETTER_DIR ${ELKKWEB_DATA_ROOT}/newsletters
ENV ELKKWEB_RESOURCE_DIR=${ELKKWEB_DATA_ROOT}/resources
ENV ELKKWEB_PORT 3000
ENV ELKKWEB_DATOMIC_URI
ENV ELKKWEB_USER elkkweb
ENV ELKKWEB_GROUP elkkweb

# --- DIRECTORY SETUP ---

# Directory Layout in Docker Container:
#
# ROOT DIRECTORY FOR DATA: /var/data
#
# /var/data/db/datomic/data -> Datomic DB data root directory

RUN mkdir -p ${ELKKWB_DATA_ROOT}
RUN mkdir -p ${ELKKWEB_INFOLETTER_DIR}
RUN mkdir -p ${ELKKWEB_RESOURCE_DIR}

# --- USER & GROUP SETUP ---

RUN addgroup --gid 50003 ${ELKKWEB_GROUP}
RUN adduser  --uid 50003 --gid 50003 --disabled-password --disabled-login ${ELKKWEB_USER}

RUN chown -R ${ELKKWEB_USER}:${ELKKWEB_GROUP}  ${ELKKWEB_DATA_ROOT}

# --- SETUP CONTENT ---

# --- EXPOSURE ---

VOLUME ${ELKKWEB_DATA_ROOT}

EXPOSE 3000

# --- RUN ---

WORKDIR $ELKKWEB_HOME

# ----------------------------------------------------------------------------
#  START RUNTIME
# ----------------------------------------------------------------------------

# --- ELKKWEB STARTUP ---

# CMD  [""]
