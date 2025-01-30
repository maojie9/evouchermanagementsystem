package com.maojie.evouchersystem.evouchermanagementsystem.service;


import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Sorder;
import com.maojie.evouchersystem.evouchermanagementsystem.response.PaymentResponse;

public interface PaymentService {
    Sorder createOrder(Customer customer, Sorder order);
    Sorder getOrderById(Sorder order) throws Exception;
    Boolean proceedPaymentOrder(Sorder order, String paymentId) throws Exception; //Payment ID is a dummy payment ID
    PaymentResponse createDefaultPaymentLink(Sorder order) throws Exception; // A dummy payment link

}
