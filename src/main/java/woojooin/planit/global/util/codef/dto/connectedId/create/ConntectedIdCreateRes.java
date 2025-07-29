package woojooin.planit.global.util.codef.dto.connectedId.create;

import java.util.List;

import lombok.Data;

@Data
public class ConntectedIdCreateRes {

	private List<SuccessItem> successList;
	private List<Object> errorList;
	private String connectedId;

	@Data
	public static class SuccessItem {
		private String clientType;
		private String code;
		private String loginType;
		private String countryCode;
		private String organization;
		private String extraMessage;
		private String businessType;
		private String message;
		private String transactionId;
	}
}
