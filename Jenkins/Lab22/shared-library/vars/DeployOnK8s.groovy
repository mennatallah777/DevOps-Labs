// Put the new image in the deployment file, then deploy it
def call(Map args) {
    def image = "${args.image}:${args.tag}"

    sh "sed -i 's|image: .*|image: ${image}|' ${args.file}"
    sh "grep image: ${args.file}"

    sh "kubectl apply -f ${args.file}"
    sh "kubectl rollout status deployment/${args.deployment} --timeout=180s"
}
