package woojooin.planit.domain.member.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealInvestTypeReq {
	private String investType;      // ex: "보수형", "안정형", "위험중립형", "성장형", "공격형"
	private Double stable;
	private Double income;
	private Double liquid;
	private Double growth;
	private Double diversified;
	
	public static String convertInvestType(String englishType) {
		if (englishType == null) {
			return "공격형";
		}
		switch (englishType) {
			case "CONSERVATIVE":
				return "보수형";
			case "STABLE":
				return "안정형";
			case "NEUTRAL":
				return "위험중립형";
			case "GROWTH":
				return "성장형";
			case "AGGRESSIVE":
			default:
				return "공격형";
		}
	}
	
	public static String determineInvestTypeByScore(Double stable, Double income, Double liquid, Double growth, Double diversified) {
		if (stable == null && income == null && liquid == null && growth == null && diversified == null) {
			return "공격형";
		}
		
		Double maxScore = 0.0;
		String investType = "공격형";
		
		if (stable != null && stable > maxScore) {
			maxScore = stable;
			investType = "안정형";
		}
		if (income != null && income > maxScore) {
			maxScore = income;
			investType = "수익형";
		}
		if (liquid != null && liquid > maxScore) {
			maxScore = liquid;
			investType = "유동형";
		}
		if (growth != null && growth > maxScore) {
			maxScore = growth;
			investType = "성장형";
		}
		if (diversified != null && diversified > maxScore) {
			maxScore = diversified;
			investType = "분산형";
		}
		
		return investType;
	}
	
	public static RealInvestTypeReq fromScores(Double stable, Double income, Double liquid, Double growth, Double diversified) {
		String investType = determineInvestTypeByScore(stable, income, liquid, growth, diversified);
		return new RealInvestTypeReq(investType, stable, income, liquid, growth, diversified);
	}
}
