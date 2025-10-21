package woojooin.planit.global.exception;

public class NotificationException extends RuntimeException {

	public NotificationException(String message, Exception e) {
		super(message, e);
	}
}
