FROM debian
RUN apt update && apt -y install gnupg && apt -y install wget && apt -y install lsb-release && apt -y install maven
RUN apt -y install curl && apt -y install procps
RUN mkdir tomcat
RUN cd /tomcat && wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.73/bin/apache-tomcat-9.0.73.tar.gz
RUN cd /tomcat && tar -zxvf apache-tomcat-9.0.73.tar.gz
COPY server.xml /tomcat/apache-tomcat-9.0.73/conf/server.xml
COPY tomcat-users.xml /tomcat/apache-tomcat-9.0.73/conf/tomcat-users.xml
COPY fullchain.pem /tomcat/apache-tomcat-9.0.73/
COPY privkey.pem /tomcat/apache-tomcat-9.0.73/
RUN chmod +x /tomcat/apache-tomcat-9.0.73/bin/catalina.sh
ENTRYPOINT tomcat/apache-tomcat-9.0.73/bin/catalina.sh run  