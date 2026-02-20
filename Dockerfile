FROM eclipse-temurin:17-jdk
EXPOSE 8080
ADD target/realtime-backend-app.war realtime-backend-app.war
ENTRYPOINT ["java","-war","/realtime-backend-app.war"]