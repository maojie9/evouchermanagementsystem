package com.maojie.evouchersystem.evouchermanagementsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maojie.evouchersystem.evouchermanagementsystem.domain.OrderStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Sorder;
import com.maojie.evouchersystem.evouchermanagementsystem.model.PaymentMethodDiscount;

import java.util.List;

public interface OrderRepository extends JpaRepository<Sorder, UUID> {
    List<Sorder> findByPaymentMethodDiscountAndStatus(PaymentMethodDiscount paymentMethodDiscount, OrderStatus accepted);
    List<Sorder> findByPaymentMethodDiscountAndStatusAndCustomer(PaymentMethodDiscount paymentMethodDiscount, OrderStatus accepted,Customer customer);
    List<Sorder> findByCustomer(Customer customer);
    List<Sorder> findByCustomerNot(Customer customer);

}
