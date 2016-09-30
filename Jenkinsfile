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
   	    javadoc: { buildJavadoc() }
	  )
   }

   stage('cucumber') {
	 build 'skysail.cucumber'
   }   
   
   stage('deployment') {
	  build 'ssp.demo.export.int'
   }
   
   stage('stresstest') {
     sh './gradlew skysail.product.demo.e2e.gatling:gatlingRun'
   }
   
}

def buildCode() {
  sh './gradlew clean build'
}

def buildJavadoc() {
  sh './gradlew javadoc'
  publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'skysail.server/generated/docs/javadoc', reportFiles: 'index.html', reportName: 'Javadoc'])
}