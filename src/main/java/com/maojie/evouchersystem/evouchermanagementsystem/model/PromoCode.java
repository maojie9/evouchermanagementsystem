package com.maojie.evouchersystem.evouchermanagementsystem.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class PromoCode implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    
    @Column(unique=true)
    private String promoCode; // PromoCode

    @JsonIncludeProperties({"mobileNoString", "name"})
    @ManyToOne
    @JoinColumn(name = "customerId") // This is the foreign key column
    private Customer customer; // Customer that own this promo code 
    
    @JsonIncludeProperties({"voucherTitle"})
    @ManyToOne
    @JoinColumn(name = "voucherListId") // This is the foreign key column
    private VoucherList voucherList; // Customer that own this voucher

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private DBStatus status;

    @OneToMany(mappedBy = "promoCode")
    private List<Voucher> vouchers;


}
