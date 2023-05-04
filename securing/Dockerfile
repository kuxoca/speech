
FROM bellsoft/liberica-openjdk-alpine:17.0.6
EXPOSE 8190
RUN mkdir /sec
COPY securing/build/libs/SecuringServer-0.1.jar /sec/SecuringServer-0.1.jar
ENTRYPOINT ["java", "-jar","/sec/SecuringServer-0.1.jar"]
