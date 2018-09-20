FROM openjdk:8
ENV JAVA_OPTS ""
WORKDIR /usr/src/app
COPY docker/run.sh ./run.sh
COPY target/presence-ms-*.jar ./app.jar
CMD ["./run.sh"]