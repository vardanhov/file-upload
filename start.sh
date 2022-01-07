#!/usr/bin/env bash

source /app/upload_file/.env
nohup java -Dspring.profiles.active=dev -jar /app/upload_file/Upload-file-app-0.0.1-SNAPSHOT.jar > /app/upload_file/log.log &
pId=$!
echo "kill $pId" > /app/upload_file/stop_app.sh
chmod +x /app/upload_file/stop_app.sh
