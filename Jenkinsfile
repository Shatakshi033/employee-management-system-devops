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
