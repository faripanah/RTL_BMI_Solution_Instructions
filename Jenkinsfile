pipeline {
    agent any
     tools {
            maven 'Maven3'   // Must match the name configured in Jenkins Global Tool Configuration
        }

    environment {
        SONARQUBE_SERVER = 'SonarQubeServer' //sonarqube server name
        SONAR_TOKEN = ''
        DOCKERHUB_CREDENTIALS_ID = 'docker_hub'
        DOCKERHUB_REPO = 'fari59/sep2_week2_2025_bmidemo'
        DOCKER_IMAGE_TAG = 'latest'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/ADirin/sep2_week2_inclassDemo_2025.git'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
       stage('SonarQube Analysis') {
                   steps {
                       withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')]) {
                           withSonarQubeEnv("${env.SONARQUBE_SERVER}") {
                               // First line is Mac local sonar-scanner path -> use correct path
                               sh """
                                   /usr/local/sonarscanner/bin/sonar-scanner \
                                   -Dsonar.projectKey=LectureDemo_SonarQube \
                                   -Dsonar.sources=src \
                                   -Dsonar.projectName=LectureDemo_SonarQube \
                                   -Dsonar.host.url=http://localhost:9000 \
                                   -Dsonar.login=${env.SONAR_TOKEN} \
                                   -Dsonar.java.binaries=target/classes \
                               """
                           }
                       }
                   }
               }



        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                }
            }
        }
        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
                        docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }
    }
}
