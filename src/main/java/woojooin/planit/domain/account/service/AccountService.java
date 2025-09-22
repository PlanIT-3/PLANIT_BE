package woojooin.planit.domain.account.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.account.domain.Account;
import woojooin.planit.domain.account.domain.BalanceData;
import woojooin.planit.domain.account.api.dto.res.AccountBankRes;
import woojooin.planit.domain.account.api.dto.res.AccountsRes;
import woojooin.planit.domain.account.api.dto.res.AccountListRes;
import woojooin.planit.domain.account.api.dto.res.BalanceListRes;
import woojooin.planit.domain.account.api.dto.res.BalanceRes;
import woojooin.planit.domain.account.mapper.AccountMapper;
import woojooin.planit.domain.goal.domain.vo.Goal;
import woojooin.planit.domain.goal.domain.enums.Bank;
import woojooin.planit.domain.goal.api.dto.res.GoalRatioListRes;
import woojooin.planit.domain.goal.api.dto.res.GoalRatioRes;
import woojooin.planit.domain.goal.mapper.GoalMapper;
import woojooin.planit.domain.member.domain.MemberProduct;
import woojooin.planit.domain.member.mapper.MemberMapper;
import woojooin.planit.domain.member.mapper.MemberProductMapper;
import woojooin.planit.domain.product.domain.ProductTypeCode;
import woojooin.planit.domain.product.mapper.ProductMapper;
import woojooin.planit.global.security.dto.response.AccountConnectionRes;
import woojooin.planit.global.security.jwt.JwtTokenProvider;
import woojooin.planit.global.util.codef.CodefAccountUtil;
import woojooin.planit.global.util.codef.dto.account.CodefAccountData;
import woojooin.planit.global.util.codef.dto.account.CodefSecuritiesAccountData;
import woojooin.planit.global.util.codef.dto.account.CodefSecuritiesAccountProductData;
import woojooin.planit.global.util.codef.dto.account.ResDepositTrust;
import woojooin.planit.global.util.codef.dto.account.ResProductItem;
import woojooin.planit.global.util.codef.dto.account.ResSecuritiesAccount;
import woojooin.planit.global.util.codef.dto.connectedId.AccountDto;
import woojooin.planit.global.util.codef.dto.connectedId.create.ConntectedIdCreateRes;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

	private final MemberMapper memberMapper;
	private final JwtTokenProvider jwtTokenProvider;
	private final CodefAccountUtil codefAccountUtil;
	private final AccountMapper accountMapper;
	private final GoalMapper goalMapper;
	private final MemberProductMapper memberProductMapper;
	private final ProductMapper productMapper;

	public BalanceListRes getAccountBalanceForDate(Long memberId, String period) {
		String startDate = calculateStartDate(period);
		List<BalanceData> balanceDataList = accountMapper.getBalanceByMemberIdAndPeriod(memberId, period, startDate);
		
		List<BalanceRes> balanceRes = balanceDataList.stream()
				.map(balance -> new BalanceRes(
						balance.getAmount(), 
						balance.getCreatedAt()))
				.collect(Collectors.toList());
		
		return new BalanceListRes(balanceRes);
	}
	
	private String calculateStartDate(String period) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDateTime;
		
		switch (period) {
			case "day":
				startDateTime = now.minusDays(7);
				break;
			case "week":
				startDateTime = now.minusWeeks(6);
				break;
			case "month":
				startDateTime = now.minusMonths(6);
				break;
			default:
				startDateTime = now.minusDays(7);
				break;
		}
		
		return startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public GoalRatioListRes getGoalRatioBasedOnAccount(Long memberId) {
		BigDecimal totalBalance = accountMapper.getTotalBalanceByMemberId(memberId);
		List<Goal> goals = goalMapper.selectAllGoals(memberId);
		
		List<GoalRatioRes> goalRatios = goals.stream()
				.map(goal -> {
					BigDecimal ratio = BigDecimal.ZERO;
					if (totalBalance.compareTo(BigDecimal.ZERO) > 0) {
						ratio = new BigDecimal(goal.getTargetAmount())
								.divide(totalBalance, 4, RoundingMode.HALF_UP)
								.multiply(new BigDecimal("100"));
					}
					return new GoalRatioRes(goal.getGoalName(), goal.getTargetAmount(), ratio);
				})
				.collect(Collectors.toList());
		
		return new GoalRatioListRes(totalBalance, goalRatios);
	}

	public AccountConnectionRes register(AccountDto accountDto, boolean isLast, Long memberId) {

		String connectedId = memberMapper.findByConnectedIdString(memberId);
		Boolean isRural = accountDto.getIsRural();

		AccountDto apiAccountDto = AccountDto.builder()
			.countryCode(accountDto.getCountryCode())
			.businessType(accountDto.getBusinessType())
			.clientType(accountDto.getClientType())
			.organization(accountDto.getOrganization())
			.loginType(accountDto.getLoginType())
			.id(accountDto.getId())
			.password(accountDto.getPassword())
			.birthDate(accountDto.getBirthDate())
			.build();

		List<AccountDto> singleAccount = List.of(apiAccountDto);
		ConntectedIdCreateRes result = null;

		if (connectedId == null) {
			result = codefAccountUtil.registerConnectedId(singleAccount);
			String newConnectedId = result.getConnectedId();

			memberMapper.updateConnectedId(memberId, newConnectedId);

			updateIsaType(memberId, isRural);

			connectAccount(newConnectedId, accountDto.getOrganization(), memberId, accountDto.getBusinessType(),
				accountDto.getId(), accountDto.getPassword());

			if (isLast) {
				String newAccessToken = jwtTokenProvider.createValidatedAccessToken(memberId, "SEMI_USER");
				String newRefreshToken = jwtTokenProvider.createValidatedRefreshToken(memberId, "SEMI_USER");

				return new AccountConnectionRes(result, newAccessToken, newRefreshToken);
			} else {
				return new AccountConnectionRes(result, "", "");
			}
		} else {
			result = codefAccountUtil.addAccount(singleAccount, connectedId);

			updateIsaType(memberId, isRural);
			connectAccount(connectedId, accountDto.getOrganization(), memberId, accountDto.getBusinessType(),accountDto.getId(), accountDto.getPassword());
			return new AccountConnectionRes(result, "", "");
		}
	}

	private void connectAccount(String connectedId, String organization, Long memberId, String businessType, String id,
		String password) {
		try {
			if ("BK".equals(businessType)) {
				CodefAccountData accountData = codefAccountUtil.getAccountData(connectedId, organization);
				
				if (accountData != null && accountData.getResDepositTrust() != null) {
					for (ResDepositTrust depositTrust : accountData.getResDepositTrust()) {
						Account account = convertToAccount(depositTrust, memberId, businessType, organization);
						accountMapper.insertAccount(account);
						log.info("계좌 저장 완료 - 계좌번호: {}, 계좌명: {}, 타입: {}", account.getAccountNumber(), account.getAccountName(), account.getAccountType());
					}
				}
			} else if ("ST".equals(businessType)) {
				CodefSecuritiesAccountData securitiesData = codefAccountUtil.getSecuritiesAccountData(connectedId,
					organization);

				if (securitiesData != null && securitiesData.getData() != null) {
					for (ResSecuritiesAccount securitiesAccount : securitiesData.getData()) {
						Account account = convertSecuritiesToAccount(securitiesAccount, memberId, organization);
						accountMapper.insertAccount(account);
						Long accountId = account.getAccountId();

						CodefSecuritiesAccountProductData accountProductData = codefAccountUtil.getAccountProductData(
							connectedId, organization, id, password, account.getAccountNumber());

						if (accountProductData != null && accountProductData.getData() != null &&
							accountProductData.getData().getResItemList() != null) {
							List<MemberProduct> memberProducts = convertToMemberProducts(
								accountProductData.getData().getResItemList(), memberId, accountId, account.getAccountNumber(),
								accountProductData.getData().getResDepositReceived());

							if (!memberProducts.isEmpty()) {
								memberProductMapper.insertBatch(memberProducts);
								log.info("상품 데이터 저장 완료 - 계좌번호: {}, 상품 수: {}", account.getAccountNumber(), memberProducts.size());
							}
						}

						log.info("증권 계좌 저장 완료 - ID: {}, 계좌번호: {}, 계좌명: {}, 타입: {}", accountId,
							account.getAccountNumber(), account.getAccountName(), account.getAccountType());
					}
				}
			}
		} catch (Exception e) {
			log.error("계좌 데이터 저장 중 오류 발생: ", e);
			throw new RuntimeException("계좌 데이터 저장 실패", e);
		}
	}

	private Account convertToAccount(ResDepositTrust depositTrust, Long memberId, String businessType, String organization) {
		Account account = new Account();
		
		account.setMemberId(memberId);
		account.setAccountName(depositTrust.getResAccountName());
		account.setAccountNumber(depositTrust.getResAccount());
		account.setAccountCurrency(depositTrust.getResAccountCurrency());

		account.setAccountType("DEPOSIT");
		
		// 금액 필드 처리 (null 체크 및 변환)
		if (depositTrust.getResAccountBalance() != null && !depositTrust.getResAccountBalance().isEmpty()) {
			account.setAccountBalance(new BigDecimal(depositTrust.getResAccountBalance()));
		} else {
			account.setAccountBalance(BigDecimal.ZERO);
		}
		
		if (depositTrust.getResAccountDeposit() != null && !depositTrust.getResAccountDeposit().isEmpty()) {
			account.setAccountDeposit(new BigDecimal(depositTrust.getResAccountDeposit()));
		} else {
			account.setAccountDeposit(BigDecimal.ZERO);
		}
		
		// 날짜 필드 처리
		if (depositTrust.getResLastTranDate() != null && !depositTrust.getResLastTranDate().isEmpty()) {
			account.setLastTranDate(parseDate(depositTrust.getResLastTranDate()));
		}
		
		if (depositTrust.getResAccountStartDate() != null && !depositTrust.getResAccountStartDate().isEmpty()) {
			account.setAccountStartDate(parseDate(depositTrust.getResAccountStartDate()));
		}
		
		if (depositTrust.getResAccountEndDate() != null && !depositTrust.getResAccountEndDate().isEmpty()) {
			account.setAccountEndDate(parseDate(depositTrust.getResAccountEndDate()));
		}
		
		// 기본값 설정
		account.setEarningsRate(BigDecimal.ZERO);
		account.setAccountInvestedCost(BigDecimal.ZERO);
		account.setIsDeleted(false);
		account.setIsIntegrated(true);
		account.setBankCode(organization);
		account.setCreatedAt(LocalDateTime.now());
		account.setUpdatedAt(LocalDateTime.now());
		
		return account;
	}
	
	private Account convertSecuritiesToAccount(ResSecuritiesAccount securitiesAccount, Long memberId, String organization) {
		Account account = new Account();
		
		account.setMemberId(memberId);
		account.setAccountName(securitiesAccount.getResAccountName());
		account.setAccountNumber(securitiesAccount.getResAccount());
		account.setAccountCurrency("KRW");
		
		account.setAccountType("ISA");
		
		BigDecimal valuationAmt = BigDecimal.ZERO;
		if (securitiesAccount.getResValuationAmt() != null && !securitiesAccount.getResValuationAmt().isEmpty()) {
			valuationAmt = new BigDecimal(securitiesAccount.getResValuationAmt());
		}
		
		BigDecimal withdrawalAmt = BigDecimal.ZERO;
		if (securitiesAccount.getResWithdrawalAmt() != null && !securitiesAccount.getResWithdrawalAmt().isEmpty()) {
			withdrawalAmt = new BigDecimal(securitiesAccount.getResWithdrawalAmt());
		}
		
		account.setAccountBalance(valuationAmt.add(withdrawalAmt));

		if (securitiesAccount.getResPurchaseAmount() != null && !securitiesAccount.getResPurchaseAmount().isEmpty()) {
			account.setAccountInvestedCost(new BigDecimal(securitiesAccount.getResPurchaseAmount()));
		} else {
			account.setAccountInvestedCost(BigDecimal.ZERO);
		}
		
		account.setIsDeleted(false);
		account.setIsIntegrated(true);
		account.setBankCode(organization);
		account.setCreatedAt(LocalDateTime.now());
		account.setUpdatedAt(LocalDateTime.now());
		
		return account;
	}
	
	private LocalDateTime parseDate(String dateStr) {
		try {
			// CODEF 날짜 형식: YYYYMMDD
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate date = LocalDate.parse(dateStr, formatter);
			return date.atStartOfDay();
		} catch (Exception e) {
			log.warn("날짜 파싱 실패: {}", dateStr, e);
			return null;
		}
	}

	public AccountsRes getAccountList(Long memberId) {
		List<AccountBankRes> accounts = accountMapper.selectAccountsByMemberId(memberId);
		
		List<AccountListRes> accountListRes = accounts.stream()
				.map(account -> {
					String organization = Bank.getNameByCode(account.getBankCode());
					if (organization == null) {
						organization = "알 수 없는 은행";
					}
					return new AccountListRes(organization, account.getAccountNumber());
				})
				.collect(Collectors.toList());
		
		return new AccountsRes(accountListRes);
	}
	
	private void updateIsaType(Long memberId, Boolean isRural) {
		if (isRural == null) {
			return;
		} else if (isRural) {
			memberMapper.updateIsaType(memberId, "RURAL");
		} else {
			memberMapper.updateIsaType(memberId, "GENERAL");
		}
	}

	private List<MemberProduct> convertToMemberProducts(List<ResProductItem> resProductItems, Long memberId,
		Long accountId, String accountNumber, String depositReceived) {
		return resProductItems.stream().map(item -> {
			MemberProduct memberProduct = new MemberProduct();

			memberProduct.setMemberId(memberId);
			memberProduct.setAccountId(accountId);
			memberProduct.setProductTypeCode(ProductTypeCode.fromCode(item.getResProductTypeCd()));
			memberProduct.setItemName(item.getResItemName());
			memberProduct.setItemCode(item.getResItemCode());
			memberProduct.setBalanceType(item.getResBalanceType());
			memberProduct.setAccountCurrency(item.getResAccountCurrency());
			memberProduct.setAccountNumber(accountNumber);
			memberProduct.setAccountExtends(accountNumber);

			// 상품명으로 product_id 조회, 없으면 기본값으로 설정
			Long productIdLong = productMapper.selectProductIdByItemName(item.getResItemName());
			memberProduct.setProductId(productIdLong != null ? productIdLong.toString() : "1");

			// BigDecimal 필드 처리 (null 체크 및 변환)
			if (item.getResQuantity() != null && !item.getResQuantity().isEmpty()) {
				memberProduct.setQuantity(new BigDecimal(item.getResQuantity()));
			} else {
				memberProduct.setQuantity(BigDecimal.ZERO);
			}

			if (item.getResSettleQuantity() != null && !item.getResSettleQuantity().isEmpty()) {
				memberProduct.setSettleQuantity(new BigDecimal(item.getResSettleQuantity()));
			} else {
				memberProduct.setSettleQuantity(BigDecimal.ZERO);
			}

			if (item.getResPresentAmt() != null && !item.getResPresentAmt().isEmpty()) {
				memberProduct.setPresentAmount(new BigDecimal(item.getResPresentAmt()));
			} else {
				memberProduct.setPresentAmount(BigDecimal.ZERO);
			}

			if (item.getResAvgPresentAmt() != null && !item.getResAvgPresentAmt().isEmpty()) {
				memberProduct.setAvgPresentAmount(new BigDecimal(item.getResAvgPresentAmt()));
			} else {
				memberProduct.setAvgPresentAmount(BigDecimal.ZERO);
			}

			if (item.getResPurchaseAmount() != null && !item.getResPurchaseAmount().isEmpty()) {
				memberProduct.setPurchaseAmount(new BigDecimal(item.getResPurchaseAmount()));
			} else {
				memberProduct.setPurchaseAmount(BigDecimal.ZERO);
			}

			if (item.getResValuationAmt() != null && !item.getResValuationAmt().isEmpty()) {
				memberProduct.setValuationAmount(new BigDecimal(item.getResValuationAmt()));
			} else {
				memberProduct.setValuationAmount(BigDecimal.ZERO);
			}

			if (item.getResValuationPL() != null && !item.getResValuationPL().isEmpty()) {
				memberProduct.setValuationPl(new BigDecimal(item.getResValuationPL()));
			} else {
				memberProduct.setValuationPl(BigDecimal.ZERO);
			}

			if (item.getResEarningsRate() != null && !item.getResEarningsRate().isEmpty()) {
				memberProduct.setEarningsRate(new BigDecimal(item.getResEarningsRate()));
			} else {
				memberProduct.setEarningsRate(BigDecimal.ZERO);
			}

			if (depositReceived != null && !depositReceived.isEmpty()) {
				memberProduct.setDepositReceived(new BigDecimal(depositReceived));
			} else {
				memberProduct.setDepositReceived(BigDecimal.ZERO);
			}

			memberProduct.setIsIntegrated(1);
			memberProduct.setCreatedAt(LocalDateTime.now());
			memberProduct.setUpdatedAt(LocalDateTime.now());

			return memberProduct;
		}).collect(Collectors.toList());
	}
}
