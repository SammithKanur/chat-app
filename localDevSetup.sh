#!/bin/bash
sudo docker run --name mysql -p 8081:3306 -v $HOME/chat-app/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=chatApp -d mysql
sudo docker run --name mongo -p 8085:27017 -v 3$HOME/chat-app/mongo:/data/db -d mongo
sudo docker run -p 9201:9200 -p 9300:9300 -e "discovery.type=single-node" --name elasticsearch -v $HOME/chat-app/elasticsearch:/usr/share/elasticsearch/data:rw -d docker.elastic.co/elasticsearch/elasticsearch:6.8.16
