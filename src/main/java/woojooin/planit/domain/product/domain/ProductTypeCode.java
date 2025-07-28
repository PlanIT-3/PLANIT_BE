package woojooin.planit.domain.product.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductTypeCode {

    STOCK("01", "주식"),
    FUND("02", "펀드"),
    CMA("03", "CMA"),
    OVERSEAS_STOCK("04", "해외주식"),
    TRUST_PENSION("05", "신탁/퇴직연금"),
    BOND("06", "채권"),
    RP("07", "RP"),
    CD_CP("08", "CD/CP"),
    ELS_DLS("09", "ELS/DLS"),
    OVERSEAS_MUTUAL_FUND("10", "해외무추얼펀드"),
    WRAP("11", "Wrap"),
    FOREIGN_RP("12", "외화RP"),
    PENSION_SAVINGS("13", "연금저축"),
    FUTURES_OPTIONS("14", "선물옵션"),
    ETC("99", "기타");

    private final String code;
    private final String description;


    public static ProductTypeCode fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return ETC;
        }

        for (ProductTypeCode type : ProductTypeCode.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }

        return ETC;
    }
}