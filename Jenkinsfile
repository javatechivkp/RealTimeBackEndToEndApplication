pipeline {
    agent any
    stages{
        stage('Build Maven'){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: 'master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/javatechivkp/RealTimeBackEndToEndApplication.git']]])
                bat 'mvn clean install'
            }
        }
        stage('Build docker image'){
            steps{
                script{
                    bat 'docker build -t thrishank99/springboot-backendapp-docker .'
                }
            }
        
        }
       stage('Push image to Hub'){
            steps{
                script{
                
                   withCredentials([usernamePassword(credentialsId: 'javatechidockerhub', passwordVariable: 'javatechidockerhub', usernameVariable: 'thrishank99')]) {
                   bat "docker login -u ${env.thrishank99} -p ${env.javatechidockerhub}"
}
                  bat 'docker push thrishank99/springboot-backendapp-docker'
                }
            }
        }
       /* stage('build & SonarQube Analysis'){
            steps{
              withSonarQubeEnv('sonarqubescannernew'){
              bat 'mvn clean package sonar:sonar'
            }
}
        } */
       /* stage('Deploy to k8s'){
            steps{
                script{
                   // kubernetesDeploy (configs: 'deploymentservice.yaml',kubeconfigId: 'k8sconfigpwd')
                    kubeconfig(credentialsId: 'javatechikubernetes', serverUrl: ' https://127.0.0.1:60861'){
					////bat 'kubectl apply -f mysql-configMap.yaml'
                   // bat 'kubectl apply -f mysql-secrets.yaml'
                    bat 'kubectl apply -f db-deployment.yaml'
                    //bat 'kubectl apply -f k8s-app.yaml'
}
                }
            }*/
        }   
        }
        }
        }