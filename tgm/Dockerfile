
FROM bellsoft/liberica-openjdk-alpine:17.0.6
EXPOSE 5672
RUN mkdir /tgm
COPY tgm/build/libs/BotApp-0.1.jar /tgm/BotApp-0.1.jar
ENTRYPOINT ["java", "-jar","/tgm/BotApp-0.1.jar"]
