package woojooin.planit.domain.member.domain.enums;

public enum InvestType {
	/**
	 * 🟦 안전형 – 원금 보존 최우선, 투자 경험 거의 없음
	 */
	SAFE_PRESERVATION("🟦", "안전형", "원금 보존 최우선, 투자 경험 거의 없음"),

	/**
	 * 🟩 안정추구형 – 예·적금 위주이되 소폭의 수익 추구
	 */
	STABILITY_SEEKER("🟩", "안정추구형", "예·적금 위주이되 소폭의 수익 추구"),

	/**
	 * 🟨 위험중립형 – 손익 균형형, 일정 수준의 리스크 허용
	 */
	RISK_NEUTRAL("🟨", "위험중립형", "손익 균형형, 일정 수준의 리스크 허용"),

	/**
	 * 🟥 적극투자형 – 수익 기대치 높고, 리스크 수용 가능
	 */
	AGGRESSIVE("🟥", "적극투자형", "수익 기대치 높고, 리스크 수용 가능"),

	/**
	 * 🟪 공격투자형 – 고수익 추구, 높은 리스크 감내 가능
	 */
	OFFENSIVE("🟪", "공격투자형", "고수익 추구, 높은 리스크 감내 가능");

	private final String emoji;
	private final String label;
	private final String description;

	InvestType(String emoji, String label, String description) {
		this.emoji = emoji;
		this.label = label;
		this.description = description;
	}

	public String getEmoji() {
		return emoji;
	}

	public String getLabel() {
		return label;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return emoji + " " + label + " – " + description;
	}
}
