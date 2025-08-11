package woojooin.planit.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woojooin.planit.domain.report.domain.ReturnRateDto;
import woojooin.planit.domain.report.domain.ReturnType;
import woojooin.planit.domain.report.mapper.ReportMapper;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportMapper reportMapper;

    @Transactional(readOnly = true)
    public List<ReturnRateDto>getReturnRateByType(Long memberId, ReturnType returnType, LocalDate startDate){
        return reportMapper.findReturnRateByType(memberId, returnType, startDate);
    }
}
