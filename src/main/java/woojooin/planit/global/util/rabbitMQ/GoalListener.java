package woojooin.planit.global.util.rabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.member.domain.Member;
import woojooin.planit.domain.member.mapper.MemberMapper;
import woojooin.planit.global.fcm.service.FCMService;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoalListener {

	private final FCMService fcmService;
	private final MemberMapper memberMapper;

	@Value("${rabbit-mq.queue.goal=goal-notification}")
	private String goalNotificationQueue;

	@RabbitListener(queues = "${rabbit-mq.queue.goal:goal-notification}")
	public void receive(GoalMsg goalMsg) {
		Member member = memberMapper.findById(goalMsg.getMemberId());
		fcmService.sendGoalAchievementNotification(member.getMemberId(), member.getFcmToken(), goalMsg.getGoalName(),
			goalMsg.getAchievementRate(), goalMsg.getTryCount());
	}

}
