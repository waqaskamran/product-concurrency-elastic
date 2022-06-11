package com.elastic.fetch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.elastic.fetch.entity.Product;
import com.elastic.fetch.entity.ProductResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ProductService {

    private static final String PRODUCT_INDEX = "productindex";

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public List<String> assignCategoryToProduct(String newCategoryId, String currentCategoryId) {
        List<ProductResponseDTO> productResponseDTOS = searchProductByCriteria(currentCategoryId);
        List<String> productIds = new ArrayList<>();
        try {
            for (ProductResponseDTO productResponseDTO : productResponseDTOS) {

                if (productResponseDTO != null) {
                    productResponseDTO.getProduct().setCategory(newCategoryId);
                    int counter = productResponseDTO.getProduct().getCounter() + 1;
                    productResponseDTO.getProduct().setCounter(counter);


                    UpdateRequest updateRequest = new UpdateRequest.Builder<>().index("productindex").id(productResponseDTO.getIndexId()).doc(productResponseDTO.getProduct()).build();

                    UpdateResponse update = elasticsearchClient.update(updateRequest, Product.class);
                    productIds.add(update.id());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return productIds;
    }


    private List<ProductResponseDTO> searchProductByCriteria(String currentCategoryId) {
        List<ProductResponseDTO> productResponseDTOS = new ArrayList<>();
        try {
            SearchResponse<Product> search = elasticsearchClient.search(s -> s.index("productindex")
                    .size(1).query(q -> q.term(t -> t.field("category.keyword")
                    .value(currentCategoryId))), Product.class);

            log.info(" searchProductByCriteria .. result size...>{}", search.hits().hits().size());
            List<Hit<Product>> productHit = search.hits().hits();
            for (Hit<Product> hit : productHit) {
                ProductResponseDTO productResponseDTO = ProductResponseDTO.builder().product(hit.source()).indexId(hit.id()).build();
                productResponseDTOS.add(productResponseDTO);
            }
            return productResponseDTOS;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
