package woojooin.planit.domain.member.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.product.domain.ProductTypeCode;

@Data
@NoArgsConstructor
public class MemberProduct {

	private Long memberProductId;
	private Long memberId;
	private Long accountId;                  // 계좌ID
	private Long productId;                  // 상품ID
	private String productTypeCode;            // 상품유형코드
	private String productType;
	private BigDecimal avgPresentAmount;        // 평균매입가
	private BigDecimal presentAmount;           // 현재가
	private String balanceType;                 // 잔고유형
	private String itemName;                    // 상품/종목명
	private BigDecimal valuationPl;             // 평가손익
	private BigDecimal quantity;                // 보유수량
	private BigDecimal purchaseAmount;          // 매입금액
	private BigDecimal earningsRate;            // 수익률 (%)
	private String accountCurrency;             // 통화코드
	private BigDecimal settleQuantity;          // 정산수량
	private String itemCode;                    // 상품/종목코드
	private BigDecimal depositReceived;         // 예수금
	private int isIntegrated;               // 통합여부 (0: 미통합, 1: 통합)
	private BigDecimal valuationAmount;         // 평가금액
	private String accountExtends;              // 계좌번호 확장
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;


}