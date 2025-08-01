package woojooin.planit.domain.product.domain.dto.res;

import lombok.Builder;
import lombok.Data;
import woojooin.planit.domain.product.domain.Product;

@Data
@Builder
public class ProductDetailDto {
	// Header
	private String itmsNm;        // 종목명
	private String isinCd;        // ISIN 코드
	private String srtnCd;        // 단축 코드
	private String basDt;         // 기준일자
	private String clpr;          // 종가
	private String vs;            // 전일 대비
	private String fltRt;         // 등락률

	// 요약 정보
	private String mkp;           // 시가
	private String hipr;          // 고가
	private String lopr;          // 저가

	// 계산 정보
	private String changeRange;   // 가격 변동폭 (고가 - 저가)

	// 거래 정보
	private String trqu;          // 거래량
	private String trPrc;         // 거래대금

	// 위험 정보
	private String riskLevel;     // 투자위험성

	public static ProductDetailDto from(Product product) {
		String changeRange = "-";
		try {
			int high = Integer.parseInt(product.getHipr());
			int low = Integer.parseInt(product.getLopr());
			changeRange = String.valueOf(high - low);
		} catch (NumberFormatException ignored) {}

		return ProductDetailDto.builder()
			.itmsNm(product.getItmsNm())
			.isinCd(product.getIsinCd())
			.srtnCd(product.getSrtnCd())
			.basDt(product.getBasDt())
			.clpr(product.getClpr())
			.vs(product.getVs())
			.fltRt(product.getFltRt())
			.mkp(product.getMkp())
			.hipr(product.getHipr())
			.lopr(product.getLopr())
			.changeRange(changeRange)
			.trqu(product.getTrqu())
			.trPrc(product.getTrPrc())
			.riskLevel(product.getRiskLevel())
			.build();
	}

}
