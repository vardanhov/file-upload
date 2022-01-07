FROM timbru31/java-node:8-jdk-fermium

ADD . /app
WORKDIR /app
VOLUME ******

RUN  ./mvnw package -DskipTests

EXPOSE 5432
EXPOSE 5005
EXPOSE 1024

ENTRYPOINT ["java", "-Xdebug", "-Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=n", "-jar", "-Dspring.profiles.active=dev","/app/target/Upload-file-app-0.0.1-SNAPSHOT.jar"]
