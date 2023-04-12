#!/bin/sh
mkdir ~/projects
mkdir ~/projects/spring
sudo apt-get -y install
sudo apt -y upgrade
sudo apt-get install openjdk-11-jdk
sudo apt -y install maven
sudo apt-get install apt-transport-https ca-certificates curl gnupg2 software-properties-common
curl -fsSL https://download.docker.com/linux/debian/gpg | sudo apt-key add
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian buster stable"
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io
sudo apt install docker-compose
sudo apt install coturn
sudo apt install git
cd ~/projects/spring && git clone https://github.com/SammithKanur/chat-app.git
cd ~/
scp tomcat/apache-tomcat-9.0.46.tar.gz drivers.tar.gz tmp.tar.gz sammith@34.93.195.251:~/
sudo docker run --name mongo -p 8085:27017 -v ~/tmp/mongo:/data/db mongo
sudo docker run --name elasticsearch -v ~/tmp/elasticsearch:/data -p 9201:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.8.16
chatAppEnv="dev"
tomcatUser="admin"
tomcatPassword="tomcatSam"
