# the-exterminators_ticketing-system
# Group 5 &lt;The Exterminators> - Project for Software Engineering 2 - MCI

The Exteriminators proudly present their Project for Software Engineering 2 at Management Center Innsbruck, # Digital Business and Software Engineering Class of 2021.

Our Project-Application is a Bug and Defect Tracking Ticketing System, which would not only allow users to create, assign and solve tickets, but also manage projects (for ticket categorization), manage users and much more.

Feel free to check out our ticketing system to your heart's content.
Please keep in mind, we are currently connecting to a cloud-based SQL Instance for Data persistence.
Once that is shutdown the Database would have to be run locally and the database connection credentials would need to be adapted accordingly in src/main/resources/application.properties



# No matter which OS you're running (Unix, Linux, Windows) please make sure to have the following Pre-requisites installed and ready (Path environment Variables set, etc.)

1. Java JDK 17 or higher
2. Maven 3.9.2
3. Node.js 18.16
4. Docker Engine
5. Docker Hub


# The following instructions are for downloading and unpacking the Repository from GitHub

1. please download the released codebase from GitHub using the following links (either .zip or .tar.gz File): 
	- https://github.com/the-exterminators/error-annihilator/archive/refs/tags/final.zip 
	- https://github.com/the-exterminators/error-annihilator/archive/refs/tags/final.tar.gz
2. Navigate to the Folder containing the downloaded archive and unpack it with the following command
```
unzip error-annihilator-final.zip
```
for the zip-File
and
```
tar -xzf error-annihilator-final.tar.gz
```
for the tar.gz-File

# The following instructions are for building a Docker-Container for application execution in the unpacked Project Folder

1. make sure Java, Maven, Node.js, Docker-Engine and Docker-Hub are installed with the required version (or higher)
```
java --version
mvn --version
npm --version
docker --version
```
2. Make sure you have navigated to the proper directory (unpacked Project Folder From previous Step '...Downloading and Unpacking the Repository...')
3. Build a Docker image 
```
docker build --build-arg offlinekey='eyJraWQiOiI1NDI3NjRlNzAwMDkwOGU2NWRjM2ZjMWRhYmY0ZTJjZDI4OTY2NzU4IiwidHlwIjoiSldUIiwiYWxnIjoiRVM1MTIifQ.eyJhbGxvd2VkUHJvZHVjdHMiOlsidmFhZGluLWNvbGxhYm9yYXRpb24tZW5naW5lIiwidmFhZGluLXRlc3RiZW5jaCIsInZhYWRpbi1kZXNpZ25lciIsInZhYWRpbi1jaGFydCIsInZhYWRpbi1ib2FyZCIsInZhYWRpbi1jb25maXJtLWRpYWxvZyIsInZhYWRpbi1jb29raWUtY29uc2VudCIsInZhYWRpbi1yaWNoLXRleHQtZWRpdG9yIiwidmFhZGluLWdyaWQtcHJvIiwidmFhZGluLW1hcCIsInZhYWRpbi1zcHJlYWRzaGVldC1mbG93IiwidmFhZGluLWNydWQiXSwic3ViIjoiMWVmYjZkNzQtZWI0OC00OTRkLWE5N2EtYWY5ZDA2OTM4NWI0IiwidmVyIjoxLCJpc3MiOiJWYWFkaW4iLCJuYW1lIjoiU3RlZmFuIEVsIEFiZGVsbGFvdWkiLCJhbGxvd2VkRmVhdHVyZXMiOlsiY2VydGlmaWNhdGlvbnMiLCJzcHJlYWRzaGVldCIsInRlc3RiZW5jaCIsImRlc2lnbmVyIiwiY2hhcnRzIiwiYm9hcmQiLCJhcHBzdGFydGVyIiwidmlkZW90cmFpbmluZyIsInByby1wcm9kdWN0cy0yMDIyMTAiXSwibWFjaGluZV9pZCI6bnVsbCwic3Vic2NyaXB0aW9uIjoiVmFhZGluIFBybyIsImJ1aWxkX3R5cGVzIjpbInByb2R1Y3Rpb24iXSwiZXhwIjoxNzE4OTI4MDAwLCJpYXQiOjE2ODc3ODgzODksImFjY291bnQiOiJNYW5hZ2VtZW50IENlbnRlciBJbm5zYnJ1Y2sifQ.AHQRyxUNG6g59lXR-upHtUjJjI1K3mhGiJQk79Sbq3Ia988pAbFEmRqwR_Xbn_xMEnPy3v7mWPl-yii8SEPcadWFAPKcP2cTOxldLknoPNubkMw70wNh7ElFbJhA7hSqTkRgVvq61fR-HLdUXOoJFj0oVqgL73XdLDXJrwe8xiJ64KNk' -t error-annihilator .
```
4. Run the Docker image as a container
```
docker run --hostname=ed4b6d87eefe --user=myuser --env=PATH=/usr/local/openjdk-17/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin --env=JAVA_HOME=/usr/local/openjdk-17 --env=LANG=C.UTF-8 --env=JAVA_VERSION=17 --env=MAVEN_HOME=/usr/share/maven --env=MAVEN_CONFIG=/root/.m2 -p 8080:8080 --runtime=runc -d error-annihilator:latest
```

