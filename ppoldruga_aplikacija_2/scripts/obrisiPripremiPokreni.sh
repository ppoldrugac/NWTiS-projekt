#!/bin/bash
echo "DOCKER STOP:"
docker stop ppoldruga_payara_micro
echo "DOCKER REMOVE:"
docker rm ppoldruga_payara_micro
echo "DOCKER PRIPREMI:"
./scripts/pripremiSliku.sh
echo "DOCKER POKRENI:"
./scripts/pokreniSliku.sh