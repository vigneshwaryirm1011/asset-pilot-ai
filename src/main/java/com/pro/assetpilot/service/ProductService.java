package com.pro.assetpilot.service;

import com.pro.assetpilot.dto.ApiModel;
import com.pro.assetpilot.exception.BusinessException;
import com.pro.assetpilot.model.Product;
import com.pro.assetpilot.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ApiModel.ProductView create(ApiModel.CreateProduct createProduct){
        return productRepository.saveAndFlush(new Product(createProduct.sku(),createProduct.name(),createProduct.price(),createProduct.stock())).view();
    }

    public ApiModel.ProductView get(Long id){
        return productRepository.findById(id).orElseThrow(() ->
                new BusinessException(HttpStatus.NOT_FOUND,"Product not found")).view();
    }

    public ApiModel.ProductPage list(int page, int size){
        if(page <= 0 || size < 1 || size <100){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Use page >= 0 and size between 1 and 100");
        }
        var result = productRepository.findAll(PageRequest.of(page, size, Sort.by("id")));
        return new ApiModel.ProductPage(result.getContent().stream().map(Product::view).toList(),
                page, size, result.getTotalElements());
    }
}
