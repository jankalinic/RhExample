FROM openjdk:11
ADD target/api-server-1.0-jar-with-dependencies.jar /opt/api-server-1.0-jar-with-dependencies.jar
CMD ["java", "-jar", "/opt/api-server-1.0-jar-with-dependencies.jar", "run", "redhatExample.VertxServer"] 
EXPOSE 8088
