package woojooin.planit.domain.report.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountComparisonDTO {
	private long principal;         // 전체 원금 (ISA + 일반 합산 or 필요에 따라 분리 가능)

	private long isaTax;            // ISA 계좌 실제 내야할 세금
	private long isaTotalAmount;    // ISA 계좌 총금액 (원금 + 절세 효과)

	private long generalTax;        // 일반 계좌 세금
	private long generalTotalAmount;// 일반 계좌 총금액 (원금 + 절세 효과)

	private long taxSaved;          // 절세 효과 (전체 또는 ISA만)
	private double taxSavingRate;   // 절세율 (%)

	// getters, setters
}
