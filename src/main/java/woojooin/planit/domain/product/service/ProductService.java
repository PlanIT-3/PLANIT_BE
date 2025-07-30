package woojooin.planit.domain.product.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.product.domain.Product;
import woojooin.planit.domain.product.mapper.ProductMapper;
import woojooin.planit.global.util.openData.OpenApiUtil;
import woojooin.planit.global.util.openData.dto.OpenApiResponse;
import woojooin.planit.global.util.openData.dto.price.etf.ETFPriceRes;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
	private final OpenApiUtil openApiUtil;
	private final ProductMapper mapper;

	public void fetchAndSaveProducts(){
		OpenApiResponse<ETFPriceRes> response = openApiUtil.getETFPriceInfo();

		if (response == null ||
			response.getResponse() == null ||
			response.getResponse().getBody() == null ||
			response.getResponse().getBody().getItems() == null ||
			response.getResponse().getBody().getItems().getItem() == null) {

			System.out.println("ETF API 응답이 비어 있습니다.");
			return;
		}

		// 데이터 변환 및 저장
		for (ETFPriceRes.Item item : response.getResponse().getBody().getItems().getItem()) {
			Product product = ETFPriceRes.mapItemToProduct(item);
			log.info("product={}", product);
			mapper.insertProduct(product);
		}
	}


}
