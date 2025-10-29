package woojooin.planit.domain.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.product.domain.Product;

public interface ProductMapper {
	List<Product> selectByRiskLevel(String riskLevel);

	Product selectBySrtnCd(String srtncd);

	Product findByProductId(Long productId);

	Product findByShortenCode(@Param("shortenCode") String shortenCode);

	Long selectProductIdByItemName(String itemName);
}
