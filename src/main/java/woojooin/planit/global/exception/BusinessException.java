package woojooin.planit.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import woojooin.planit.global.response.ResponseCode;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {
	private ResponseCode responseCode;
}
