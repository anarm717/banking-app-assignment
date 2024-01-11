FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/bankingapp-0.0.1-SNAPSHOT.jar /var/app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/var/app.jar"]
