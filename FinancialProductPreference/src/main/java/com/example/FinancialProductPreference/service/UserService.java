package com.example.FinancialProductPreference.service;

import com.example.FinancialProductPreference.entity.LikeList;
import com.example.FinancialProductPreference.entity.User;
import com.example.FinancialProductPreference.repository.LikeListRepository;
import com.example.FinancialProductPreference.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final LikeListRepository likeListRepository;

	public Optional<User> getUserById(String userId) {
		return userRepository.findById(userId);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public List<LikeList> getLikesByUserId(String userId) {
		return likeListRepository.findByUser_UserId(userId);
	}

}

