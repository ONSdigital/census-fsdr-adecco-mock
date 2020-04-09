package uk.gov.ons.fsdr.adeccomock.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

  public static final String EVENTS_QUEUE = "mock.events";

  @Bean
  public AmqpAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  public Queue eventQueue() {
    return QueueBuilder.durable(EVENTS_QUEUE).build();
  }

  @Bean
  public Binding eventBinding(Queue queue, FanoutExchange fanoutExchange) {
    return BindingBuilder.bind(queue).to(fanoutExchange);
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return new Jackson2JsonMessageConverter(objectMapper);
  }
}
