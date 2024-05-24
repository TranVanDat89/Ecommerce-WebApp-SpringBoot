package com.dattran.ecommerceapp.mapper;

import com.dattran.ecommerceapp.dto.OrderDTO;
import com.dattran.ecommerceapp.dto.ProductDTO;
import com.dattran.ecommerceapp.entity.Ingredient;
import com.dattran.ecommerceapp.entity.Order;
import com.dattran.ecommerceapp.entity.Product;
import com.dattran.ecommerceapp.entity.ProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    Product toProduct(ProductDTO productDTO);
    @Mapping(target = "flavors", ignore = true)
    Ingredient toIngredient(ProductDTO productDTO);
    ProductDetail toProductDetail(ProductDTO productDTO);
    Order toOder(OrderDTO orderDTO);
}
