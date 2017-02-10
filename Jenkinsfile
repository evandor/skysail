node {

   stage('Preparation') {
      git 'https://github.com/evandor/skysail.git'
   }
   
   stage('build') {
      buildCode()
   }

   stage('cucumber') {
	 build 'skysail.cucumber'
   }   
   
   stage('deployment.int') {
      parallel (
  	    demo { build 'ssp.demo.export.int' },
	    pact { build 'ssp.pact.export.int' },
	  )
   }

   stage('document') {
      parallel (
	    //code:    { buildCode() },
		doc:     { build 'skysail.doc' },
   	    javadoc: { buildJavadoc() }
	  )
   }
   
   stage('stresstest') {
     sh './gradlew skysail.product.demo.e2e.gatling:gatlingRun -DbaseUrl=http://demo.int.skysail.io/'
   }
   
}

def buildCode() {
  sh './gradlew build'
}

def buildJavadoc() {
  sh './gradlew javadoc'
  publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'skysail.server/generated/docs/javadoc', reportFiles: 'index.html', reportName: 'Javadoc'])
}