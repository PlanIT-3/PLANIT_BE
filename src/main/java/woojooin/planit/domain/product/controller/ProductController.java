package woojooin.planit.domain.product.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.product.domain.Product;
import woojooin.planit.domain.product.service.ProductService;

@RestController
@RequestMapping("/auth/api/products")
@Api(value = "Product API", description = "상품 추천 API")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping("/recommend")
	@ApiOperation(value = "추천 상품 조회", notes = "사용자의 투자 위험성에 맞는 추천 상품 목록을 반환합니다.")
	public List<Product> getRecommendedProducts() {
		String userRiskLevel = "SAFE";
		return productService.recommendProduct(userRiskLevel);
	}
}
