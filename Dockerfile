FROM openjdk:17
EXPOSE 8080
ADD target/springboot-javatechi-kubenates.jar springboot-javatechi-kubenates.jar
ENTRYPOINT ["java","-jar","/springboot-javatechi-kubenates.jar"]