# Kuehne&Nagel City List 

![alt text](https://media1.popsugar-assets.com/files/thumbor/RQj6pnVkrLbvYwIBuQ4xoptRxK8/fit-in/2048xorig/filters:format_auto-!!-:strip_icc-!!-/2020/04/23/764/n/1922441/2aa330ba31c85edc_GettyImages-946087016/i/New-York-City.jpg)

The "City List" application is an implementation of test task for Kuehne & Nagel.
It allows a user to perform several actions:
- browse through the paginated list of cities with the corresponding photos
- search the city by the name
- edit the city (both name and photo)

# System requirements:
[Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

[Git](https://git-scm.com/downloads)

[Maven](https://maven.apache.org/download.cgi)

[Docker](https://www.docker.com/products/docker-desktop/)

[Postman](https://www.postman.com/downloads/)

# Run application

- Get the code from my Git Repository
      
        git clone https://github.com/rodion2/citylist.git

- Build the application and run tests, 'Docker Maven Plugin' is used for it, so make sure that 'Docker' is up and running.
- This will also put 'city' image in your local 'Docker image storage'

        mvn clean install docker:build  

- Run docker compose to start both PostgreSQL database and the service instance.

        docker compose up


- The service is available on

        http://localhost:8080

- To turn the system off, just use 'docker compose stop' command

       docker compose stop


# Test application

 - [Postman collection](kuehne_and_nagel_postman_collection.json) - this file needs to import into postman

## Authors

* **Rodion Minkin** -  *Initial work* - [Git](https://github.com/rodion2/)


## License

TBD

## Acknowledgments

* 
