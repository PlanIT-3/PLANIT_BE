package woojooin.planit.domain.product.domain.dto.res;

import lombok.Builder;
import lombok.Data;
import woojooin.planit.domain.product.domain.Product;

@Data
@Builder
public class ProductDetailDto {
	// Header
	private String itemName;        // 종목명
	private String isinCode;        // ISIN 코드
	private String shortenCode;     // 단축 코드
	private String baseDate;        // 기준일자
	private String closingPrice;    // 종가
	private String difference;      // 전일 대비
	private String fluctuationRate; // 등락률

	// 요약 정보
	private String marketOpenPrice; // 시가
	private String highPrice;       // 고가
	private String lowPrice;        // 저가

	// 계산 정보
	private String changeRange;     // 가격 변동폭 (고가 - 저가)

	// 거래 정보
	private String tradeQuantity;   // 거래량
	private String tradePrice;      // 거래대금

	// 위험 정보
	private String investType;      // 투자위험성

	public static ProductDetailDto from(Product product) {
		String changeRange = "-";
		try {
			int high = Integer.parseInt(product.getHighPrice());
			int low = Integer.parseInt(product.getLowPrice());
			changeRange = String.valueOf(high - low);
		} catch (NumberFormatException ignored) {}

		return ProductDetailDto.builder()
			.itemName(product.getItemName())
			.isinCode(product.getIsinCode())
			.shortenCode(product.getShortenCode())
			.baseDate(product.getBaseDate())
			.closingPrice(product.getClosingPrice())
			.difference(product.getDifference())
			.fluctuationRate(product.getFluctuationRate())
			.marketOpenPrice(product.getMarketOpenPrice())
			.highPrice(product.getHighPrice())
			.lowPrice(product.getLowPrice())
			.changeRange(changeRange)
			.tradeQuantity(product.getTradeQuantity())
			.tradePrice(product.getTradePrice())
			.investType(product.getInvestType())
			.build();
	}
}
