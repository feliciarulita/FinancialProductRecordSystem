package com.example.FinancialProductPreference.service;

import com.example.FinancialProductPreference.entity.LikeList;
import com.example.FinancialProductPreference.entity.Product;
import com.example.FinancialProductPreference.repository.LikeListRepository;
import com.example.FinancialProductPreference.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final LikeListRepository likeListRepository;

	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public List<LikeList> getLikesByProductId(Long productId) {
		return productRepository.findById(productId)
				.map(product -> likeListRepository.findByProduct(product))
				.orElse(List.of());
	}

}

