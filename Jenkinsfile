pipeline {

  agent any

  stages {

    stage('Build') {

      when {

        expression {

          openshift.withCluster() {

            return !openshift.selector('bc', 'mapit-spring').exists();

          }

        }

      }

      steps {

        script {

          openshift.withCluster() {

            openshift.newApp('redhat-openjdk18-openshift:1.1~https://github.com/fabiankuhn/openshift-testapp.git')

          }

        }

      }

    }

  }

}
