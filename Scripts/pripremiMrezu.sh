#!/bin/bash
NETWORK=ppoldruga_mreza_1
docker network create --subnet=200.20.0.0/16 $NETWORK
