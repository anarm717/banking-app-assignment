FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/demo-0.0.1-SNAPSHOT.jar /var/app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/var/app.jar"]