# The following instructions are for building and executing the Java-Application

1. make sure Java, Maven, Node.js, Docker-Engine and Docker-Hub are installed with the required version (or higher)
```
java --version
mvn --version
npm --version
docker --version
```
2. Make sure you have navigated to the proper directory (unpacked Project Folder From previous Step '...Downloading and Unpacking the Repository...')
3. Build a Production Package via Maven by entering the following command into the command line:
```
mvn clean package -Pproduction -Dvaadin.productionMode -Dvaadin.offline -Dvaadin.offline.key=eyJraWQiOiI1NDI3NjRlNzAwMDkwOGU2NWRjM2ZjMWRhYmY0ZTJjZDI4OTY2NzU4IiwidHlwIjoiSldUIiwiYWxnIjoiRVM1MTIifQ.eyJhbGxvd2VkUHJvZHVjdHMiOlsidmFhZGluLWNvbGxhYm9yYXRpb24tZW5naW5lIiwidmFhZGluLXRlc3RiZW5jaCIsInZhYWRpbi1kZXNpZ25lciIsInZhYWRpbi1jaGFydCIsInZhYWRpbi1ib2FyZCIsInZhYWRpbi1jb25maXJtLWRpYWxvZyIsInZhYWRpbi1jb29raWUtY29uc2VudCIsInZhYWRpbi1yaWNoLXRleHQtZWRpdG9yIiwidmFhZGluLWdyaWQtcHJvIiwidmFhZGluLW1hcCIsInZhYWRpbi1zcHJlYWRzaGVldC1mbG93IiwidmFhZGluLWNydWQiXSwic3ViIjoiMWVmYjZkNzQtZWI0OC00OTRkLWE5N2EtYWY5ZDA2OTM4NWI0IiwidmVyIjoxLCJpc3MiOiJWYWFkaW4iLCJuYW1lIjoiU3RlZmFuIEVsIEFiZGVsbGFvdWkiLCJhbGxvd2VkRmVhdHVyZXMiOlsiY2VydGlmaWNhdGlvbnMiLCJzcHJlYWRzaGVldCIsInRlc3RiZW5jaCIsImRlc2lnbmVyIiwiY2hhcnRzIiwiYm9hcmQiLCJhcHBzdGFydGVyIiwidmlkZW90cmFpbmluZyIsInByby1wcm9kdWN0cy0yMDIyMTAiXSwibWFjaGluZV9pZCI6bnVsbCwic3Vic2NyaXB0aW9uIjoiVmFhZGluIFBybyIsImJ1aWxkX3R5cGVzIjpbInByb2R1Y3Rpb24iXSwiZXhwIjoxNzE4OTI4MDAwLCJpYXQiOjE2ODc3ODgzODksImFjY291bnQiOiJNYW5hZ2VtZW50IENlbnRlciBJbm5zYnJ1Y2sifQ.AHQRyxUNG6g59lXR-upHtUjJjI1K3mhGiJQk79Sbq3Ia988pAbFEmRqwR_Xbn_xMEnPy3v7mWPl-yii8SEPcadWFAPKcP2cTOxldLknoPNubkMw70wNh7ElFbJhA7hSqTkRgVvq61fR-HLdUXOoJFj0oVqgL73XdLDXJrwe8xiJ64KNk
```
4. Navigate to the target folder
```
cd target
```
5. Execute the JAR File with the following command:
```
java -jar error-annihilator-1.0-SNAPSHOT.jar
```

# User Credentials for Login to Application
To begin using the application please use the Administrator-Credentials below:
- Username: demo
- Password: password

