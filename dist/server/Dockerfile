FROM openjdk:7
COPY ./build /usr/src/elkkweb-server
WORKDIR /usr/src/elkkweb-server

# ENV ELKKWEB_RESOURCES_DIR
# ENV ELKKWEB_INFOLETTER_DIR
# ENV ELKKWEB_SERVER_PORT
# ENV ELKKWEB_DATOMIC_URI

CMD ["java", "-jar", "server-0.1.0-SNAPSHOT-standalone.jar"]
