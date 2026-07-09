pipeline {
    agent any
    stages {
        stage('Debug') {
            steps {
                bat 'echo Current directory: %cd%'
                bat 'dir'
                bat 'java -version'
                bat 'gradlew.bat -v'
            }
        }
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