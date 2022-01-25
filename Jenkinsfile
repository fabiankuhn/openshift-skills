#!groovy

pipeline {

    // TODO webhook

    agent {
        label 'maven'
    }
//    tools {
//        jdk "openjdk-11"
//    }

    stages {
        stage('artifact id') {
            steps {
                script {
                    env.artifact_id = env.GIT_COMMIT[0..6]
                    currentBuild.description = "${env.artifact_id}"
                    echo "Artifact Identifier: ${env.artifact_id}"
                }
            }
        }

        stage('build') {
            steps {
                sh "./gradlew clean assemble"
            }
        }

//         stage('test') {
//             steps {
//                 sh "./gradlew --no-daemon test"
//             }
//             post {
//                 always {
//                     junit '**/test-results/test/*.xml'
//                 }
//             }
//         }

        stage('build docker image') {
            // TODO do not send whole backend folder but build folder with dockerfile

            steps {
                sh "oc process -f openshift/build-config.tpl.yaml -p DOCKER_TAG=${env.artifact_id} | oc apply -f -"
                sh "oc start-build java-backend --follow --wait"
            }
        }

        stage('update openshift') {
            steps {
                sh "oc apply -f openshift/service-config.yaml"
                sh "oc apply -f openshift/router-config.yaml"
                sh "oc process -f openshift/deployment-config.tpl.yaml -p DOCKER_TAG=${env.artifact_id} | oc apply -f -"
            }
        }

        stage('deploy') {
            steps {
                sh "oc rollout latest dc/java-backend"
                sh "oc rollout status dc/java-backend"
            }
        }
    }
}
