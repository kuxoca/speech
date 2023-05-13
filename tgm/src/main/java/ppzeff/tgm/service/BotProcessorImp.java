package ppzeff.tgm.service;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import ppzeff.shared.Constants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class BotProcessorImp implements AutoCloseable, BotProcessor {

    private final Connection connection;
    private final Channel channel;

    public BotProcessorImp() throws TimeoutException, IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constants.SERVER_ADDRESS_Rabbitmq);

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(Constants.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        } catch (IOException e) {
            log.error("connection ERROR ", e);
            throw new IOException();
        } catch (TimeoutException e) {
            log.error("connection ERROR ", e);
            throw new TimeoutException();

        }
    }

    @Override
    public String sendAndGetText(byte[] bytes, String typeService) {
        String response;
        try {
            response = call(bytes, typeService);
            log.info(" [o] " + typeService.toUpperCase() + " '" + response + "'");
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    private String call(byte[] bytes, String routingKey) throws IOException, InterruptedException, ExecutionException {
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish(Constants.EXCHANGE_NAME, routingKey, props, bytes);

        final CompletableFuture<String> response = new CompletableFuture<>();

        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.complete(new String(delivery.getBody(), StandardCharsets.UTF_8));
            }
        }, consumerTag -> {
        });

        String result = response.get();
        channel.basicCancel(ctag);
        return result;
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }
}