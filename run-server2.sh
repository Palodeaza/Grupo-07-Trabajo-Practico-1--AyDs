#!/bin/bash

echo "Ejecutando pruebaServer2.java..."
echo

RUN_CP="bin:src/main/java/libs/AbsoluteLayout-RELEASE250.jar"
java -cp "$RUN_CP" server.pruebaServer2

echo
read -p "Presioná Enter para salir..."

