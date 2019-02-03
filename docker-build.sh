#! /bin/bash

clear
MYSELF=`basename $0`

echo "=============================================================================="
echo "$MYSELF: Building Docker images for ELKKWEB"
echo "=============================================================================="
echo

echo "*** PHASE 1: POSTGRES for DATOMIC"
echo
CMD="docker build -f dockerfiles/datomic-postgres.dockerfile -t gbt/datomic-postgres:dev_a0100 -t gbt/datomic-postgres:latest --force-rm=true --squash=true ."
echo $CMD
$CMD
RC=$?
if [ $RC -ne 0 ]
then
    echo "FAILED: $CMD ... - ABORTING !!!"
    exit $RC
fi
echo "SUCCESS."
echo

echo "*** PHASE 2: DATOMIC for ELKKWEB"
echo
CMD="docker build -f dockerfiles/datomic-pro.dockerfile -t gbt/elkkweb-datomic:dev_a0100 -t gbt/elkkweb-datomic:latest --force-rm=true --squash=true ."
echo $CMD
$CMD
RC=$?
if [ $RC -ne 0 ]
then
    echo "FAILED: $CMD ... - ABORTING !!!"
    exit $RC
fi

echo "*** PHASE 3: ELKKWEB"
echo
CMD="docker build -f dockerfiles/elkkweb.dockerfile -t gbt/elkkweb:dev_a0100 -t gbt/elkkweb:latest --force-rm=true --squash=true ."
echo $CMD
$CMD
RC=$?
if [ $RC -ne 0 ]
then
    echo "FAILED: $CMD ... - ABORTING !!!"
    exit $RC
fi

echo "Done - Exiting."
echo "READY."
echo
exit
