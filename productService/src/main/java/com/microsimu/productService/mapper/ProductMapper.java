package com.microsimu.productService.mapper;
import com.microsimu.productService.dto.request.ProductRequestDto;
import com.microsimu.productService.entity.ProductEntity;

public final class ProductMapper {
	private ProductMapper() {}

	public static ProductEntity toEntityFromRequestDto(ProductRequestDto dto) {
		if ( dto == null ) {
			return null;
		}

		ProductEntity productEntity = new ProductEntity();

		productEntity.setId( dto.getId() );
		productEntity.setName( dto.getName() );
		productEntity.setPrice( dto.getPrice() );
		productEntity.setStock( dto.getStock() );
		productEntity.setCategory( dto.getCategory() );

		return productEntity;
	}
}
