FROM openjdk:11-jdk-alpine

ENV APP_HOME=/apps/shoppingmall/lib/

WORKDIR $APP_HOME

COPY build/libs/*.jar shopping-mall.jar

VOLUME ["/data"]

EXPOSE 10091
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-Xms1g", "-Xmx1g", "-jar", "shopping-mall.jar"]