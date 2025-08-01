package woojooin.planit.domain.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.product.domain.dto.res.ProductDetailDto;
import woojooin.planit.domain.product.service.ProductService;
import woojooin.planit.global.response.Response;

@RestController
@RequestMapping("/api/products")
@Api(value = "Product API", description = "상품 API")
@RequiredArgsConstructor
public class PublicProductController {

	private final ProductService productService;

	@GetMapping("/{srtnCd}")
	public ResponseEntity<Response<ProductDetailDto>> getProductDetail(@PathVariable("srtnCd") String srtnCd) {
		ProductDetailDto productDetail = productService.getProductBySrtnCd(srtnCd);
		return ResponseEntity.ok(Response.ok(productDetail));
	}

}
