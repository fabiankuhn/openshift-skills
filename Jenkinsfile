#!groovy

// Inspiration
// https://access.redhat.com/solutions/4677131
// https://www.openshift.com/blog/building-declarative-pipelines-openshift-dsl-plugin
// https://stackoverflow.com/questions/52195748/build-an-image-from-dockerfile-using-pipeline-with-openshift-jenkins-client-plug
// https://developers.redhat.com/blog/2017/11/20/building-declarative-pipelines-openshift-dsl-plugin/


// Testing
// https://andywis.github.io/tech_blog/openshift-ci-part2.html


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

//        stage('test'){
//            steps {
//                withGradle {
//                    sh './gradlew clean check'
//                }
//            }
//        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }

        // TODO: make github private
        // TODO: build specific branch

        // TODO: deploy specific artefact/tag

        // TODO remove deploy step
        stage('build') {
            steps {
                script {
                    openshift.withCluster() {


                        def buildConfig = openshift.selector("bc", "openshift-testapp")
                        buildConfig.startBuild("--wait")
                        def builds = buildConfig.related('builds')
                        builds.describe()

                        sh "ls -la"

//                        def buildConfig = openshift.selector("bc", "openshift-testapp")
//                        buildConfig.startBuild("--from-dir python", "--wait")
//                        def builds = buildConfig.related('builds')
//                        builds.describe()


                        // TODO dockerfile: run build in docker container...
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

            // TODO do not deploy if fails

            steps {
                echo 'Deploying....'
            }
        }
    }
}
