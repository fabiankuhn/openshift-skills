#!groovy
def artifactId = env.BRANCH_NAME

pipeline {
    tools {
        jdk "JDK 17" // Tool defined in Jenkins -> Manage Jenkins -> Global Tool Config -> JDK (see docs)
    }

    agent {
        label 'maven'
    }

    stages {
        stage('preamble') {
            steps {
                echo "Branch: " + env.BRANCH_NAME
                echo "Commit: " + env.GIT_COMMIT
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
            steps {
                sh "oc process -f openshift/build-config.tpl.yaml -p DOCKER_TAG=${artifactId} | oc apply -f -"
                sh "oc start-build java-backend --from-dir=backend --follow --wait"
            }
        }

        stage('deploy') {
            steps {
                sh "oc apply -f openshift/service-config.yaml"
                sh "oc apply -f openshift/router-config.yaml"
                sh "oc process -f openshift/deployment-config.tpl.yaml -p DOCKER_TAG=${artifactId} | oc apply -f -"
                sh "oc rollout latest dc/java-backend"
            }
        }
    }
}
