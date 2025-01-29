package com.maojie.evouchersystem.evouchermanagementsystem.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class VoucherList implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    
    @Column(unique=true)
    private String voucherTitle; // Title of the voucher
    
    @Lob
    private String voucherDesctiption; // Description of the voucher

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date voucherExpiryDate; // Expiry date of the voucher

    @Lob
    private byte[] voucherImage; // BLOB data to store the image of the voucher

    private double voucherAmount; // What is the value of the voucher
    private int voucherQuantity; // How many voucher are available 
    
    @JsonIncludeProperties({"id", "userName"})
    @ManyToOne
    @JoinColumn(name = "voucherOwner") // This is the foreign key column
    private Owner owner; // Owner of the voucher ie. Who create the voucher

    private int voucherLimitPerCustomer; // How many voucher each customer at most can buy 
    private int voucherGiftLimitPerCustomer; // How many voucher each customer can give out

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private DBStatus status;

    @OneToMany(mappedBy = "voucherList")
    private List<PaymentMethodDiscount> paymentMethodDiscounts;


}
