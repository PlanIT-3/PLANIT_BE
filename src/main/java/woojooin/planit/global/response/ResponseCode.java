package woojooin.planit.global.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseCode {

	// General
	SUCCESS("GEN-000", "Success!", HttpStatus.OK),
	NOT_FOUND("GEN-001", "NOT FOUND!", HttpStatus.NOT_FOUND);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;

	ResponseCode(String code, String message, HttpStatus httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}
}
