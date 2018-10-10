FROM maven:3-jdk-8 AS builder
WORKDIR /usr/src/app
COPY pom.xml ./pom.xml
COPY src ./src
RUN mvn clean install

FROM openjdk:8
ENV JAVA_OPTS ""
WORKDIR /usr/src/app
COPY docker/run.sh ./run.sh
COPY --from=builder /usr/src/app/target/presence-ms-*.jar ./app.jar
CMD ["./run.sh"]