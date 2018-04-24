pipeline {
  agent any
  stages {
    stage('build') {
      parallel {
        stage('build') {
          steps {
            sh 'echo "Hello World"'
            sh '''
                    echo "Multiline shell steps works too"
                    ls -lah
                '''
          }
        }
        stage('error') {
          steps {
            hipchatSend(token: 'XoqidjjH7rxYbEugHBM3OXG47QNmWvap2xrsfBvX', message: 'Hello!', color: 'RED', room: 'AndroidTestAutomation  Android Automation build updates Make a video callIntegrationsRoom actions', sendAs: 'DarienTest', server: 'api.hipchat.com')
          }
        }
      }
    }
    stage('Sanity check') {
      steps {
        input 'Does the staging environment look ok?'
      }
    }
  }
  post {
    always {
      echo 'This will always run'
      
    }
    
    success {
      echo 'This will run only if successful'
      
    }
    
    failure {
      echo 'This will run only if failed'
      
    }
    
    unstable {
      echo 'This will run only if the run was marked as unstable'
      
    }
    
    changed {
      echo 'This will run only if the state of the Pipeline has changed'
      echo 'For example, if the Pipeline was previously failing but is now successful'
      
    }
    
  }
}