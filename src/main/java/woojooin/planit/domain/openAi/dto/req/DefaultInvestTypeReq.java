package woojooin.planit.domain.openAi.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultInvestTypeReq {
    private String investType;      // ex: "보수형", "안정형", "위험중립형", "성장형", "공격형"
    private Double stable;
    private Double income;
    private Double liquid;
    private Double growth;
    private Double diversified;


    public static DefaultInvestTypeReq conservative() {
        // 보수형: 안정성 35, 수익성 7, 유동성 21, 성장성 7, 분산투자 30
        return new DefaultInvestTypeReq("보수형", 35.0, 7.0, 21.0, 7.0, 30.0);
    }

    public static DefaultInvestTypeReq stable() {
        // 안정형: 안정성 30, 수익성 10, 유동성 20, 성장성 10, 분산투자 30
        return new DefaultInvestTypeReq("안정형", 30.0, 10.0, 20.0, 10.0, 30.0);
    }

    public static DefaultInvestTypeReq neutral() {
        // 위험중립형: 모든 항목 20
        return new DefaultInvestTypeReq("위험중립형", 20.0, 20.0, 20.0, 20.0, 20.0);
    }

    public static DefaultInvestTypeReq growth() {
        // 성장형: 안정성 15, 수익성 25, 유동성 15, 성장성 35, 분산투자 10
        return new DefaultInvestTypeReq("성장형", 15.0, 25.0, 15.0, 35.0, 10.0);
    }

    public static DefaultInvestTypeReq aggressive() {
        // 공격형: 안정성 10, 수익성 35, 유동성 10, 성장성 35, 분산투자 10
        return new DefaultInvestTypeReq("공격형", 10.0, 35.0, 10.0, 35.0, 10.0);
    }
}
