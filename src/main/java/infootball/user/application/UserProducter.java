package infootball.user.application;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserProducter {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingKey;

    public void sendMessage(String message) {
        System.out.println("Sending message: " + message);
        amqpTemplate.convertAndSend(exchange, routingKey, message);
    }
}
