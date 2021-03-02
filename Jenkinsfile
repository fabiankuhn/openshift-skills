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

        stage('test') {
            tools {
                jdk "jdk-11.0.1" // Tool defined in Jenkins -> Manage Jenkins -> Global Tool Config -> JDK (see docs)
            }
            agent {
                label 'maven' // Starts automatically
            }
            steps {
                sh "./gradlew --no-daemon clean check"
            }
            post {
                always {
                    junit '**/test-results/test/*.xml'
                }
            }
        }


        // TODO: deploy specific artefact/tag
        stage('build') {
            tools {
                jdk "jdk-11.0.1"
            }
            agent {
                label 'maven' // Starts automatically
            }
            steps {

                // TODO document how to create image stream and build config: oc new-build --strategy docker --binary --docker-image openjdk:11-slim --name java-backend

                sh "./gradlew clean assemble" //--no-deamon? clean?

                script {
                    openshift.withCluster() {

                        def buildConfig = openshift.selector("bc", "java-backend")
                        buildConfig.startBuild("--from-dir backend", "--wait")
                        def builds = buildConfig.related('builds')
                        builds.describe()
                    }
                }
            }
        }

        stage('build python app') {
            steps {

                script {
                    openshift.withCluster() {

                        def buildConfig = openshift.selector("bc", "python-app")
                        buildConfig.startBuild("--from-dir python", "--wait")
                        def builds = buildConfig.related('builds')
                        builds.describe()
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
