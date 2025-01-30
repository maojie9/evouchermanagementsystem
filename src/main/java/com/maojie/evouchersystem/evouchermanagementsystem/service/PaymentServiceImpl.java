package com.maojie.evouchersystem.evouchermanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Sorder;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.OrderRepository;
import com.maojie.evouchersystem.evouchermanagementsystem.response.PaymentResponse;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PromoCodeService promoCodeService;
    @Autowired
    CustomerService customerService;

    @Override
    public Sorder createOrder(Customer customer, Sorder order) {
        Sorder newOrder = new Sorder();

        newOrder.setCustomer(customer);
        newOrder.setPaymentMethodDiscount(order.getPaymentMethodDiscount());

        newOrder.setNoOfVoucher(order.getNoOfVoucher());
        newOrder.setPurchasedPrice(order.getPurchasedPrice());
        newOrder.setTotalPurchasedPrice(order.getTotalPurchasedPrice());

        return orderRepository.save(newOrder);
    }

    @Override
    public Sorder getOrderById(Sorder order) throws Exception {
        return orderRepository.findById(order.getId()).orElseThrow(() -> new Exception("Unable to fund the order"));
    }

    @Override
    public Boolean proceedPaymentOrder(Sorder order, String paymentId) throws Exception { //Payment ID is a dummy payment ID
        // TODO Dummy process the payment

        // Trigger PromoCode Management System
        Sorder updatedOrder = orderRepository.findById(order.getId()).orElseThrow(() -> new Exception("Unable to fund the order"));
        Customer customer = customerService.findCustomerById(updatedOrder.getCustomer().getId());

        return promoCodeService.createPromoCode(customer, updatedOrder);

        // TODO Add the refund procedure if the order is rejected

    }

    // A dummy payment link
    @Override
    public PaymentResponse createDefaultPaymentLink(Sorder order) throws Exception {
        // Dummy implementation and simulate what happened after making the payment
       // paymentService.proceedPaymentOrder(order, "Dummy Payment ID");

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setUrl("");
        return paymentResponse;
    }

}
