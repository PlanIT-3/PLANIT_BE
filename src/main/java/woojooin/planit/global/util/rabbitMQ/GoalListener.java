package woojooin.planit.global.util.rabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.firebase.FirebaseApp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoalListener {

	private final FirebaseApp firebaseApp;
	@Value("${rabbit-mq.queue.goal=goal-notification}")
	private String goalNotificationQueue;

	@RabbitListener(queues = "${rabbit-mq.queue.goal:goal-notification}")
	public void receive(GoalMsg goalMsg) {
		log.info("listener = {}", goalMsg);

		//Todo: fcm 연결
	}

}
