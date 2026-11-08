// Put the new image in the deployment file, then deploy it.
// gitPush: true commits and pushes the file so Argo CD syncs it (Lab 23);
// otherwise it is applied with kubectl (Lab 22).
def call(Map args) {
    def image = "${args.image}:${args.tag}"

    sh "sed -i 's|image: .*|image: ${image}|' ${args.file}"
    sh "grep image: ${args.file}"

    if (args.gitPush) {
        withCredentials([usernamePassword(credentialsId: args.credentialsId, usernameVariable: 'GIT_USER', passwordVariable: 'GIT_TOKEN')]) {
            sh """
                git config user.name 'Jenkins'
                git config user.email 'jenkins@ivolve.local'
                git add ${args.file}
                git commit -m 'Deploy ${image} [skip ci]' || echo 'No change to commit'
                git push https://\$GIT_USER:\$GIT_TOKEN@${args.repo} HEAD:${args.branch ?: 'main'}
            """
        }
    } else {
        sh "kubectl apply -f ${args.file}"
        sh "kubectl rollout status deployment/${args.deployment} --timeout=180s"
    }
}
