package woojooin.planit.domain.goal.domain.enums;

public enum Bank {

 
	INDUSTRIAL("0002", "한국산업은행"),
	IBK("0003", "IBK기업은행"),
	KOOKMIN("0004", "KB국민은행"),
	SUHYUP("0007", "수협은행"),
	NONGHYEOP("0011", "농협은행"),
	WOORI("0020", "우리은행"),
	SC("0023", "SC제일은행"),
	CITI("0027", "한국씨티은행"),
	DAEGU("0031", "대구은행"),
	BUSAN("0032", "부산은행"),
	GWANGJU("0034", "광주은행"),
	JEJU("0035", "제주은행"),
	JEONBUK("0037", "전북은행"),
	GYEONGNAM("0039", "경남은행"),
	SAEMAUL("0045", "새마을금고"),
	SHINHYUP("0048", "신협"),
	POST("0071", "우체국"),
	HANA("0081", "하나은행"),
	SHINHAN("0088", "신한은행"),
	KBANK("0089", "케이뱅크"),
	
	// 증권사
	YUANTA("0209", "유안타증권"),
	KB_SECURITIES("0218", "KB증권"),
	IBK_SECURITIES("0225", "IBK투자증권"),
	DAOL("0227", "다올투자증권"),
	MIRAE_ASSET("0238", "미래에셋증권"),
	SAMSUNG("0240", "삼성증권"),
	KOREA_INVESTMENT("0243", "한국투자증권"),
	NH_INVESTMENT("0247", "NH투자증권"),
	KYOBO("0261", "교보증권"),
	HI_INVESTMENT("0262", "하이투자증권"),
	KIWOOM("0264", "키움증권"),
	LS("0265", "LS증권"),
	SK("0266", "SK증권"),
	DAISHIN("0267", "대신증권"),
	HANWHA("0269", "한화투자증권"),
	HANA_INVESTMENT("0270", "하나금융투자"),
	SHINHAN_INVESTMENT("0278", "신한금융투자"),
	DB("0279", "DB금융투자"),
	EUGENE("0280", "유진투자증권"),
	MERITZ("0287", "메리츠종합금융증권");

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

