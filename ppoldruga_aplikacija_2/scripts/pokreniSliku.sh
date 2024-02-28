#!/bin/bash
NETWORK=ppoldruga_mreza_1

docker run -it -d \
  -p 8070:8080 \
  --network=$NETWORK \
  --ip 200.20.0.4 \
  --name=ppoldruga_payara_micro \
  --hostname=ppoldruga_payara_micro \
  ppoldruga_payara_micro:6.2023.4 \
  --deploy /opt/payara/deployments/ppoldruga_aplikacija_2-1.0.0.war \
  --contextroot ppoldruga_aplikacija_2 \
  --noCluster &

wait
