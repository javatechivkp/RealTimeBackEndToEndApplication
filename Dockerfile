FROM openjdk:17
EXPOSE 8080
ADD target/realtime-backend-app.war realtime-backend-app.war
ENTRYPOINT ["java","-war","/realtime-backend-app.war"]