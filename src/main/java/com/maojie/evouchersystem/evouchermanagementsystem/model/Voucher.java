package com.maojie.evouchersystem.evouchermanagementsystem.model;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Voucher implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @JsonIncludeProperties({"mobileNoString", "name"})
    @ManyToOne
    @JoinColumn(name = "currentCustomerId") // This is the foreign key column
    private Customer currentCustomer; // Which customer hold the posession for the voucher

    @JsonIncludeProperties({"promoCode", "order"})
    @ManyToOne
    @JoinColumn(name = "promoCodeId") // This is the foreign key column
    private PromoCode promoCode; // Customer that own this voucher via promo code

    @Enumerated(EnumType.STRING)
    private DBStatus status;

}
