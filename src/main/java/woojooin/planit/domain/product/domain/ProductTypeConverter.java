package woojooin.planit.domain.product.domain;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ProductTypeConverter extends BaseTypeHandler<ProductTypeCode> {

    /**
     * DB에 저장할 때: ENUM → String 코드 변환
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ProductTypeCode parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    /**
     * DB에서 조회할 때: String 코드 → ENUM 변환 (컬럼명으로)
     */
    @Override
    public ProductTypeCode getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return ProductTypeCode.fromCode(code);
    }

    /**
     * DB에서 조회할 때: String 코드 → ENUM 변환 (컬럼 인덱스로)
     */
    @Override
    public ProductTypeCode getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return ProductTypeCode.fromCode(code);
    }

    /**
     * Stored Procedure 호출 시: String 코드 → ENUM 변환
     */
    @Override
    public ProductTypeCode getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return ProductTypeCode.fromCode(code);
    }
}