package com.example.FinancialProductPreference.repository;

import com.example.FinancialProductPreference.entity.LikeList;
import com.example.FinancialProductPreference.entity.Product;
import com.example.FinancialProductPreference.entity.User;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeListRepository extends JpaRepository<LikeList, Long> {

	List<LikeList> findByUser(User user);

	List<LikeList> findByProduct(Product product);

	List<LikeList> findByUser_UserId(String userId);

	void deleteByUser_UserId(String userId);

    LikeList findTopByUser_UserIdOrderBySnDesc(String userId);

    @Procedure(procedureName = "add_like_list")
    void addLikeList(
        String p_user_id,
        Long p_product_id,
        Integer p_quantity,
        String p_account
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "CALL update_like_list(:sn, :productId, :qty, :account)", 
        nativeQuery = true)
    void updateLikeListProcedure(
        @Param("sn") Long sn,
        @Param("productId") Long productId,
        @Param("qty") Integer qty,
        @Param("account") String account
    );
}

