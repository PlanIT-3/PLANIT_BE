package woojooin.planit.domain.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.product.domain.Product;
import woojooin.planit.domain.product.service.ProductService;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.response.Response;
import woojooin.planit.global.response.ResponseCode;

@RestController
@RequestMapping("/api/products")
@Api(value = "Product API", description = "상품 API")
@RequiredArgsConstructor
public class PublicProductController {

	private final ProductService productService;

	@GetMapping("/{srtnCd}")
	public ResponseEntity<Response<Product>> getProductDetail(@PathVariable("srtnCd") String srtnCd) {
		Product product = productService.getProductBySrtnCd(srtnCd);
		return ResponseEntity.ok(Response.ok(product));
	}

}
