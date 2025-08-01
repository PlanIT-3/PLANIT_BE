package woojooin.planit.domain.product.domain.dto.res;

import lombok.Builder;
import lombok.Data;
import woojooin.planit.domain.product.domain.Product;

@Data
@Builder
public class ProductRecommendationDto {
	private String itmsNm;     // 종목명
	private String srtnCd;     // 단축코드
	private String basDt;      // 기준일자
	private String clpr;       // 종가
	private String fltRt;      // 등락률
	private String riskLevel;  // 투자 위험성

	public static ProductRecommendationDto from(Product product) {
		return ProductRecommendationDto.builder()
			.itmsNm(product.getItmsNm())
			.srtnCd(product.getSrtnCd())
			.basDt(product.getBasDt())
			.clpr(product.getClpr())
			.fltRt(product.getFltRt())
			.riskLevel(product.getRiskLevel())
			.build();
	}


}
