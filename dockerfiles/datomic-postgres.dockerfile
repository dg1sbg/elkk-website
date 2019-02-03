# ============================================================================
#  DOCKERFILE for POSTGRES + DATOMIC
# ============================================================================

# ----------------------------------------------------------------------------
#  BASIC IMAGE REFERENCE
# ----------------------------------------------------------------------------

FROM postgres

# ----------------------------------------------------------------------------
#  IMAGE METADATA
# ----------------------------------------------------------------------------

LABEL title="POSTGRES for DATOMIC"
LABEL description="This sets up a PostgreSQL environment for use with the Datomic DB."
LABEL version="A.01.00"
LABEL maintainer="Frank Goenninger <frank..goenninger@goenninger.net>"
LABEL disclaimer="Tbis packages is copyright Goenninger B&T UG, Germany. German law applies in all cases and under all circumstances. By using this package you agree to these terms."

# ----------------------------------------------------------------------------
#  SETUP
# ----------------------------------------------------------------------------
# Note: For directory layout info see section "--- DIRECTORY SETUP ----"
# below!

# --- GLOBAL ENV SETUP ---

# Nothing yet

# --- ENV VAR SETUP: POSTGRES ---

# ENV PGHOSTADDR localhost
# ENV PGPORT 5432
ENV POSTGRES_PASSWORD dt619pa7_ie4viydt__

# --- ENV VAR SETUP: DATOMIC  ---

# Nothing to declare

# --- DIRECTORY SETUP ---

# Directory Layout in Docker Container:
#
# ROOT DIRECTORY FOR DATA: /var/data
#
# /var/data/db/postgres/data -> PostgreSQL DB data root directory

ENV POSTGRES_DATA_ROOT  /var/data/db/postgres
ENV POSTGRES_DATA_DB_DIR ${POSTGRES_DATA_ROOT}/data
ENV POSTGRES_GROUP postgres
ENV POSTGRES_USER postgres
ENV PGDATA ${POSTGRES_DATA_DB_DIR}

RUN mkdir -p ${POSTGRES_DATA_ROOT}
RUN mkdir -p ${POSTGRES_DATA_DB_DIR}

# RUN addgroup --gid 50001 ${POSTGRES_GROUP}
# RUN adduser  --uid 50001 --gid 50001 --disabled-password --disabled-login ${POSTGRES_USER}

RUN chown -R ${POSTGRES_USER}:${POSTGRES_GROUP}  ${POSTGRES_DATA_ROOT}

# --- POSTGRES SETUP ---

COPY db/datomic/sql/ /docker-entrypoint-initdb.d/

# --- EXPOSURE ---

EXPOSE 5432

# VOLUME ${POSTGRES_DATA_DB_DIR}

# ----------------------------------------------------------------------------
#  START RUNTIME
# ----------------------------------------------------------------------------

# --- POSTGRES STARTUP ---

# Nothing here additionally
