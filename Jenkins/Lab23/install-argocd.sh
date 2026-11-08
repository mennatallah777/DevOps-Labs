#!/bin/bash
# Install Argo CD, expose its UI on a NodePort, and print the admin password
set -e

kubectl create namespace argocd --dry-run=client -o yaml | kubectl apply -f -
kubectl apply -n argocd --server-side --force-conflicts -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

# Not needed for this lab: SSO (dex), notifications, ApplicationSets. Saves memory and disk on the small nodes.
kubectl -n argocd scale deploy argocd-dex-server argocd-notifications-controller argocd-applicationset-controller --replicas=0

# UI on https://<node-ip>:30443
kubectl -n argocd patch svc argocd-server -p '{"spec":{"type":"NodePort","ports":[{"name":"http","port":80,"targetPort":8080,"nodePort":30880},{"name":"https","port":443,"targetPort":8080,"nodePort":30443}]}}'

echo "Waiting for Argo CD to start..."
kubectl -n argocd rollout status deploy/argocd-server --timeout=600s
kubectl -n argocd rollout status deploy/argocd-repo-server --timeout=600s
kubectl -n argocd rollout status statefulset/argocd-application-controller --timeout=600s

echo
echo "Argo CD UI: https://192.168.111.129:30443"
echo "Username:   admin"
echo -n "Password:   "
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath='{.data.password}' | base64 -d
echo
