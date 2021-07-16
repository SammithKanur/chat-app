FROM debian
RUN apt update && apt -y install gnupg && apt -y install wget && apt -y install lsb-release && apt -y install maven
RUN apt -y install curl
RUN mkdir chat-app
RUN mkdir tomcat
COPY tomcat /tomcat
RUN chmod +x /tomcat/apache-tomcat-9.0.46/bin/catalina.sh
ENTRYPOINT tomcat/apache-tomcat-9.0.46/bin/catalina.sh run