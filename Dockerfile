FROM java:8

ADD . /app
WORKDIR /app

RUN ./mvnw package -DskipTests

EXPOSE 8888
EXPOSE 5432

ENTRYPOINT ["java","-jar","/app/target/Upload-file-app-0.0.1-SNAPSHOT.jar"]