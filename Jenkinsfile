#!groovy

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
                echo "Using project:"
                sh "oc project"
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
                sh "oc apply -f openshift/image-stream-config.yaml"
                sh "oc apply -f openshift/build-config.yaml"
                sh "oc start-build java-backend --from-dir=backend --follow --wait"
            }
        }

        stage('deploy') {
            steps {
                sh "oc apply -f openshift/service-config.yaml"
                sh "oc apply -f openshift/router-config.yaml"
                sh "oc apply -f openshift/deployment-config.yaml"
                sh "oc rollout latest dc/java-backend"
            }
        }
    }
}
