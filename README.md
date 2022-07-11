# SimpleJobsMatchingEngine
This is simple matching engine, which matches workers with their jobs

# This is maven project. Before running the project make sure to use command
mvn clean install

# There are no test cases added for this project
# This Project will call the swipejobs test api's first to get the details of all the workers and the jobs.
Url for both are provided in the application.properties file
Workers Url : https://test.swipejobs.com/api/workers
Jobs Url : https://test.swipejobs.com/api/jobs

# Application is running at http://localhost:8080/jobs/{workerId}


