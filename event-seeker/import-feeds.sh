#!/bin/sh
curl --data "" "http://localhost:9000/api/events/concerts?url=http://localhost:9000/assets/feeds/Concerts.json"
curl --data "" "http://localhost:9000/api/events/conferences?url=http://localhost:9000/assets/feeds/Conferences.json"
curl --data "" "http://localhost:9000/api/events/sports?url=http://localhost:9000/assets/feeds/SportEvents.json"
