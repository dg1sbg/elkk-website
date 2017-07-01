#! /bin/bash

clear
# docker build -f dockerfiles/datomic-postgres.dockerfile -t "gbt/elkkweb-datomic-postgres:dev_a0100" --no-cache=true --force-rm=true --squash=true .
docker build -f dockerfiles/datomic-postgres.dockerfile -t "gbt/elkkweb-datomic-postgres:dev_a0100" --force-rm=true --squash=true .
