package woojooin.planit.domain.account.dto.res;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceRes {
	private Long amount;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String createdAt;
	
	public BalanceRes(Long amount, LocalDateTime createdAt) {
		this.amount = amount;
		this.createdAt = createdAt.toString().substring(0, 19).replace("T", " ");
	}
}