package com.example.FinancialProductPreference.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "like_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sn")
    private Long sn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "purchase_quantity", nullable = false)
    private Integer purchaseQuantity;

    @Column(name = "account", nullable = false, length = 50)
    private String account;

    @Column(name = "total_fee", nullable = false)
    private Float totalFee;

    @Column(name = "total_amount", nullable = false)
    private Float totalAmount;

}
