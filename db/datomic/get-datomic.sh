DATOMIC_VERSION=0.9.5561
CREDENTIALS_FILE=~/swdev/elkk-website/db/datomic/.credentials
curl -u $(cat $CREDENTIALS_FILE) -SL https://my.datomic.com/repo/com/datomic/datomic-pro/$DATOMIC_VERSION/datomic-pro-$DATOMIC_VERSION.zip -o datomic.zip
