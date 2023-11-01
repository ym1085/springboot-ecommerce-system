FROM openjdk:11-jdk

ENV APP_HOME=/apps/shoppingmall/lib/

WORKDIR $APP_HOME

COPY build/libs/*.jar shopping-mall.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-Xms1g", "-Xmx1g", "-jar", "shopping-mall.jar"]