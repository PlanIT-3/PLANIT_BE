package woojooin.planit.global.util.codef.dto.res;

import lombok.Getter;

@Getter
public class CodefResponse<T> {
	private CodefResult result;
	private T data;

	@Getter
	public static class CodefResult {
		private String code;
		private String extraMessage;
		private String message;
		private String transactionId;
	}

}
