package woojooin.planit.domain.product.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.product.service.ProductService;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	//배치로 수정 예정
	@GetMapping("/save")
	public String saveProducts() {
		productService.fetchAndSaveProducts();
		return "저장 완료!";
	}

}
