package woojooin.planit.domain.product.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import woojooin.planit.domain.product.domain.EtfDailyHistory;
import woojooin.planit.domain.product.mapper.EtfDailyHistoryMapper;

@Repository
@AllArgsConstructor
public class EtfDailyHistoryRepository {

	private final EtfDailyHistoryMapper etfDailyHistoryMapper;

	public int saveAll(List<EtfDailyHistory> etfDailyHistories) {
		return etfDailyHistoryMapper.saveAll(etfDailyHistories);
	}

	public List<EtfDailyHistory> findByProductAfterStart(String shortenCode, LocalDate startDate) {
		return etfDailyHistoryMapper.findByProductAfterStart(shortenCode, startDate);
	}
}
