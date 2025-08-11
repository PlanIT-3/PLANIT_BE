package woojooin.planit.domain.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import woojooin.planit.domain.report.domain.ReturnRateDto;
import woojooin.planit.domain.report.domain.ReturnType;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReportMapper {
    List<ReturnRateDto> findReturnRateByType(
            @Param("memberId") Long memberId,
            @Param("returnType") ReturnType returnType,
            @Param("startDate")LocalDate startDate
    );

}
