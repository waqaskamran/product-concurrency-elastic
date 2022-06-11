package com.elastic.fetch.controller;

import com.elastic.fetch.dto.ProductRequestDTO;
import com.elastic.fetch.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    ResponseEntity<List<String>> GetAndUpdateProductCategory(@RequestBody ProductRequestDTO productRequestDTO){
        return new ResponseEntity<>(productService.assignCategoryToProduct(productRequestDTO.getNewCategoryId(),productRequestDTO.getCurrentCategoryId()), HttpStatus.OK);
    }
}
