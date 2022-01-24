FROM openjdk:17-slim

ARG JAR_FILE=app/build/libs/\*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]

LABEL org.opencontainers.image.source=https://github.com/fabiankuhn/openshift-skills
