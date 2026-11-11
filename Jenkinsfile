
pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        APP_NAME = 'employee-management'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvn -B clean verify'
            }
        }

        stage('Build and Push Image') {
            steps {
                script {
                    env.IMAGE_TAG = "${env.BUILD_NUMBER}"

                    withCredentials([
                        usernamePassword(
                            credentialsId: 'dockerhub-creds',
                            usernameVariable: 'DOCKERHUB_USER',
                            passwordVariable: 'DOCKERHUB_TOKEN'
                        )
                    ]) {
                        env.IMAGE_REPO = "${env.DOCKERHUB_USER}/${env.APP_NAME}"

                        bat '''
                            @echo off
                            echo %DOCKERHUB_TOKEN% | docker login -u "%DOCKERHUB_USER%" --password-stdin

                            docker build -t "%IMAGE_REPO%:%IMAGE_TAG%" -t "%IMAGE_REPO%:latest" .
                            docker push "%IMAGE_REPO%:%IMAGE_TAG%"
                            docker push "%IMAGE_REPO%:latest"

                            docker logout
                        '''
                    }
                }
            }
        }
    }

    post {
        always {
            bat 'docker image prune -f'
        }

        success {
            echo 'Build, test and Docker Hub push completed.'
        }

        failure {
            echo 'Pipeline failed. Review stage logs.'
        }
    }
}
