package uk.gov.ons.fsdr.adeccomock.configuration;

import java.util.Arrays;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class QueueConfig {

  public static final String EVENTS_QUEUE = "mock.events";
  public static final String EVENTS_TOPIC_QUEUE = "mock.events.topic";
  public static final String FFA_EVENTS_EXCHANGE = "FFA.Events.Exchange";

  @Bean
  public AmqpAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  public Queue eventQueue() {
    return QueueBuilder.durable(EVENTS_QUEUE).build();
  }

  @Bean
  public Queue eventTopicQueue() {
    return QueueBuilder.durable(EVENTS_TOPIC_QUEUE).build();
  }

  @Bean
  public Binding eventBinding(@Qualifier("eventQueue") Queue queue, FanoutExchange fanoutExchange) {
    return BindingBuilder.bind(queue).to(fanoutExchange);
  }

  @Bean("mockEventsTopicExchange")
  public TopicExchange mockEventsTopicExchange() {
	  TopicExchange topicExchange = new TopicExchange(FFA_EVENTS_EXCHANGE);
    return topicExchange;
  }
  
  @Bean
  public Declarables mockEventTopicBinding(@Qualifier("eventTopicQueue") Queue mockEventTopicQueue, @Qualifier("mockEventsTopicExchange") TopicExchange mockEventsTopicExchange) {
    return new Declarables(mockEventTopicQueue, mockEventsTopicExchange,
    		BindingBuilder.bind(mockEventTopicQueue).to(mockEventsTopicExchange).with("ACTION.RESPONSE.PRODUCER.XMA"),
    		BindingBuilder.bind(mockEventTopicQueue).to(mockEventsTopicExchange).with("ACTION.RESPONSE.PRODUCER.SERVICE_NOW"),
    		BindingBuilder.bind(mockEventTopicQueue).to(mockEventsTopicExchange).with("SERVICE.GSUITE")
			);
  }
  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return new Jackson2JsonMessageConverter(objectMapper);
  }
}
