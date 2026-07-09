pipeline {
    agent any
    environment {
        JAVA_HOME = 'C:\\Users\\Никита\\.jdks\\liberica-21.0.11'
        PATH = "${JAVA_HOME}\\bin;${env.PATH}"
    }
    stages {
        stage('Debug') {
            steps {
                bat 'echo === JAVA_HOME ==='
                bat 'echo %JAVA_HOME%'
                bat 'echo === Java version ==='
                bat 'java -version'
                bat 'echo === Gradle wrapper files ==='
                bat 'dir gradle\\wrapper'
                bat 'echo === gradle-wrapper.properties ==='
                bat 'type gradle\\wrapper\\gradle-wrapper.properties'
                bat 'echo === Try to run wrapper with explicit java ==='
                bat 'cmd /c "%JAVA_HOME%\\bin\\java.exe -jar gradle\\wrapper\\gradle-wrapper.jar -v"'
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