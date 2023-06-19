package ppzeff.recognize.server;



import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import ppzeff.recognize.service.RecognizeService;
import ppzeff.recognize.service.RecognizeServiceSber;
import ppzeff.recognize.service.RecognizeServiceYandex;
import ppzeff.shared.Constants;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RecognizeServer {

    public void start(String[] argv) throws Exception {
        RecognizeService rec = null;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constants.SERVER_ADDRESS_Rabbitmq);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(Constants.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();

        if (argv.length < 1) {
            rec = new RecognizeServiceSber();
            channel.queueBind(queueName, Constants.EXCHANGE_NAME, Constants.ROUTING_KEY_SBER);
            log.info("DEFAULT ROUTING_KEY: {}", Constants.ROUTING_KEY_SBER);
        } else {

            for (String severity : argv) {
                if (severity.equals(Constants.ROUTING_KEY_SBER) || severity.equals("1")) {
                    channel.queueBind(queueName, Constants.EXCHANGE_NAME, Constants.ROUTING_KEY_SBER);
                    rec = new RecognizeServiceSber();
                    log.info("ROUTING_KEY: {}", Constants.ROUTING_KEY_SBER);
                } else if (severity.equals(Constants.ROUTING_KEY_YANDEX) || severity.equals("2")) {
                    rec = new RecognizeServiceYandex();
                    channel.queueBind(queueName, Constants.EXCHANGE_NAME, Constants.ROUTING_KEY_YANDEX);
                    log.info("ROUTING_KEY: {}", Constants.ROUTING_KEY_YANDEX);
                }
            }
        }

        channel.basicQos(1);

        log.info(" [x] Awaiting RPC requests");

        AtomicInteger i = new AtomicInteger(1);

        RecognizeService finalRec = rec;
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            log.info("get message # {}", i.getAndIncrement());
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();
            String response = null;
            try {
                byte[] bytes = delivery.getBody();
                response = finalRec.recognize(bytes);
                log.info("rabbitMQ - {} - {}", i, response);
            } catch (RuntimeException e) {
                log.info(" [.] " + e);
            } finally {
                channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        channel.basicConsume(queueName, false, deliverCallback, (consumerTag -> {
        }));
    }

}