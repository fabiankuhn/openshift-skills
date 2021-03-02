#!groovy

pipeline {
    agent any

    stages {
        stage('preamble') {
            steps {
                script {
                    openshift.withCluster() {
                        openshift.withProject() {
                            echo "Using project: ${openshift.project()}"
                        }
                    }
                }
            }
        }
        // TODO: make github private
        // TODO: build specific branch
        stage('build') {
            steps {
                script {
                    openshift.withCluster() {
                        openshift.withProject() {
                            def buildConfig = openshift.selector("bc", "openshift-testapp")
                            openshift.startBuild("openshift-testapp") // we started the build process
                            def builds = buildConfig.related('builds')
                            builds.describe()
                            // timeout(5) {
                            //     builds.untilEach(1) {
                            //         it.describe()
                            //         echo "Inside loop: ${it}"
                            //         return (it.object().status.phase == "Complete")
                            //     }
                            // }
                        }
                    }
                }
            }

        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
