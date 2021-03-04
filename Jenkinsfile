#!groovy

// Important: Binary Builds (!!!)
// https://medium.com/@Kaza/how-to-build-from-docker-on-openshift-9638583f880a
// https://docs.openshift.com/container-platform/3.6/dev_guide/dev_tutorials/binary_builds.html

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

//         stage('test') {
//             tools {
//                 jdk "jdk-11.0.1" // Tool defined in Jenkins -> Manage Jenkins -> Global Tool Config -> JDK (see docs)
//             }
//             agent {
//                 label 'maven' // Starts automatically
//             }
//             steps {
//                 sh "./gradlew --no-daemon clean check"
//             }
//             post {
//                 always {
//                     junit '**/test-results/test/*.xml'
//                 }
//             }
//
//             // TODO stash build files
//         }
//
//         stage('build') {
//
//             parallel {
//
//                 // TODO unstash
//
//                 stage("build backend"){
//                     tools {
//                         jdk "jdk-11.0.1"
//                     }
//                     agent {
//                         label 'maven' // Starts automatically
//                     }
//                     steps {
//
//                         sh "./gradlew clean assemble" //--no-deamon? clean?
//
//                         script {
//                             openshift.withCluster() {
//
//                                 def buildConfig = openshift.selector("bc", "java-backend")
//                                 buildConfig.startBuild("--from-dir backend", "--wait")
//                                 def builds = buildConfig.related('builds')
//                                 builds.describe()
//                             }
//                         }
//                     }
//                 }
//
//                 stage("build python"){
//                     steps {
//
//                         script {
//                             openshift.withCluster() {
//
//                                 def buildConfig = openshift.selector("bc", "python-app")
//                                 buildConfig.startBuild("--from-dir python", "--wait")
//                                 def builds = buildConfig.related('builds')
//                                 builds.describe()
//                             }
//                         }
//                     }
//                 }
//             }
//         }

        stage('publish backen jib') {
            tools {
                jdk "jdk-11.0.1"
            }
            agent {
                label 'maven'
            }
//            steps {
//                script {
//                    openshift.withCluster() {
//                        openshift.withCredentials() {
//
//                            sh "./gradlew --no-daemon -Djib.console=plain :backend:app:jib"
//                            // withCredentials([usernamePassword(credentialsId: 'service-account-builder', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
////                            sh "./gradlew --no-daemon " +
////                                    // "-Djib.to.auth.username=$USERNAME -Djib.to.auth.password=$PASSWORD " +
////                                    "-Djib.console=plain " +
////                                    ":backend:app:jib"
//                        }
//                    }
//                }
//            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'service-account-builder', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                    sh "./gradlew --no-daemon -Djib.console=plain"
                    "-Djib.to.auth.username=$USERNAME -Djib.to.auth.password=$PASSWORD " +
                            "-Djib.console=plain " +
                            ":backend:app:jib"
                }
            }

        }

        stage('Deploy') {

            // TODO do not deploy if fails
            // TODO: deploy specific artefact/tag

            steps {
                echo 'Deploying....'
            }
        }
    }
}
