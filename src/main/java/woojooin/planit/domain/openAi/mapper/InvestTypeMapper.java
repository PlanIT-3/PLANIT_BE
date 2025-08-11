package woojooin.planit.domain.openAi.mapper;

import org.apache.ibatis.annotations.Mapper;
import woojooin.planit.domain.openAi.dto.req.InvestTypeReq;

@Mapper
public interface InvestTypeMapper {
InvestTypeReq getInvestTypeByMemberId(Long memberId);
}
