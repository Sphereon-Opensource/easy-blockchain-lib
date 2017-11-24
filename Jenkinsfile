node {

    // Checkout code from repository
    stage('Checkout source') {
        checkout scm
    }

    stage('Build easy-blockchain-lib') {
		withMaven(
				// Maven installation declared in the Jenkins "Global Tool Configuration"
				maven: 'M3')
			{

            // Run the maven build (works on both linux and windows)
			sh "mvn clean install"

		}
		// withMaven will discover the generated Maven artifacts, JUnit Surefire & FailSafe reports and FindBugs reports
	}
}