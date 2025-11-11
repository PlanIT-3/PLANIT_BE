package woojooin.planit.global.config.datasource;

import woojooin.planit.global.enums.DataSourceType;

public class DataSourceContextHolder {

	private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<>();

	public static void set(DataSourceType type) {
		contextHolder.set(type);
	}

	public static DataSourceType get() {
		return contextHolder.get() == null ? DataSourceType.MASTER : contextHolder.get();
	}

	public static void clear() {
		contextHolder.remove();
	}
}
