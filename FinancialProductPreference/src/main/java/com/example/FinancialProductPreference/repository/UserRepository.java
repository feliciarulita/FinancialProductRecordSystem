package com.example.FinancialProductPreference.repository;

import com.example.FinancialProductPreference.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	java.util.Optional<User> findByAccount(String account);

}
