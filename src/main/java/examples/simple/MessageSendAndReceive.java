package examples.simple;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

/**
 * 消息的消费者接收消息可以采用两种方式：
 * 
 * 1、consumer.receive() 或 consumer.receive(int timeout)； 2、注册一个MessageListener。
 * 
 * 
 * 
 */

public class MessageSendAndReceive {

	public static void main(String[] args) throws JMSException {
		ConnectionFactory factory = new ActiveMQConnectionFactory(
				"vm://localhost");
		Connection connection = factory.createConnection();
		connection.start();
		Queue queue = new ActiveMQQueue("testQueue");
		final Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		Message message = session.createTextMessage("Hello JMS!");
		MessageProducer producer = session.createProducer(queue);
		producer.send(message);
		System.out.println("Send Message Completed!");

		// 消费消息
		MessageConsumer comsumer = session.createConsumer(queue);

		// 消息接收 receive方式
		Message recvMessage = comsumer.receive();
		System.out.println(((TextMessage) recvMessage).getText());
		session.close();
		connection.close();
	}
}
