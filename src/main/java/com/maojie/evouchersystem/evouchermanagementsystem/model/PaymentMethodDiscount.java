package com.maojie.evouchersystem.evouchermanagementsystem.model;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class PaymentMethodDiscount  implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "voucherListId") // This is the foreign key column
    private VoucherList voucherList; // Payment method of the particualar list of voucher

    private int paymentMethod; // Enum of Payment Method
    private int paymentMethodDiscounts; // In percentage


}
