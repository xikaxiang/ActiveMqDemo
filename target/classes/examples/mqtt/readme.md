## Overview

This is an example of how use the MQTT protocol with ActiveMQ.  

## Prereqs

- Install Java SDK
- Install [Maven](http://maven.apache.org/download.html) 

## Building

Run:

    mvn install

## Running the Examples

In one terminal window run:

    java -cp target/mqtt-example-0.1-SNAPSHOT.jar example.Listener

In another terminal window run:

    java -cp target/mqtt-example-0.1-SNAPSHOT.jar example.Publisher

You can control to which stomp server the examples try to connect to by
setting the following environment variables: 

* `MQTT_HOST`
* `MQTT_PORT`
* `MQTT_USER`
* `MQTT_PASSWORD`

<!-- DOS protection, limit concurrent connections to 1000 and frame size to 100MB -->
<transportConnector name="openwire" uri="tcp://0.0.0.0:61616?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
<transportConnector name="amqp" uri="amqp://0.0.0.0:5672?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
<transportConnector name="stomp" uri="stomp://0.0.0.0:61613?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
<transportConnector name="mqtt" uri="mqtt://0.0.0.0:1883?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
<transportConnector name="ws" uri="ws://0.0.0.0:61614?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
