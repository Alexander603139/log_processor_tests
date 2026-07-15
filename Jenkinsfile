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
        stage('Send Email') {
            steps {
                emailext (
                    subject: "Allure Report for Build ${env.BUILD_NUMBER}",
                    body: """
                        <p>Allure report is ready:</p>
                        <a href="${env.BUILD_URL}allure">${env.BUILD_URL}allure</a>
                    """,
                    to: 'info0@mail.ru',  // или оставить пустым – будет использоваться Default Recipients
                    mimeType: 'text/html'
                )
            }
        }
    }
}