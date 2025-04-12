#!/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

mkdir -p ./jars

echo -e "${GREEN}Downloading project-poseidon-1.1.10.jar...${NC}"
curl -L -o ./jars/project-poseidon-1.1.10.jar https://github.com/retromcorg/Project-Poseidon/releases/download/1.1.10-250328-1731-f67a8e3/project-poseidon-1.1.10.jar

echo -e "${GREEN}Downloading luaj-jse-3.0.2.jar...${NC}"
curl -L -o ./jars/luaj-jse-3.0.2.jar https://github.com/luaj/luaj/releases/download/v3.0.2/luaj-jse-3.0.2.jar

echo -e "${GREEN}Installing project-poseidon-1.1.10.jar to local Maven repository...${NC}"
mvn install:install-file -Dfile=./jars/project-poseidon-1.1.10.jar -DgroupId=com.legacyminecraft.poseidon -DartifactId=poseidon-craftbukkit -Dversion=1.1.10 -Dpackaging=jar

echo -e "${GREEN}Installing luaj-jse-3.0.2.jar to local Maven repository...${NC}"
mvn install:install-file -Dfile=./jars/luaj-jse-3.0.2.jar -DgroupId=org.luaj -DartifactId=luaj-jse -Dversion=3.0.2 -Dpackaging=jar

echo -e "${GREEN}Running Maven clean and eclipse:eclipse...${NC}"
mvn clean eclipse:eclipse

echo -e "${GREEN}Build process complete!${NC}"
