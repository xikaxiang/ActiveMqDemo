## Overview

This is an example of how to use the ActiveMQ 5.x / OpenWire protocol to communicate with ActiveMQ

This example demos using exclusive consumers (one single consumer)

## Prereqs

- Install Java SDK
- Install [Maven](http://maven.apache.org/download.html) 

## Building

Run:

    mvn install

## Running the Examples

You will want to run **multiple** instances of a consumer. To run the consumer open a
terminal and type

    mvn -Pconsumer

In another terminal window run:

    mvn -Pproducer

You'll notice that even though you have multiple consumers, only one consumer will be consuming messages. If you
kill the consumer that's currently receiving messages, one of the other consumers will pick up the consumption.



ActiveMQ 的独占消费（Exclusive Consumer）
我们经常希望维持队列中的消息，按一定次序转发给消息者。然而当有多个JMS Session和消息消费者实例的从同一个队列中获取消息的时候，就不能保证消息顺序处理。因为消息被多个不同线程并发处理着。

在ActiveMQ4.x中可以采用Exclusive Consumer或者Exclusive Queues，避免这种情况，Broker会从消息队列中，一次发送消息给一个消息消费者来保证顺序。

配置如下：

        queue = new ActiveMQQueue("TEST.QUEUE?consumer.exclusive=true");

consumer = session.createConsumer(queue);
规则如下：

A．当在接收信息的时候有一个或者多个备份接收消息者和一个独占消息者的同时接收时候，无论两者创建先后，在接收的时候，均为独占消息者接收。

B．当在接收信息的时候，有多个独占消费者的时候，只有一个独占消费者可以接收到消息。

C． 当有多个备份消息者和多个独占消费者的时候，当所有的独占消费者均close的时候，只有一个备份消费者接到到消息。
备注：备份消费者为不带任何参数的消费者
测试时，至少有一个独占和非独占消费者