package woojooin.planit.global.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseCode {

	// General
	SUCCESS("GEN-000", "Success!", HttpStatus.OK),
	NOT_FOUND("GEN-001", "NOT FOUND!", HttpStatus.NOT_FOUND),

	// 4xx
	BAD_REQUEST("GEN-003", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
	NOT_VALID("GEN-004", "유효성 검사에 실패했습니다.", HttpStatus.BAD_REQUEST),
	NO_HANDLER("GEN-005", "요청한 경로를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	METHOD_NOT_ALLOWED("GEN-006", "허용되지 않은 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),
	UNSUPPORTED_MEDIA_TYPE("GEN-007", "지원하지 않는 미디어 타입입니다.", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
	NOT_ACCEPTABLE("GEN-008", "요청한 Accept 헤더로 응답을 생성할 수 없습니다.", HttpStatus.NOT_ACCEPTABLE),
	MISSING_PARAMETER("GEN-009", "필수 요청 파라미터가 누락되었습니다.", HttpStatus.BAD_REQUEST),
	TYPE_MISMATCH("GEN-010", "요청 파라미터 타입이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
	MESSAGE_NOT_READABLE("GEN-011", "요청 본문을 읽을 수 없습니다.", HttpStatus.BAD_REQUEST), // 빈 본문/JSON 파싱
	MISSING_PATH_VARIABLE("GEN-012", "경로 변수 정보가 누락되었습니다.", HttpStatus.BAD_REQUEST),
	BIND_ERROR("GEN-013", "요청 바인딩에 실패했습니다.", HttpStatus.BAD_REQUEST),
	ACCESS_DENIED("GEN-014", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
	UNAUTHORIZED("GEN-015", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),

	// Authentication and Authorization
	INSUFFICIENT_PRIVILEGES("AUTH-001", "권한이 부족합니다.", HttpStatus.FORBIDDEN),
	ROLE_MISMATCH("AUTH-002", "요청한 역할과 현재 역할이 일치하지 않습니다.", HttpStatus.FORBIDDEN),
	DUPLICATE_EMAIL("AUTH-003", "이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST),
	INVALID_LOGIN("AUTH-004", "유효하지 않은 이메일 또는 비밀번호입니다.", HttpStatus.UNAUTHORIZED),
	REISSUE_FAILED("AUTH-005", "리프레시 토큰 재발급에 실패했습니다.", HttpStatus.UNAUTHORIZED),

	// ISA Account Domain Errors
	ISA_MEMBER_NOT_FOUND("ISA-001", "해당 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	ISA_PRODUCT_NOT_FOUND("ISA-002", "해당 상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	ISA_OBJECT_NOT_FOUND("ISA-003", "해당 목표를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	ISA_EMPTY_PRODUCT_LIST("ISA-004", "등록할 상품 목록이 비어있습니다.", HttpStatus.BAD_REQUEST),
	ISA_INVALID_ALLOCATION_RATE("ISA-005", "할당 비율이 올바르지 않습니다. (0-100 사이의 값이어야 합니다)", HttpStatus.BAD_REQUEST),
	ISA_TOTAL_ALLOCATION_EXCEEDED("ISA-006", "총 할당 비율이 100%를 초과할 수 없습니다.", HttpStatus.BAD_REQUEST),
	ISA_INVALID_AMOUNT("ISA-007", "투자 금액이 올바르지 않습니다. (양수여야 합니다)", HttpStatus.BAD_REQUEST),
	ISA_ACCOUNT_NUMBER_REQUIRED("ISA-008", "계좌번호는 필수입니다.", HttpStatus.BAD_REQUEST),
	ISA_DUPLICATE_PRODUCT("ISA-009", "중복된 상품이 포함되어 있습니다.", HttpStatus.BAD_REQUEST),
	ISA_REGISTRATION_FAILED("ISA-010", "상품 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	ISA_UPDATE_FAILED("ISA-011", "상품 수정에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	ISA_DELETE_FAILED("ISA-012", "상품 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	ISA_TAX_CALCULATION_FAILED("ISA-013", "세금 절약 금액 계산에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),


	// Deposit Account Domain Errors
	DEPOSIT_MEMBER_NOT_FOUND("DEP-001", "해당 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	DEPOSIT_ACCOUNT_NOT_FOUND("DEP-002", "해당 예적금 계좌를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	DEPOSIT_OBJECT_NOT_FOUND("DEP-003", "해당 목표을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	DEPOSIT_EMPTY_ACCOUNT_LIST("DEP-004", "등록할 예적금 계좌 목록이 비어있습니다.", HttpStatus.BAD_REQUEST),
	DEPOSIT_INVALID_ALLOCATION_RATE("DEP-005", "할당 비율이 올바르지 않습니다. (0-100 사이의 값이어야 합니다)", HttpStatus.BAD_REQUEST),
	DEPOSIT_TOTAL_ALLOCATION_EXCEEDED("DEP-006", "총 할당 비율이 100%를 초과할 수 없습니다.", HttpStatus.BAD_REQUEST),
	DEPOSIT_INVALID_AMOUNT("DEP-007", "할당 금액이 올바르지 않습니다. (양수여야 합니다)", HttpStatus.BAD_REQUEST),
	DEPOSIT_ACCOUNT_NUMBER_REQUIRED("DEP-008", "계좌번호는 필수입니다.", HttpStatus.BAD_REQUEST),
	DEPOSIT_DUPLICATE_ACCOUNT("DEP-009", "중복된 예적금 계좌가 포함되어 있습니다.", HttpStatus.BAD_REQUEST),
	DEPOSIT_REGISTRATION_FAILED("DEP-010", "예적금 계좌 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	DEPOSIT_UPDATE_FAILED("DEP-011", "예적금 계좌 수정에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	DEPOSIT_DELETE_FAILED("DEP-012", "예적금 계좌 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	DEPOSIT_VALIDATION_FAILED("DEP-013", "예적금 계좌 검증에 실패했습니다.", HttpStatus.BAD_REQUEST),
	DEPOSIT_INSUFFICIENT_AMOUNT("DEP-014", "할당 가능한 잔여액이 부족합니다.", HttpStatus.BAD_REQUEST),
	DEPOSIT_ACCOUNT_HAS_ALLOCATION("DEP-015", "할당된 금액이 있는 계좌는 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),


	//goal
	GOAL_NOT_FOUND("GOAL-001", "해당 목표를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	GOAL_CREATE_FAILED("GOAL-002", "목표 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	GOAL_UPDATE_FAILED("GOAL-003", "목표 수정에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	GOAL_DELETE_FAILED("GOAL-004", "목표 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

	// Account Connection
	ACCOUNT_CONNECTION_FAILED("ACC-001", "계좌 연동에 실패했습니다.", HttpStatus.BAD_REQUEST),
	ACCOUNT_ALREADY_REGISTERED("ACC-002", "이미 계정이 등록된 기관입니다. 기존 계정을 먼저 삭제하세요.", HttpStatus.BAD_REQUEST),


  // 5xx
	INTERNAL_ERROR("GEN-999", "서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;

	ResponseCode(String code, String message, HttpStatus httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}
}
