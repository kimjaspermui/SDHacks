#!/bin/bash

# java -cp ./JRPiCam-v1.0.1.jar:. CarDetector

while true; do

  raspistill -rot 270 -n -t 50 -o "test.jpg"

  # wait one second
  sleep 0.010

done
