package woojooin.planit.domain.goal.domain;

public enum Bank {
	INDUSTRIAL("002", "한국산업은행"),
	IBK("003", "IBK기업은행"),
	KOOKMIN("004", "KB국민은행"),
	SUHYUP("007", "수협은행"),
	NONGHYEOP("011", "농협은행"),
	WOORI("020", "우리은행"),
	SC("023", "SC제일은행"),
	CITI("027", "한국씨티은행"),
	DAEGU("031", "대구은행"),
	BUSAN("032", "부산은행"),
	GWANGJU("034", "광주은행"),
	JEJU("035", "제주은행"),
	JEONBUK("037", "전북은행"),
	GYEONGNAM("039", "경남은행"),
	SAEMAUL("045", "새마을금고"),
	SHINHYUP("048", "신협"),
	POST("071", "우체국"),
	HANA("081", "하나은행"),
	SHINHAN("088", "신한은행"),
	KBANK("089", "케이뱅크");

	private final String code;        // 은행 코드
	private final String displayName; // 은행 이름

	Bank(String code, String displayName) {
		this.code = code;
		this.displayName = displayName;
	}

	public String getCode() {
		return code;
	}

	public String getDisplayName() {
		return displayName;
	}

	// 코드로 enum 찾기
	public static Bank fromCode(String code) {
		for (Bank b : values()) {
			if (b.code.equals(code)) {
				return b;
			}
		}
		return null; // 못 찾으면 null
	}

	// 코드로 은행 이름 바로 반환
	public static String getNameByCode(String code) {
		Bank bank = fromCode(code);
		return bank != null ? bank.getDisplayName() : null;
	}

	public static void main(String[] args) {
		System.out.println(Bank.getNameByCode("004")); // 국민은행
		System.out.println(Bank.getNameByCode("088")); // 신한은행
		System.out.println(Bank.fromCode("071").getDisplayName()); // 우체국
	}
}

