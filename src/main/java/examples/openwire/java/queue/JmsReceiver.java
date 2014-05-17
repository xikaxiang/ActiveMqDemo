package examples.openwire.java.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import examples.utils.EnvHelper;

public class JmsReceiver {

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

		// MessageProducer：消息 消费者
		MessageConsumer consumer = session.createConsumer(destination);

		System.out.println("Waiting for messages...");
		while (true) {
			Message msg = consumer.receive(1000); // 设置超时
			if (msg instanceof TextMessage) {
				TextMessage textMsg = (TextMessage) msg;
				;
				System.out.println("接受的消息信息：" + msg.getStringProperty("id")
						+ "  ,text：" + textMsg.getText());
				if ("Exit".equalsIgnoreCase(textMsg.getText().trim())) {
					break;
				}
			}

		}
		session.close();
		conn.close();

	}

}
