#!groovy

pipeline {
    tools {
        jdk "JDK 17" // Tool defined in Jenkins -> Manage Jenkins -> Global Tool Config -> JDK (see docs)
    }

    agent {
        label 'maven'
    }

    stages {
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

        stage('build artifact id') {
            steps {
                script {
                    def artifactId = env.BRANCH_NAME
                }
            }
        }

        stage('build docker image') {
            steps {
                sh "oc process -f openshift/build-config.tpl.yaml -p DOCKER_TAG=${env.GIT_COMMIT} | oc apply -f -"
                sh "oc start-build java-backend --from-dir=backend --follow --wait"
            }
        }

        stage('deploy') {
            steps {
                sh "oc apply -f openshift/service-config.yaml"
                sh "oc apply -f openshift/router-config.yaml"
                sh "oc process -f openshift/deployment-config.tpl.yaml -p DOCKER_TAG=${env.GIT_COMMIT} | oc apply -f -"
                sh "oc rollout latest dc/java-backend"
//                currentBuild.description = {artifactId}
            }
        }
    }
}
