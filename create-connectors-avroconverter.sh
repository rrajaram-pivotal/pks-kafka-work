#!/usr/bin/env bash

echo "-----------------------"
echo "Creating connectors ..."
echo "-----------------------"


echo
curl -i -X POST http://localhost:8083/connectors \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -d @connectors/avroconverter/elasticsearch-sink-orders.json

echo
echo "--------------------------------------------------------------"
echo "Check state of connectors and their tasks by running script ./check-connectors-state.sh or at Kafka Contol Center, link http://localhost:9021"
echo "--------------------------------------------------------------"
