pipeline {
    agent any
     tools {
            maven 'Maven3'   // Must match the name configured in Jenkins Global Tool Configuration
        }

    environment {
        DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
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
