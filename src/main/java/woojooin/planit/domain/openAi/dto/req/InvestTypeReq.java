package woojooin.planit.domain.openAi.dto.req;

import lombok.Data;

@Data
public class InvestTypeReq {
    private String investType;      // ex: "보수형", "안정형", "위험중립형", "성장형", "공격형"
    private Double stable;
    private Double income;
    private Double liquid;
    private Double growth;
    private Double diversified;
}
