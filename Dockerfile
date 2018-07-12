FROM openjdk:8-jdk

ENV TZ=Asia/Shanghai

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY auth-0.0.1-SNAPSHOT.jar /home/auth.jar

EXPOSE 7788

CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/home/auth.jar","--spring.profiles.active=dev"]