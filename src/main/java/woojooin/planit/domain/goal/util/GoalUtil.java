package woojooin.planit.domain.goal.util;

public class GoalUtil {
	/**
	 * 계좌 실제 잔액과 할당 비율로 실제 투자 가능한 금액 계산
	 *
	 * @param accountActualAmount 실제 계좌 금액
	 * @param accountAllocatedRate 할당 비율 (예: 30이면 30%)
	 * @return 실제 투자 가능한 금액
	 */
	public static long calculateInvestableAmount(long accountActualAmount, int accountAllocatedRate) {
		// 할당 비율을 소수로 변환 (30 -> 0.3)
		double rate = accountAllocatedRate / 100.0;

		// 투자 가능한 금액 계산 (소수점 이하 절사)
		long investableAmount = (long) (accountActualAmount * rate);

		return investableAmount;
	}

	public static void main(String[] args) {
		long accountActualAmount = 1_000_000; // 예: 계좌에 100만원 있음
		int accountAllocatedRate = 30;        // 30%

		long investableAmount = calculateInvestableAmount(accountActualAmount, accountAllocatedRate);
		System.out.println("투자 가능한 금액: " + investableAmount + "원");
		// 출력: 투자 가능한 금액: 300000원
	}
}
