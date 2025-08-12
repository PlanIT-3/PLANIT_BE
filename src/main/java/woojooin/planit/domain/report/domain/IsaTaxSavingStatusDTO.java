package woojooin.planit.domain.report.domain;

import lombok.Data;

@Data
public class IsaTaxSavingStatusDTO {
	private int maxTaxSavingLimit;
	private int currentTaxSaving;
	private int remainingTaxSaving;

	// getters/setters
}

