# Employee Management System – DevOps Project

Java 21 + Spring Boot REST API + MySQL + Docker + Jenkins + Docker Hub + Kubernetes.

## Prerequisites
Java 21, Maven 3.9+, Git, Docker Engine/Compose v2. For Kubernetes: a cluster, kubectl, a working StorageClass/PVC provisioner, and Metrics Server for HPA.

## 1. Run locally
Copy `.env.example` to `.env`, edit both passwords, then:
```bash
docker compose up --build -d
docker compose ps
docker compose logs -f app
```
API: `http://localhost:8080/api/employees`; health: `http://localhost:8080/actuator/health`.

Stop: `docker compose down`. Destructive database-volume removal: `docker compose down -v`.

## 2. Test API
Create:
```bash
curl -X POST http://localhost:8080/api/employees -H "Content-Type: application/json" -d '{"firstName":"Asha","lastName":"Sharma","email":"asha@example.com","department":"Engineering","salary":65000}'
```
List: `curl http://localhost:8080/api/employees`
Get: `curl http://localhost:8080/api/employees/1`
Update:
```bash
curl -X PUT http://localhost:8080/api/employees/1 -H "Content-Type: application/json" -d '{"firstName":"Asha","lastName":"Sharma","email":"asha@example.com","department":"Platform","salary":72000}'
```
Delete: `curl -X DELETE http://localhost:8080/api/employees/1`

## 3. GitHub
Create a GitHub repository, then:
```bash
git init
git add .
git commit -m "Initial Employee Management System"
git branch -M main
git remote add origin https://github.com/YOUR_GITHUB_USERNAME/employee-management-system.git
git push -u origin main
```

## 4. Docker Hub
```bash
docker build -t YOUR_DOCKERHUB_USERNAME/employee-management:1.0 .
docker login
docker push YOUR_DOCKERHUB_USERNAME/employee-management:1.0
```

## 5. Kubernetes
Before applying: replace the passwords in `k8s/secret.yaml`, replace `YOUR_DOCKERHUB_USERNAME` in `k8s/deployment.yaml`, and ensure a default StorageClass exists (or set `storageClassName` in the PVC).
```bash
kubectl get nodes
kubectl apply -f k8s/namespace.yaml
kubectl apply -n employee-system -f k8s/configmap.yaml
kubectl apply -n employee-system -f k8s/secret.yaml
kubectl apply -n employee-system -f k8s/mysql-pvc.yaml
kubectl apply -n employee-system -f k8s/mysql-deployment.yaml
kubectl apply -n employee-system -f k8s/mysql-service.yaml
kubectl apply -n employee-system -f k8s/deployment.yaml
kubectl apply -n employee-system -f k8s/service.yaml
kubectl apply -n employee-system -f k8s/hpa.yaml
kubectl get all,pvc,hpa -n employee-system
kubectl rollout status deployment/employee-app -n employee-system
kubectl get svc employee-app -n employee-system
```
Open `http://EXTERNAL_ADDRESS/api/employees` when the LoadBalancer receives an external address. For minikube, use `minikube service employee-app -n employee-system`.

## 6. Jenkins
Install Pipeline, Git and Credentials Binding plugins. The Jenkins agent needs Java 21, Maven, Docker CLI/daemon access and kubectl.
Create credentials:
- `dockerhub-creds`: Username/password credential; use a Docker Hub access token as password.
- `eks-kubeconfig`: Secret file credential containing kubeconfig for the target cluster.

Create a Pipeline job from SCM and point it at `Jenkinsfile`. The pipeline runs Maven tests, builds and pushes a versioned Docker image, applies Kubernetes manifests and updates the Deployment. The kubeconfig identity needs permission to manage the resources applied by the pipeline. Configure the cluster/database and secret safely before running.

## 7. AWS EKS
After creating an EKS cluster and node group:
```bash
aws eks update-kubeconfig --region YOUR_AWS_REGION --name YOUR_EKS_CLUSTER
kubectl get nodes
```
Use a dedicated least-privilege Jenkins identity. A public LoadBalancer can incur AWS charges.

## Production caveats
This is a learning baseline, not a hardened production platform. Do not commit real secrets; use AWS Secrets Manager/External Secrets. Add API authentication/authorization and HTTPS, Flyway/Liquibase migrations, database backups, monitoring/logging, image scanning/signing, network policies, and least-privilege IAM/RBAC. Consider Amazon RDS instead of self-managed MySQL for production.

## Lecture mapping
Linux/Git/networking: host and repository; Jenkins: Jenkinsfile; Docker multi-stage/Compose/registry: Dockerfile, compose and Docker Hub; Kubernetes controllers/services/configuration/resources: manifests; production, autoscaling and RBAC: probes, PVC, HPA and Role/RoleBinding.
