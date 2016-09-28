node {

   stage('Preparation') {
      git 'https://github.com/evandor/skysail.git'
   }
   
   stage('build') {
      buildCode()
   }

   stage('document') {
      parallel (
	    //code:    { buildCode() },
		doc:     { build 'skysail.doc' },
   	    javadoc: { build 'skysail.javadoc' }
	  )
   }

   stage('cucumber') {
	 build 'skysail.cucumber'
   }   
   
   stage('deployment') {
	  build 'ssp.demo.export.int'
   }
   
   stage('stresstest') {
     sh './gradlew skysail.product.demo.e2e.gatling:gatlingRun -DbaseUrl=http://demo.int.skysail.io/'
   }
   
}

def buildCode() {
    if (isUnix()) {
      sh './gradlew clean build'
    } else {
      bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
    }
}