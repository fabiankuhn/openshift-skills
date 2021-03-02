# TODO check version
FROM openjdk:11.0.4-jre-slim-buster
ARG JAR_FILE=app/build/libs/\*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8180

# docker build -t fabiankuhn/openshift-testapp-backend .
# docker run -p 8180:8180 -d --name openshift-testapp fabiankuhn/openshift-testapp-backend
