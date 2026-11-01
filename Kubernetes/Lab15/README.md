# Lab 15: Node.js Application Deployment with ClusterIP Service

Deploy the Node.js app from Lab 8 on the cluster, connected to the MySQL StatefulSet from Lab 14.

> The lab names them `ivolveApp` and `ivolveAppSvc`, but Kubernetes object names must be lowercase, so they are named `ivolveapp` and `ivolveappsvc`.

## Files

- [deployment.yaml](deployment.yaml): 2 replicas of `mennayasser777/ivolve-app:v1`
  - env from the `ivolve-cm` ConfigMap and `ivolve-secret` Secret (Lab 12)
  - `nodeSelector: project=ivolve` and a toleration for `workload=ivolveApp:NoSchedule`, so pods run on `worker1` (Lab 10)
  - `ivolve-pvc` (Lab 13) mounted at `/app/logs`
  - an init container (`mysql:8.0`) that creates the `ivolve` database and grants `ivolve_user` privileges on it
  - `maxSurge: 0` because the Lab 11 quota allows only 3 pods in the namespace, so the update must remove an old pod before creating a new one
- [service.yaml](service.yaml): ClusterIP service on port 80, forwarding to port 3000 on the app pods

## Commands

```bash
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
kubectl get pods -n ivolve -o wide
kubectl get endpoints ivolveappsvc -n ivolve
kubectl logs -n ivolve -l app=ivolveApp -c init-db
kubectl exec -it ivolve-db-0 -n ivolve -- mysql -h ivolve-db-svc -u root -p
kubectl logs -n ivolve -l app=ivolveApp -c ivolveapp
```

Inside MySQL:

```sql
SHOW DATABASES;
SELECT user, host FROM mysql.user;
SHOW GRANTS FOR 'ivolve_user'@'%';
```

## Screenshots

All steps and results are in [lab_15.pdf](lab_15.pdf).
