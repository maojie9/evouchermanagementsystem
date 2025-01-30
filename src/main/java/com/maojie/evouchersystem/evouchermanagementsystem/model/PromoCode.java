package com.maojie.evouchersystem.evouchermanagementsystem.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class PromoCode implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    
    @Column(unique=true)
    private String promoCode; // PromoCode

    @OneToOne
    @JoinColumn(name = "orderId")
    private Sorder sorder;

    @OneToMany(mappedBy = "promoCode")
    private List<Voucher> vouchers;


}
