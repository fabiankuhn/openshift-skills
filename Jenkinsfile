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

            openshift.newApp('openshift-testapp:latest~https://github.com/fabiankuhn/openshift-testapp.git')

          }

        }

      }

    }

  }

}
