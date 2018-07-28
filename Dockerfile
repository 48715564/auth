FROM openjdk:8

ENV MAVEN_HOME /opt/maven

ENV PATH $PATH:$MAVEN_HOME/bin

RUN cd /tmp \
	&& wget -nv http://it.apache.contactlab.it/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz \
	&& tar xzvf apache-maven-3.3.9-bin.tar.gz \
	&& mv /tmp/apache-maven-3.3.9 $MAVEN_HOME \
	&& rm /tmp/apache-maven-3.3.9-bin.tar.gz

VOLUME /root/.m2

ENV TZ=Asia/Shanghai

ENV PINPOINT_VERSION=1.6.2

ADD pom.xml /tmp/build/

ADD src /tmp/build/src

ADD lib /tmp/build/lib

ADD configure.sh /usr/local/bin/

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir /opt/settings && echo "env=DEV" > /opt/settings/server.properties

RUN cd /tmp/build && mvn clean package \
    && mv target/*.jar /app.jar \
    && cd / && rm -rf /tmp/build \
    && chmod a+x /usr/local/bin/configure.sh \
    && mkdir -p /assets/pinpoint-agent \
    && curl -SL https://raw.githubusercontent.com/naver/pinpoint/$PINPOINT_VERSION/agent/src/main/resources-release/pinpoint.config -o /assets/pinpoint.config \
    && curl -SL https://github.com/naver/pinpoint/releases/download/$PINPOINT_VERSION/pinpoint-agent-$PINPOINT_VERSION.tar.gz -o pinpoint-agent-$PINPOINT_VERSION.tar.gz \
    && gunzip pinpoint-agent-$PINPOINT_VERSION.tar.gz \
    && tar -xf pinpoint-agent-$PINPOINT_VERSION.tar -C /assets/pinpoint-agent \
    && curl -SL https://raw.githubusercontent.com/naver/pinpoint/$PINPOINT_VERSION/agent/src/main/resources-release/lib/log4j.xml -o /assets/pinpoint-agent/lib/log4j.xml \
    && sed -i 's/DEBUG/INFO/' /assets/pinpoint-agent/lib/log4j.xml \
    && rm pinpoint-agent-$PINPOINT_VERSION.tar

EXPOSE 7788

CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=dev"]