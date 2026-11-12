
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

                        powershell '''
                            $env:DOCKERHUB_TOKEN | docker login --username $env:DOCKERHUB_USER --password-stdin
                            if ($LASTEXITCODE -ne 0) { exit 1 }

                            docker build -t "$env:IMAGE_REPO`:$env:IMAGE_TAG" -t "$env:IMAGE_REPO`:latest" .
                            if ($LASTEXITCODE -ne 0) { exit 1 }

                            docker push "$env:IMAGE_REPO`:$env:IMAGE_TAG"
                            if ($LASTEXITCODE -ne 0) { exit 1 }

                            docker push "$env:IMAGE_REPO`:latest"
                            if ($LASTEXITCODE -ne 0) { exit 1 }

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
