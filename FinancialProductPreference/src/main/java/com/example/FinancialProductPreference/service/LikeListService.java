package com.example.FinancialProductPreference.service;

import com.example.FinancialProductPreference.entity.LikeList;
import com.example.FinancialProductPreference.repository.LikeListRepository;
import com.example.FinancialProductPreference.repository.ProductRepository;
import com.example.FinancialProductPreference.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeListService {

	private final LikeListRepository likeListRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;

    @Transactional
	public LikeList createLike(LikeList like) {
		String userId = like.getUser() != null ? like.getUser().getUserId() : null;
		Long productId = like.getProduct() != null ? like.getProduct().getId() : null;

		if (userId == null || userRepository.findById(userId).isEmpty()) {
			throw new IllegalArgumentException("User not found: " + userId);
		}
		if (productId == null || productRepository.findById(productId).isEmpty()) {
			throw new IllegalArgumentException("Product not found: " + productId);
		}

		likeListRepository.addLikeList(userId, productId, like.getPurchaseQuantity(), like.getAccount());

        return likeListRepository.findTopByUser_UserIdOrderBySnDesc(userId);
	}

	public Optional<LikeList> getLikeById(Long sn) {
		return likeListRepository.findById(sn);
	}

	@Transactional
	public LikeList updateLike(LikeList like) {
		likeListRepository.updateLikeListProcedure(
			like.getSn(),
			like.getProduct().getId(),
			like.getPurchaseQuantity(),
			like.getAccount()
		);
		
		return likeListRepository.findById(like.getSn())
				.orElseThrow(() -> new RuntimeException("Failed to retrieve updated like"));
	}

	public List<LikeList> getLikesByUserId(String userId) {
		return likeListRepository.findByUser_UserId(userId);
	}

	public List<LikeList> getLikesByProductId(Long productId) {
		return productRepository.findById(productId)
				.map(likeListRepository::findByProduct)
				.orElse(List.of());
	}

	@Transactional
	public void deleteLike(Long sn) {
		likeListRepository.deleteById(sn);
	}

	@Transactional
	public void deleteLikesByUserId(String userId) {
		likeListRepository.deleteByUser_UserId(userId);
	}

}

