pipeline {
  agent any
  stages {
    stage('build') {
       steps {
                sh 'echo "Hello World"'
                sh '''
                    echo "Multiline shell steps works too"
                    ls -lah
                '''
                sh './name.sh John Smith'
            }
    }
  }
}
