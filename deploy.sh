#!/bin/bash
mvn clean install
sudo cp target/chat-app-1.0-SNAPSHOT.war chat-app-server/war/chat-app.war