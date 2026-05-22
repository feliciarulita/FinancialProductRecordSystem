package com.example.FinancialProductPreference.controller;

import com.example.FinancialProductPreference.entity.LikeList;
import com.example.FinancialProductPreference.entity.Product;
import com.example.FinancialProductPreference.entity.User;
import com.example.FinancialProductPreference.service.LikeListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173"})
public class LikeListController {

	private final LikeListService likeListService;
	private final com.example.FinancialProductPreference.repository.UserRepository userRepository;
	private final com.example.FinancialProductPreference.repository.ProductRepository productRepository;
	
	private static final Pattern ACCOUNT_PATTERN = Pattern.compile("^[a-zA-Z0-9_\\-]{1,50}$");
	private static final int MAX_ACCOUNT_LENGTH = 50;
	private static final int MAX_QUANTITY = 1000000;

	private void validateAccount(String account) {
		if (account != null && !account.trim().isEmpty()) {
			if (account.length() > MAX_ACCOUNT_LENGTH) {
				throw new IllegalArgumentException("Account length exceeds maximum of " + MAX_ACCOUNT_LENGTH);
			}
			if (!ACCOUNT_PATTERN.matcher(account).matches()) {
				throw new IllegalArgumentException("Account contains invalid characters. Use only alphanumeric, hyphen, and underscore.");
			}
		}
	}
	
	private void validatePurchaseQuantity(Integer quantity) {
		if (quantity == null || quantity <= 0) {
			throw new IllegalArgumentException("Purchase quantity must be greater than 0");
		}
		if (quantity > MAX_QUANTITY) {
			throw new IllegalArgumentException("Purchase quantity exceeds maximum of " + MAX_QUANTITY);
		}
	}

	@PostMapping
	public ResponseEntity<LikeList> createLike(@RequestBody LikeRequest dto) {
		validatePurchaseQuantity(dto.getPurchaseQuantity());
		validateAccount(dto.getAccount());
		
		User user = null;
		user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalArgumentException("Default user not found"));

		Product product = productRepository.findById(dto.getProductId())
				.orElseThrow(() -> new IllegalArgumentException("Product not found: " + dto.getProductId()));

		String account = (dto.getAccount() != null && !dto.getAccount().trim().isEmpty()) 
			? dto.getAccount() 
			: user.getAccount();

		LikeList like = LikeList.builder()
				.user(user)
				.product(product)
				.purchaseQuantity(dto.getPurchaseQuantity())
				.account(account)
				.build();


		LikeList saved = likeListService.createLike(like);
		return ResponseEntity.created(URI.create("/api/likes/" + saved.getSn()))
                .body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<LikeList> updateLike(@PathVariable Long id, @RequestBody LikeRequest dto) {
		validatePurchaseQuantity(dto.getPurchaseQuantity());
		validateAccount(dto.getAccount());
		
		LikeList existing = likeListService.getLikeById(id)
				.orElseThrow(() -> new IllegalArgumentException("Like not found: " + id));

		if (dto.getProductId() != null) {
			Product product = productRepository.findById(dto.getProductId())
					.orElseThrow(() -> new IllegalArgumentException("Product not found: " + dto.getProductId()));
			existing.setProduct(product);
		}

		if (dto.getPurchaseQuantity() != null) {
			existing.setPurchaseQuantity(dto.getPurchaseQuantity());
		}

		String account = (dto.getAccount() != null && !dto.getAccount().trim().isEmpty()) 
			? dto.getAccount() 
			: existing.getUser().getAccount();
		existing.setAccount(account);

		LikeList updated = likeListService.updateLike(existing);
		return ResponseEntity.ok(updated);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LikeList> getLike(@PathVariable Long id) {
		return likeListService.getLikeById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<LikeList>> getLikesByUser(@PathVariable String userId) {
		return ResponseEntity.ok(likeListService.getLikesByUserId(userId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLike(@PathVariable Long id) {
		likeListService.deleteLike(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	public static class LikeRequest {
		private String userId;
		private Long productId;
		private Integer purchaseQuantity;
		private String account;
		private Float totalFee;
		private Float totalAmount;

		public String getUserId() { return userId; }
		public void setUserId(String userId) { this.userId = userId; }
		public Long getProductId() { return productId; }
		public void setProductId(Long productId) { this.productId = productId; }
		public Integer getPurchaseQuantity() { return purchaseQuantity; }
		public void setPurchaseQuantity(Integer purchaseQuantity) { this.purchaseQuantity = purchaseQuantity; }
		public String getAccount() { return account; }
		public void setAccount(String account) { this.account = account; }
		public Float getTotalFee() { return totalFee; }
		public void setTotalFee(Float totalFee) { this.totalFee = totalFee; }
		public Float getTotalAmount() { return totalAmount; }
		public void setTotalAmount(Float totalAmount) { this.totalAmount = totalAmount; }
	}

}

