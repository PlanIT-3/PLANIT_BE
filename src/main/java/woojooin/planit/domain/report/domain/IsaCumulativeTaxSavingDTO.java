package woojooin.planit.domain.report.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsaCumulativeTaxSavingDTO {
	private String quarter;
	private long cumulativeTaxSaved;  // 누적 절세액

	// getters, setters
}
