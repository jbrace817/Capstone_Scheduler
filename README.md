#Scheduler application - C195 Performance Assessment.

- Author: James Brace
- Email: jbrace4@wgu.edu
- Version: 30
- Date: April 21, 2022

#Purpose

- Our company has been contracted to develop a GUI-Based scheduling application that conducts business in multiple languanges.

#Development Evironment

- IntelliJ IDEA 2021.3(Community Edition)
- Build #IC-213.5744.223, built on November 27, 2021
- Runtime version: 11.0.13+7-b1751.19 amd64
- VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
- JDK 17.0.1
- Non-Bundled Plugins:
  - com.intellij.javafx (1.0.4)
- Kotlin: 213-1.5.10-release-949-IJ5744.223

#How to setup the program
Find the config.properties.CHANGEME file and delete the "CHANGEME" extension. Update jdbcUrl, dbUsername, dbName, and dbPassword
fields to match that of your database.

- Open the "scheduler" folder that contains the source code created in intellij.
- In intellij go to file project structure > Project > choose SDK 17 and change language level to 11 Local variable syntax for Lambda parameters.
- In Intellij go to file > project structure > Modules and add your SDK, mysql-connector-java-80.25, and the javafx library. 
- run the program

#Additional Report
- The report I created lists all customers, their phone number, country, division and the month and date they joined. 