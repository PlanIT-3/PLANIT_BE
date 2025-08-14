package woojooin.planit.global.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.global.response.Response;
import woojooin.planit.global.response.ResponseCode;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/* ======= BusinessException  ======= */
	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<Object> handBusinessException(BusinessException ex) {

		final Response response = Response.error(ex.getResponseCode());

		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/* ======= 일반 Exception ======= */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleGenericException(Exception ex) {

		final Response response = Response.error(ResponseCode.INTERNAL_ERROR);

		return ResponseEntity.status(response.getStatus()).body(response);
	}

	/* ======= Validation / Binding 계열 ======= */

	// @Valid on @RequestBody
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<FieldViolation> errors = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(e -> new FieldViolation(e.getField(), e.getRejectedValue(), e.getDefaultMessage()))
			.collect(Collectors.toList());

		return toResponse(ResponseCode.NOT_VALID, Map.of("errors", errors));
	}

	// @Validated on method params (e.g. @RequestParam, @PathVariable 제약 위반)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
		List<Map<String, Object>> errors = ex.getConstraintViolations().stream()
			.map(this::toViolationMap)
			.collect(Collectors.toList());

		log.info(errors.toString());
		return toResponse(ResponseCode.NOT_VALID, Map.of("errors", errors));
	}

	private Map<String, Object> toViolationMap(ConstraintViolation<?> v) {
		return Map.of(
			"property", v.getPropertyPath() == null ? null : v.getPropertyPath().toString(),
			"invalidValue", v.getInvalidValue(),
			"message", v.getMessage()
		);
	}

	private ResponseEntity<Object> toResponse(ResponseCode code, @Nullable Object data) {
		return ResponseEntity
			.status(code.getHttpStatus())
			.body(Response.build(data, code));
	}

	@Getter
	static class FieldViolation {
		private final String field;
		private final Object rejectedValue;
		private final String message;

		public FieldViolation(String field, Object rejectedValue, String message) {
			this.field = field;
			this.rejectedValue = rejectedValue;
			this.message = message;
		}
	}
}
