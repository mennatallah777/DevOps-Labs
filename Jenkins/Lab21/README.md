# Lab 21: Jenkins Pipeline for Application Deployment

A Jenkins pipeline that builds [DemoApp](https://github.com/Ibrahim-Adel15/DemoApp), pushes its image to DockerHub, and deploys it to the Kubernetes cluster.

## 1. Jenkins agent on the VM

Jenkins runs in a container that has no `docker`, `mvn`, `trivy` or `kubectl`. The master VM has all of them, so it is added as an agent.

**Manage Jenkins → Nodes → New Node** → `vm-agent`, Permanent Agent:

| Field | Value |
|---|---|
| Remote root directory | `/home/mennatallahyasser/jenkins-agent` |
| Labels | `vm-agent` |
| Usage | Only build jobs with label expressions matching this node |
| Launch method | Launch agent by connecting it to the controller |

Start the agent on the VM, with the secret from the node page saved in `secret-file`:

```bash
cd ~/jenkins-agent
echo <secret> > secret-file && chmod 600 secret-file
curl -sO http://192.168.111.128:8080/jnlpJars/agent.jar
nohup java -jar agent.jar -url http://192.168.111.128:8080/ -secret @secret-file -name vm-agent -webSocket -workDir /home/mennatallahyasser/jenkins-agent > agent.log 2>&1 &
```

## 2. DockerHub credential

DockerHub access token saved in **Manage Jenkins → Credentials** as `Username with password`, ID `dockerhub`.

## 3. Pipeline

[`Jenkinsfile`](Jenkinsfile) runs on `vm-agent`:

| Stage | Command |
|---|---|
| Clone | `git` DemoApp into `app/` |
| Build App | `mvn clean package -DskipTests` |
| Build Docker Image | `docker build -t mennayasser777/demoapp:<build> .` |
| Scan Docker Image | `trivy image --severity HIGH,CRITICAL` |
| Push Image | `docker login` + `docker push` |
| Delete Image Locally | `docker rmi` |
| Update Deployment File | `sed` the new image into [`deployment.yaml`](deployment.yaml) |
| Deploy to Kubernetes | `kubectl apply` + `kubectl rollout status` |

Post actions: `always` logs out of DockerHub and cleans the workspace, `success` and `failure` print the result.

The job is a **Pipeline** with **Pipeline script from SCM**: repo `https://github.com/mennatallah777/DevOps-Labs.git`, branch `main`, script path `Jenkins/Lab21/Jenkinsfile`.

The app is reachable at `http://<node-ip>:30080`.
