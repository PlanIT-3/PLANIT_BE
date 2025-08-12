package woojooin.planit.domain.product.domain;

import lombok.Data;

@Data
public class Product {

	private String shortenCode;           // 단축 코드
	private String baseDate;              // 기준일자
	private String investType;            // 투자 유형 (CONSERVATIVE / AGGRESSIVE / GROWTH / NEUTRAL / STABLE)
	private String isinCode;              // ISIN 코드
	private String itemName;              // 종목명
	private String closingPrice;          // 종가
	private String difference;            // 대비 (전일 대비 등락가)
	private String fluctuationRate;       // 등락률
	private String netAssetValue;         // 순자산가치 (NAV)
	private String marketOpenPrice;       // 시가
	private String highPrice;             // 고가
	private String lowPrice;              // 저가
	private String tradeQuantity;         // 거래량
	private String tradePrice;            // 거래대금
	private String marketTotalAmount;     // 시가총액
	private String stockListingCount;     // 상장주식수
	private String baseIndexName;         // 기초지수명
	private String baseIndexClosingPrice; // 기초지수 종가
	private String netAssetTotalAmount;   // 순자산총액

}
