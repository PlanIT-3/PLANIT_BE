package woojooin.planit.global.fcm.dto;

import lombok.Data;

@Data
public class FCMTokenDto {
	private Long memberId;
	private String token;
	private String deviceType; // "web", "android", "ios"
}