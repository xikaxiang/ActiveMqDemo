/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package examples.openwire.advanced.scenarios.compositeDest;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author <a href="http://www.christianposta.com/blog">Christian Posta</a>
 */
public class ConsumerT {

	private static final String BROKER_URL = "tcp://localhost:61616";
	private static final Boolean NON_TRANSACTED = false;
	public static final long TIMEOUT = 20000;

	public static void main(String[] args) {
		String url = BROKER_URL;
		if (args.length > 0) {
			url = args[0].trim();
		}
		System.out
				.println("\nWaiting to receive messages... will timeout after "
						+ TIMEOUT / 1000 + "s");
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"admin", "password", url);
		Connection connection = null;
		Connection connection1 = null;
		Connection connection2 = null;
		Connection connection3 = null;

		Session session = null;

		try {

			connection = connectionFactory.createConnection();
			connection.start();

			connection1 = connectionFactory.createConnection();
			connection1.start();
			
			connection2 = connectionFactory.createConnection();
			connection2.start();	
			connection3 = connectionFactory.createConnection();
			connection3.start();
			
			//session = connection.createSession(NON_TRANSACTED,
			//		Session.AUTO_ACKNOWLEDGE);

			ConsumerThread c1 = new ConsumerThread(connection, "test-queue");
			ConsumerThread c2 = new ConsumerThread(connection1, "test-queue-foo");
			ConsumerThread c3 = new ConsumerThread(connection2, "test-queue-bar");
			ConsumerThread c4 =new ConsumerThread(connection3, "test-topic-foo");
			c1.start();
			c2.start();
			c3.start();
			c4.start();

		} catch (JMSException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		/*
		 * 
		 * if (session != null) { try { connection.close(); } catch
		 * (JMSException e5) {
		 * System.out.println("Could not close an open session..."); } }
		 * 
		 * if (connection != null) { try { connection.close(); } catch
		 * (JMSException e5) {
		 * System.out.println("Could not close an open connection..."); } }
		 */
	}

}
