package top.yeliusu.websocketserver.websocket.utils;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveMqUtils {

    private static ConnectionFactory factory;
    private static Connection connection;
    private static Session session;

    static Map<String, MessageProducer> sendQueues = new ConcurrentHashMap<>();
    static Map<String, MessageConsumer> getQueues = new ConcurrentHashMap<>();
    private static final String mqUrl = "tcp://39.105.143.125:61616?wireFormat.maxInactivityDuration=0";

    static {
        try {
            factory = new ActiveMQConnectionFactory(mqUrl);
            connection = factory.createConnection();
            connection.start();
            /*参数1：是否开启事务。true：开启事务，第二个参数忽略。参数2：当第一个参数为false时，才有意义。
            消息的应答模式。1、自动应答2、手动应答。一般是自动应答*/
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static MessageProducer createProducer(String queueName) {
        if (sendQueues.containsKey(queueName))
            return sendQueues.get(queueName);
        try {
            Destination destination = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(destination);
            sendQueues.put(queueName, producer);
            return producer;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return sendQueues.get(queueName);
    }

    public static MessageConsumer createConsumer(String queueName) {
        if (getQueues.containsKey(queueName))
            return getQueues.get(queueName);
        try {
            Destination destination = session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(destination);
            getQueues.put(queueName, consumer);
            return consumer;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return getQueues.get(queueName);
    }

    public static void sendMessage(String queue, String text) {
        try {
            TextMessage message = session.createTextMessage(text);
            createProducer(queue).send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static String getMessage(String queue) {
        try {
            MessageConsumer consumer = createConsumer(queue);

            consumer.setMessageListener(message -> {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    if (textMessage != null) {
                        //获取消息内容
                        String text = textMessage.getText();
                        System.out.println("消费者message = " + text);
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void close() {
        try {
            connection.close();
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String queueName = "9223372036854775807";
        sendMessage(queueName,"用户发送的消息");
        String message = getMessage(queueName);
        System.out.println(message);
    }

}
