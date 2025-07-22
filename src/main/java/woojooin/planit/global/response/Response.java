package woojooin.planit.global.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Response<T> {
	private final String code;
	private final String message;
	private final T data;

	public static <T> Response<T> ok(T data) {
		ResponseCode code = ResponseCode.SUCCESS;

		return Response.<T>builder()
			.code(code.getCode())
			.message(code.getMessage())
			.data(data)
			.build();
	}

	public static <T> Response<T> build(T data, ResponseCode code) {
		return Response.<T>builder()
			.code(code.getCode())
			.message(code.getMessage())
			.data(data)
			.build();
	}

	public static Response<Void> error(ResponseCode code) {
		return Response.<Void>builder()
			.code(code.getCode())
			.message(code.getMessage())
			.build();
	}
}
