package dev.luisjohann.ofxmsimport.queue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.luisjohann.ofxmsimport.records.SseImportedMessageDTO;

@Component
public class QueueSenderImportedSuccess {

   @Autowired
   private RabbitTemplate rabbitTemplate;

   @Value("${queue.routing-key.imported}")
   private String routingKeySse;

   @Value("${exchange.name}")
   private String exchangeName;

   public void send(SseImportedMessageDTO message) {
      rabbitTemplate.convertAndSend(exchangeName, routingKeySse, message);
   }
}
