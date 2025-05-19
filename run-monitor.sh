#!/bin/bash

echo "Ejecutando monitor..."
echo

RUN_CP="bin:src/main/java/libs/AbsoluteLayout-RELEASE250.jar"
java -cp "$RUN_CP" monitor.pruebaMonitor

echo
read -p "Presioná Enter para salir..."


