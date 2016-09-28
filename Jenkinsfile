node {
   //def mvnHome
   stage('Preparation') { // for display purposes
      git 'https://github.com/evandor/skysail.git'
   }
   stage('Build') {
      // Run the maven build
      if (isUnix()) {
         sh 'gradle clean build'
      } else {
         bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
      }
   }
   stage('Results') {
      junit '**/target/surefire-reports/TEST-*.xml'
      archive 'target/*.jar'
   }
}
