FROM eclipse-temurin:17-jdk
EXPOSE 8080
ADD target/realtime-backend-applatest.jar realtime-backend-applatest.jar
ENTRYPOINT ["java","-jar","/realtime-backend-applatest.jar"]