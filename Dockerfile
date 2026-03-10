FROM eclipse-temurin:21-jdk
EXPOSE 8080
ADD target/springboot-aws-eks-pipeline-application.jar springboot-aws-eks-pipeline-application.jar
ENTRYPOINT ["java","-jar","/springboot-aws-eks-pipeline-application.jar"]