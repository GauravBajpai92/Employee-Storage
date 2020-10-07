# Employee-Storage


Service is used to get employee data from user it validates the data and sends it to the Storage service to store it. 
This service has 3 end points for POST, PUT and GET employee data.

It sends PUT and POST Messages are send through Active MQ message queues.

It sends to Artemis ActiveMQ broker messages.

vromero/activemq-artemis Docker image is used (https://hub.docker.com/r/vromero/activemq-artemis) for Active MQ.
Note: This is a Linux image and will not run on Windows container. Please switch to Linux docker container.
Follow https://github.com/vromero/activemq-artemis-docker/blob/master/README.md for image installation.

HOW TO USE THE SERVICE:
-----------------------------------------------------------------------------------------------------------------------------

POST:
http://localhost:7779/v1/employee/?fileType=xml
 port 7779 is used and can be configured through application.properties.
 
File Type xml and csv are excepted for other values unsupported exception is thrown.
Sample post body:
{
    "empName": "Gaurav",  // String Values allowed
    "salary": 11111.11,   //Double values no more than 7 digits before decimal and 2 digits after decimal
    "age": 28             //Age Range should be between 18 to 60
}
 
On successful Post url is returned in the headers {getLocation: /v1/employee/25bc4965-47f6-4d29-8017-cd908ef70329} with Status 201
Same can be used to get the elements. Also for Put UUID returned in the get location is used as Request param

PUT:
http://localhost:7779/v1/employee/?fileType=xml&employeeId=399387ec-9bed-41cf-bbed-0d1273f2b1de is sample url
Second Request param is the UUID returned as part of Put getLocation header.
Same as Post Put body is :
Sample post body:
{
    "empName": "Gaurav",  // String Values allowed
    "salary": 11111.11,   //Double values no more than 7 digits before decimal and 2 digits after decimal
    "age": 28             //Age Range should be between 18 to 60
}
On successful Put 200 is returned.

GET:
sample Url: http://localhost:7779/v1/employee/e5c0d272-8455-4c62-b901-904a965ee3b9
same as returned during post in headers.
Sample output:
{
    "empId": "e5c0d272-8455-4c62-b901-904a965ee3b9",
    "empName": "Gaurav",
    "salary": 11111.11,
    "age": 19
}

For Security AES encryption is used, shared key between Employee Storage and Storage Service is used to encrypt and decrypt messages.
