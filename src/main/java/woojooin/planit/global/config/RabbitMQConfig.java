package woojooin.planit.global.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableRabbit
@RequiredArgsConstructor
public class RabbitMQConfig {

	@Value("${spring.rabbitmq.host}")
	private String host;

	@Value("${spring.rabbitmq.port}")
	private int port;

	@Value("${spring.rabbitmq.username}")
	private String username;

	@Value("${spring.rabbitmq.password}")
	private String password;

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);

		return connectionFactory;
	}

	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
		ConnectionFactory cf,
		Jackson2JsonMessageConverter converter) {
		SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
		f.setConnectionFactory(cf);
		f.setMessageConverter(converter);
		return f;
	}

	@Bean
	@Scope("prototype")
	public RabbitTemplate rabbitTemplate(ConnectionFactory cf,
		Jackson2JsonMessageConverter converter) {
		RabbitTemplate t = new RabbitTemplate(cf);
		t.setMessageConverter(converter);
		return t;
	}

	@Bean
	public AmqpAdmin amqpAdmin(ConnectionFactory cf) {
		return new RabbitAdmin(cf);
	}

	@Bean
	public Queue goalQueue(@Value("${rabbit-mq.queue.goal:goal-notification}") String name) {
		return new Queue(name, true);
	}

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange("direct_exchange", true, false);
	}

	@Bean
	public Binding goalBinding(Queue goalQueue, DirectExchange directExchange) {
		return BindingBuilder.bind(goalQueue).to(directExchange).with("goal-notification");
	}

}
