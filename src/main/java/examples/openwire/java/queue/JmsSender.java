package examples.openwire.java.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import examples.utils.EnvHelper;

public class JmsSender {

	public static void main(String[] args) throws JMSException {

		String user = EnvHelper.env("ACTIVEMQ_USER", "admin");
		String password = EnvHelper.env("ACTIVEMQ_PASSWORD", "password");
		String host = EnvHelper.env("ACTIVEMQ_HOST", "localhost");
		int port = Integer.parseInt(EnvHelper.env("ACTIVEMQ_PORT", "61616"));
		// String destination = EnvHelper.arg(args, 0, "event");
		String brokerUrl = "tcp://" + host + ":" + port;
		ConnectionFactory connFactory = new ActiveMQConnectionFactory(user,
				password, brokerUrl);
		// JMS 客户端到JMS Provider 的连接
		Connection conn = connFactory.createConnection();
		conn.start();

		Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
		// Destination ：消息的目的地;消息发送给谁.
		// 获取session注意参数值my-queue是Query的名字
		Destination destination = session.createQueue("my-queue");

		// MessageProducer：消息生产者
		MessageProducer producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		String body = "Hello ,Active MQ Queue";
		
		for (int i = 1; i <= 5; i++) {
			TextMessage msg = session.createTextMessage(body);
			msg.setIntProperty("id", i);
			msg.setStringProperty("id","Message Count "+ i);
			// 发送消息
			producer.send(msg);
			System.out.println(String.format("Sent %d messages", i));
		}

		producer.send(session.createTextMessage("exit")); // 发送结束标识
		producer.close();
		session.commit();
		session.close();
		conn.close();

	}

}
