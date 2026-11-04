// Run unit tests, then build the jar
def call(String appDir = 'app') {
    dir(appDir) {
        sh 'mvn test'
        sh 'mvn clean package -DskipTests'
    }
}
