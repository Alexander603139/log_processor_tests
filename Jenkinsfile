pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                bat 'gradlew.bat clean build -x test'
            }
        }
        stage('Test') {
            steps {
                bat 'gradlew.bat test'
            }
        }
    }
}