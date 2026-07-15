pipeline {
    agent any
    environment {
        JAVA_HOME = 'C:\\Users\\Никита\\.jdks\\liberica-21.0.11'
        PATH = "${JAVA_HOME}\\bin;C:\\Gradle\\gradle-9.6.1\\bin;${env.PATH}"
    }
    stages {
        stage('Build') {
            steps {
                bat 'gradle clean build -x test'
            }
        }
        stage('Test') {
            steps {
                bat 'gradle test'
            }
        }
        stage('Allure Report') {
            steps {
                allure([
                    includeProperties: false,
                    jdk: '',
                    report: 'build/reports/allure-report',
                    results: [[path: 'build/allure-results']]
                ])
            }
        }
//         3B2eQzAWErjQnoo3tIbL
        stage('Send Email') {
            steps {
                emailext (
                    subject: "Allure Report for Build ${env.BUILD_NUMBER}",
                    body: "Allure report: ${env.BUILD_URL}allure",
                    to: 'info0@mail.ru',
                    mimeType: 'text/html'
                )
            }
        }
    }
}