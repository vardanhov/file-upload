FROM java:8

ADD . /src
WORKDIR /src

#RUN ./mvnw package -DskipTests

EXPOSE 8888
EXPOSE 5432

ENTRYPOINT ["java","-jar","/src/target/Upload-file-app-0.0.1-SNAPSHOT.jar"]