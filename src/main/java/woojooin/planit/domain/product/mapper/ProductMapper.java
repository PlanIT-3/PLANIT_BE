package woojooin.planit.domain.product.mapper;

import java.util.List;

import woojooin.planit.domain.product.domain.Product;

public interface ProductMapper {
	void insertProduct(Product product);
	void deleteAll();
	List<Product> selectByRiskLevel(String riskLevel);
	Product selectBySrtnCd(String srtncd);
}
