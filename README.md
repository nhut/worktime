# Worktime
PHZ intra client

Access web application with your browser: https://127.0.0.1

### Run the application from console (for development)
``
$ mvn clean install\
$ cd web\
$ mvn spring-boot:run
``

### Docker
Run application using image from DockerHub:
``
$ docker run -it --rm -p 443:443 donitsi/worktime
``

#### Run application in docker container (Production version)
``
$ docker-compose up
``

#### Run application in docker container (Development version)
``
$ docker-compose -f docker-compose-dev.yml up
``

#### Clean up
``
$ docker-compose down
``

### DockerHub
``
$ docker login
``

#### Build image (1.0.0 is tag version number)
``
$ docker build --no-cache -t donitsi/worktime:1.0.0 .
``

#### Push image (1.0.0 is tag version number)
```
$ docker push donitsi/worktime:1.0.0
$ docker image tag donitsi/worktime:1.0.0 donitsi/worktime:latest
$ docker push donitsi/worktime:latest
```
