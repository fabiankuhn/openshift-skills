#!groovy

// Inspiration
// https://access.redhat.com/solutions/4677131
// https://www.openshift.com/blog/building-declarative-pipelines-openshift-dsl-plugin
// https://stackoverflow.com/questions/52195748/build-an-image-from-dockerfile-using-pipeline-with-openshift-jenkins-client-plug
// https://developers.redhat.com/blog/2017/11/20/building-declarative-pipelines-openshift-dsl-plugin/


// Testing
// https://andywis.github.io/tech_blog/openshift-ci-part2.html

// Official Jenkins Doc
// https://www.jenkins.io/doc/tutorials/build-a-java-app-with-maven/

// Binary Builds (!!!)
// https://docs.openshift.com/container-platform/3.6/dev_guide/dev_tutorials/binary_builds.html


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

//        stage('test') {
//            tools {
//                jdk "jdk-11.0.1" // Tool defined in Jenkins -> Manage Jenkins -> Global Tool Config -> JDK (see docs)
//            }
//            agent {
//                label 'maven' // Starts automatically
//            }
//            steps {
//                sh "./gradlew --no-daemon build" // TODO make tests only. build step before
//
//                stash name: 'backend-build', includes: 'backend/app/build/**/*'
//            }
//            post {
//                always {
//                    junit '**/test-results/test/*.xml'
//                }
//            }
//        }


        // TODO: make github private
        // TODO: build specific branch
        // TODO: deploy specific artefact/tag
        // TODO remove deploy step
        stage('build') {
            steps {

                dir('unstash') {
                    unstash 'backend-build'
                }

                sh "ls -la unstash/backend/app/build/libs"

                // File is called 'app-0.0.1-SNAPSHOT.jar'


                script {
                    openshift.withCluster() {


                        // sh "ls -la"
                        // sh "cd python && ls -la && cd .."


                        def buildConfig = openshift.selector("bc", "java-backend")
                        buildConfig.startBuild("--from-dir backend", "--wait")
                        def builds = buildConfig.related('builds')
                        builds.describe()


                        // def buildConfig = openshift.selector("bc", "openshift-testapp")
                        // buildConfig.startBuild("--from-file python/Dockerfile") // TODO wait for it to finish
                        // def builds = buildConfig.related('builds')
                        // builds.describe()


                        // TODO dockerfile: run build in docker container...
                    }
                }
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
