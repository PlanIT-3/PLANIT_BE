package woojooin.planit.global.util.openData.dto.price.etf;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import woojooin.planit.domain.product.domain.Product;
import woojooin.planit.domain.product.domain.RiskLevel;

@Data
public class ETFPriceRes {

	private int numOfRows;
	private int pageNo;
	private int totalCount;
	private Items items;

	@Data
	public static class Items {
		private List<Item> item;
	}

	@Data
	public static class Item {

		@JsonProperty("basDt")
		private String baseDate;                // 기준일자

		@JsonProperty("srtnCd")
		private String shortenCode;             // 단축코드

		@JsonProperty("isinCd")
		private String isinCode;                 // ISIN코드

		@JsonProperty("itmsNm")
		private String itemName;                 // 종목명

		@JsonProperty("clpr")
		private String closingPrice;             // 종가

		@JsonProperty("vs")
		private String difference;               // 전일 대비

		@JsonProperty("fltRt")
		private String fluctuationRate;          // 등락률

		@JsonProperty("nav")
		private String netAssetValue;            // 순자산가치

		@JsonProperty("mkp")
		private String marketOpenPrice;          // 시가

		@JsonProperty("hipr")
		private String highPrice;                // 고가

		@JsonProperty("lopr")
		private String lowPrice;                 // 저가

		@JsonProperty("trqu")
		private String tradeQuantity;            // 거래량

		@JsonProperty("trPrc")
		private String tradePrice;               // 거래대금

		@JsonProperty("mrktTotAmt")
		private String marketTotalAmount;        // 시가총액

		@JsonProperty("stLstgCnt")
		private String stockListingCount;        // 상장주식수

		@JsonProperty("bssIdxIdxNm")
		private String baseIndexName;            // 기초지수명

		@JsonProperty("bssIdxClpr")
		private String baseIndexClosingPrice;    // 기초지수 종가

		@JsonProperty("nPptTotAmt")
		private String netAssetTotalAmount;      // 순자산총액
	}

	public static Product mapItemToProduct(Item item) {
		Product product = new Product();

		product.setShortenCode(item.getShortenCode());
		product.setIsinCode(item.getIsinCode());
		product.setItemName(item.getItemName());
		product.setBaseDate(item.getBaseDate());
		product.setClosingPrice(item.getClosingPrice());
		product.setDifference(item.getDifference());
		product.setFluctuationRate(item.getFluctuationRate());
		product.setNetAssetValue(item.getNetAssetValue());
		product.setMarketOpenPrice(item.getMarketOpenPrice());
		product.setHighPrice(item.getHighPrice());
		product.setLowPrice(item.getLowPrice());
		product.setTradeQuantity(item.getTradeQuantity());
		product.setTradePrice(item.getTradePrice());
		product.setMarketTotalAmount(item.getMarketTotalAmount());
		product.setStockListingCount(item.getStockListingCount());
		product.setBaseIndexName(item.getBaseIndexName());
		product.setBaseIndexClosingPrice(item.getBaseIndexClosingPrice());
		product.setNetAssetTotalAmount(item.getNetAssetTotalAmount());

		product.setInvestType(String.valueOf(RiskLevel.SAFE));

		return product;
	}
}
