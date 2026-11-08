// Build, scan, push, then delete the image locally
def call(Map args) {
    def image = "${args.image}:${args.tag}"
    def appDir = args.appDir ?: 'app'

    dir(appDir) {
        sh "docker build -t ${image} ."
    }

    sh "trivy image --pkg-types os --scanners vuln --severity HIGH,CRITICAL --exit-code 0 --no-progress ${image}"

    withCredentials([usernamePassword(credentialsId: args.credentialsId, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
        sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
        sh "docker push ${image}"
    }

    sh "docker rmi ${image}"
    // The VM disk is small: drop the build cache so master doesn't hit disk pressure
    sh 'docker builder prune -f'
}
