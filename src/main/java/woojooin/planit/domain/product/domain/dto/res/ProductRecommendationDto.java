package woojooin.planit.domain.product.domain.dto.res;

import lombok.Builder;
import lombok.Data;
import woojooin.planit.domain.product.domain.Product;

@Data
@Builder
public class ProductRecommendationDto {
	private String itemName;        // 종목명
	private String shortenCode;     // 단축코드
	private String baseDate;        // 기준일자
	private String closingPrice;    // 종가
	private String fluctuationRate; // 등락률
	private String investType;      // 투자 위험성

	public static ProductRecommendationDto from(Product product) {
		return ProductRecommendationDto.builder()
			.itemName(product.getItemName())
			.shortenCode(product.getShortenCode())
			.baseDate(product.getBaseDate())
			.closingPrice(product.getClosingPrice())
			.fluctuationRate(product.getFluctuationRate())
			.investType(product.getInvestType())
			.build();
	}
}
