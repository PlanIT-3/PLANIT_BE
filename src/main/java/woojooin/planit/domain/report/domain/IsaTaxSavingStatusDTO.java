package woojooin.planit.domain.report.domain;

import lombok.Data;

@Data
public class IsaTaxSavingStatusDTO {
	private int maxTaxSavingLimit;
	private int currentTaxSaving;
	private int remainingTaxSaving;
	private double  usageRate;

	// getters/setters

	public void calculateFields() {
		this.remainingTaxSaving = maxTaxSavingLimit - currentTaxSaving;
		this.usageRate = maxTaxSavingLimit == 0 ? 0 : (double) currentTaxSaving / maxTaxSavingLimit * 100;
	}
}

