package examples.openwire.advanced.scenarios.compositeDest;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class ConsumerThread extends Thread {
	private static final long TIMEOUT = 20000;
	private static final Boolean NON_TRANSACTED = false;

	private Connection connection;

	private MessageConsumer consumer;

	public MessageConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(MessageConsumer consumer) {
		this.consumer = consumer;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	private String dest;

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public ConsumerThread(Connection connection, String dest) {
		super(dest);
		this.connection = connection;
		Session session;
		try {
			session = connection.createSession(NON_TRANSACTED,
					Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic(dest);
			consumer = session.createConsumer(destination);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		int i = 0;
		Message message = null;
		System.out.println(Thread.currentThread().getName() + "running .....");
		try {

			while (true) {
				message = this.consumer.receive(TIMEOUT);
				if (message != null) {
					if (message instanceof TextMessage) {
						String text = ((TextMessage) message).getText();
						System.out.println("Got " + i++ + "  " + this.getName()
								+ "Meeage : " + text);
					}
				} else {
					break;
				}
			}

		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			try {
				if (consumer != null) {
					consumer.close();
				}

			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

	}
}
