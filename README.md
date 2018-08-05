# Email send service

## Problem :

Email sender servoce abstracting between mail gun and send grid servers.


#How to run: 
Open as gradle project, from Gradle view window, application -> bootrun to start the service
or use the below CLIs.


Environment : Java 8

### Run local instance
`./gradlew clean bootRun`

Spring Boot will run locally on http://localhost:9002/


### Build .war file
`./gradlew clean assemble`

## Send email

Post end point : http://localhost:9002/api/send

Sample post body:

{
	"from"  : "test2@gmail.com",
	"to" : "test1@gmail.com",
		"cc" : ["test3@gmail.com"],
			"bcc" : ["test3@gmail.com"],
	"subject" : "Hello",
	"message" : "Test email"
}

Know issues: Mailgun can not send emails to random ids, the email adress need to be registed with 
the server before. 


