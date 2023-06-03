#!/bin/bash
PORTS=(9993 9983 9973)
IMAGE=biuqu/bq-demo:1.

sh build.sh "${PORTS[@]}" "$IMAGE"