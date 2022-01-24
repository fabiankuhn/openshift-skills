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
    tools {
        jdk "JDK 17" // Tool defined in Jenkins -> Manage Jenkins -> Global Tool Config -> JDK (see docs)
    }
    agent {
        label 'maven' // Starts automatically
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

         stage('test') {
             steps {
                 sh "./gradlew --no-daemon clean check"
             }
             post {
                 always {
                     junit '**/test-results/test/*.xml'
                 }
             }
         }

        stage('build docker image') {
            steps {
                sh "oc apply -f openshift/image-stream-config.yaml"
                sh "oc apply -f openshift/service-config.yaml"
                sh "oc start-build java-backend --from-dir=backend --follow --wait"
            }
        }

        stage('deploy') {
            steps {
                sh "oc apply -f openshift/service-config.yaml"
                sh "oc apply -f openshift/router-config.yaml"
                sh "oc apply -f openshift/deployment-config.yaml"
            }
        }
    }
}
