FROM adoptopenjdk/openjdk11:latest
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/iptracker-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} iptracker.jar
ENTRYPOINT ["java","-jar","-Dspring.redis.host=rediscontainer","/iptracker.jar"]
