#! /bin/bash

clear
docker build -f docker/datomic-postgres.dockerfile -t  gbt/elkk-datomic-postgres --force-rm=true --squash=true .

