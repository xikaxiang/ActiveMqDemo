package examples.openwire.ecommerce;
/*

 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TemporaryQueue;

/**
 * The Retailer orders computers from the Vendor by sending a message via
 * the VendorOrderQueue. It then syncronously receives the reponse message
 * and reports if the order was successful or not.
 */
/**
 * TemporaryQueue和TemporaryTopic，从字面上就可以看出它们是“临时”的目的地。可以通过Session来创建，例如：
 * TemporaryQueue replyQueue = session.createTemporaryQueue();
 * 虽然它们是由Session来创建的，但是它们的生命周期确实整个Connection。如果在一个Connection上创建了两个Session，
 * 则一个Session创建的TemporaryQueue或TemporaryTopic也可以被另一个Session访问。
 * 那如果这两个Session是由不同的Connection创建，则一个Session创建的TemporaryQueue不可以被另一个Session访问。
 * 另外，它们的主要作用就是用来指定回复目的地， 即作为JMSReplyTo。
 * @author 
 *
 */
public class Retailer implements Runnable {
	private String url;
	private String user;
	private String password;
	
	public Retailer(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	public void run() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
		try {
			Connection connection = connectionFactory.createConnection();
			
			// The Retailer's session is non-trasacted. //非事物的
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination vendorOrderQueue = session.createQueue("VendorOrderQueue");
			TemporaryQueue retailerConfirmQueue = session.createTemporaryQueue();
			
			MessageProducer producer = session.createProducer(vendorOrderQueue);
			MessageConsumer replyConsumer = session.createConsumer(retailerConfirmQueue);

			connection.start();

			for (int i = 0; i < 5; i++) {
				MapMessage message = session.createMapMessage();
				message.setString("Item", "Computer(s)");
				int quantity = (int)(Math.random() * 4) + 1;
				message.setInt("Quantity", quantity);
				//指定这个回复到retailerConfirmQueue
				message.setJMSReplyTo(retailerConfirmQueue);
				producer.send(message);
				System.out.println("Retailer: Ordered " + quantity + " computers.");
				
				MapMessage reply = (MapMessage) replyConsumer.receive();
				if (reply.getBoolean("OrderAccepted")) {
					System.out.println("Retailer: Order Filled");
				} else {
					System.out.println("Retailer: Order Not Filled");
				}
			}
			
			// Send a non-MapMessage to signal the end
			producer.send(session.createMessage());
			
			replyConsumer.close();
			connection.close();
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String url = "tcp://localhost:61616";
		String user = "admin";
		String password = "password";
		
		if (args.length >= 1) {
			url = args[0];
		}
		
		if (args.length >= 2) {
			user = args[1];
		}

		if (args.length >= 3) {
			password = args[2];
		}
		
		Retailer r = new Retailer(url, user, password);
		
		new Thread(r, "Retailer").start();
	}
}