package com.example.FinancialProductPreference.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@Column(name = "user_id", length = 50)
	private String userId;

	@Column(name = "user_name", nullable = false, length = 255)
	private String userName;

	@Column(name = "email", nullable = false, unique = true, length = 255)
	private String email;

	@Column(name = "account", nullable = false, length = 50)
	private String account;

}
