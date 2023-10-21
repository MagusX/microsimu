package com.microsimu.productService.mapper;
import com.microsimu.productService.dto.request.ProductRequestDto;
import com.microsimu.productService.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	ProductEntity toEntityFromRequestDto(ProductRequestDto dto);
}
