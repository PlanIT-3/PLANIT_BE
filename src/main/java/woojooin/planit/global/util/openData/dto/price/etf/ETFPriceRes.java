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
		private String basDt;            // 기준일자

		@JsonProperty("srtnCd")
		private String srtnCd;           // 단축코드

		@JsonProperty("isinCd")
		private String isinCd;           // ISIN코드

		@JsonProperty("itmsNm")
		private String itmsNm;           // 종목명

		@JsonProperty("clpr")
		private String clpr;             // 종가

		@JsonProperty("vs")
		private String vs;               // 대비

		@JsonProperty("fltRt")
		private String fltRt;            // 등락률

		@JsonProperty("nav")
		private String nav;              // 순자산가치

		@JsonProperty("mkp")
		private String mkp;              // 시가

		@JsonProperty("hipr")
		private String hipr;             // 고가

		@JsonProperty("lopr")
		private String lopr;             // 저가

		@JsonProperty("trqu")
		private String trqu;             // 거래량

		@JsonProperty("trPrc")
		private String trPrc;            // 거래대금

		@JsonProperty("mrktTotAmt")
		private String mrktTotAmt;       // 시가총액

		@JsonProperty("stLstgCnt")
		private String stLstgCnt;        // 상장주식수

		@JsonProperty("bssIdxIdxNm")
		private String bssIdxIdxNm;      // 기초지수명

		@JsonProperty("bssIdxClpr")
		private String bssIdxClpr;       // 기초지수 종가

		@JsonProperty("nPptTotAmt")
		private String nPptTotAmt;       // 순자산총액
	}

	public static Product mapItemToProduct(Item item) {
		Product product = new Product();

		product.setSrtnCd(item.getSrtnCd());
		product.setIsinCd(item.getIsinCd());
		product.setItmsNm(item.getItmsNm());
		product.setBasDt(item.getBasDt());
		product.setClpr(item.getClpr());
		product.setVs(item.getVs());
		product.setFltRt(item.getFltRt());
		product.setMkp(item.getMkp());
		product.setHipr(item.getHipr());
		product.setLopr(item.getLopr());
		product.setTrqu(item.getTrqu());
		product.setTrPrc(item.getTrPrc());
		product.setLstgStCnt(item.getStLstgCnt());
		product.setMrktTotAmt(item.getMrktTotAmt());

		product.setRiskLevel(String.valueOf(RiskLevel.SAFE));

		return product;
	}


}
