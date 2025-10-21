package woojooin.planit.domain.member.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.goal.domain.vo.Action;
import woojooin.planit.domain.product.domain.ProductTypeCode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberProduct {
	private Long memberProductId;
	private Long memberId;
	private Long accountId;
	private List<Action> actionList;
	private Member member;

	// ==== 상품 정보 ====
	private ProductTypeCode productTypeCode;            // 상품유형코드
	private String itemName;                    // 상품/종목명
	private String itemCode;                    // 상품/종목코드
	private String productId;

	// ==== 계좌 정보 ====
	private String accountNumber;               // 계좌번호
	private String accountExtends;              // 계좌번호 확장
	private String balanceType;                 // 잔고유형
	private String accountCurrency;             // 통화코드

	private BigDecimal avgPresentAmount;        // 평균매입가
	private BigDecimal presentAmount;           // 현재가
	private BigDecimal valuationPl;             // 평가손익
	private BigDecimal valuationAmount;         // 평가금액
	private BigDecimal purchaseAmount;          // 매입금액
	private BigDecimal earningsRate;            // 수익률 (%)
	private BigDecimal depositReceived;         // 예수금
	private BigDecimal quantity;                // 보유수량
	private BigDecimal settleQuantity;          // 정산수량
	private int isIntegrated;               // 통합여부 (0: 미통합, 1: 통합)
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}