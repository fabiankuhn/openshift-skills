FROM openjdk:11-slim

EXPOSE 8080

ARG JAR_FILE=app/build/libs/\*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]


# cd backend
# docker build -t fabiankuhn/openshift-testapp-backend .
# docker run -p 8180:8180 -d --name openshift-testapp fabiankuhn/openshift-testapp-backend
