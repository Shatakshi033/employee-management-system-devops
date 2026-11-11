pipeline {
  agent any
  options { timestamps(); disableConcurrentBuilds() }
  environment { APP_NAME='employee-management'; K8S_NAMESPACE='employee-system' }
  stages {
    stage('Checkout') { steps { checkout scm } }
    stage('Build and Test') { steps { sh 'mvn -B clean verify' } }
    stage('Build and Push Image') {
      steps {
        script {
          env.IMAGE_TAG = "${env.BUILD_NUMBER}"
          withCredentials([usernamePassword(credentialsId:'dockerhub-creds',usernameVariable:'DOCKERHUB_USER',passwordVariable:'DOCKERHUB_TOKEN')]) {
            env.IMAGE_REPO = "${DOCKERHUB_USER}/${APP_NAME}"
            sh '''
              echo "$DOCKERHUB_TOKEN" | docker login -u "$DOCKERHUB_USER" --password-stdin
              docker build -t "$IMAGE_REPO:$IMAGE_TAG" -t "$IMAGE_REPO:latest" .
              docker push "$IMAGE_REPO:$IMAGE_TAG"
              docker push "$IMAGE_REPO:latest"
              docker logout
            '''
          }
        }
      }
    }
    stage('Deploy to Kubernetes') {
      steps {
        withCredentials([file(credentialsId:'eks-kubeconfig',variable:'KUBECONFIG')]) {
          sh '''
            kubectl apply -f k8s/namespace.yaml
            kubectl apply -n "$K8S_NAMESPACE" -f k8s/configmap.yaml
            kubectl apply -n "$K8S_NAMESPACE" -f k8s/secret.yaml
            kubectl apply -n "$K8S_NAMESPACE" -f k8s/mysql-pvc.yaml
            kubectl apply -n "$K8S_NAMESPACE" -f k8s/mysql-deployment.yaml
            kubectl apply -n "$K8S_NAMESPACE" -f k8s/mysql-service.yaml
            kubectl apply -n "$K8S_NAMESPACE" -f k8s/deployment.yaml
            kubectl apply -n "$K8S_NAMESPACE" -f k8s/service.yaml
            kubectl apply -n "$K8S_NAMESPACE" -f k8s/hpa.yaml
            kubectl set image deployment/employee-app employee-app="$IMAGE_REPO:$IMAGE_TAG" -n "$K8S_NAMESPACE"
            kubectl rollout status deployment/employee-app -n "$K8S_NAMESPACE" --timeout=180s
          '''
        }
      }
    }
  }
  post {
    always { sh 'docker image prune -f || true' }
    success { echo 'Deployment completed.' }
    failure { echo 'Pipeline failed. Review stage logs.' }
  }
}
