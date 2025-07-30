package woojooin.planit.domain.object.action.domain;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ActionTypeConverter extends BaseTypeHandler<ActionType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ActionType parameter, JdbcType jdbcType) throws
        SQLException {
        ps.setString(i, parameter.getDescription());
    }

    @Override
    public ActionType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String description = rs.getString(columnName);
        return ActionType.fromCode(description);
    }

    @Override
    public ActionType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String description = rs.getString(columnIndex);
        return ActionType.fromCode(description);
    }

    @Override
    public ActionType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String description = cs.getString(columnIndex);
        return ActionType.fromCode(description);
    }
}