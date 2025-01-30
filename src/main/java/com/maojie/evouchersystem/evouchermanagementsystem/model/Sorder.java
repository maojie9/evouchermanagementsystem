package com.maojie.evouchersystem.evouchermanagementsystem.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.maojie.evouchersystem.evouchermanagementsystem.domain.OrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Sorder implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @JsonIncludeProperties({"mobileNoString", "name"})
    @ManyToOne
    @JoinColumn(name = "customerId") // This is the foreign key column
    private Customer customer; // Customer that own this promo code 
    
    @JsonIncludeProperties({"id, paymentMethod, voucherList"})
    @ManyToOne
    @JoinColumn(name = "paymentMethodDiscountId") // This is the foreign key column
    private PaymentMethodDiscount paymentMethodDiscount; // Which payment method for the order

    @Lob
    private String reason; // Desribe the rejection reason

    // These field are not related to the latest voucherList managed by owner because it just a screenshot only
    private Date pruchasedDate; // When the order purchased
    private int noOfVoucher; // Number of vouchers ordered
    private double purchasedPrice; //Unit purchased price at that time
    private double totalPurchasedPrice; // total price for the order

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(mappedBy = "sorder")
    private PromoCode promoCode;

}
