#!/bin/bash
ps ax |grep java |grep hllserver.jar | awk '{print $1}' |xargs kill -9
