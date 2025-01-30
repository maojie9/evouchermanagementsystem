package com.maojie.evouchersystem.evouchermanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Sorder;
import com.maojie.evouchersystem.evouchermanagementsystem.response.PaymentResponse;
import com.maojie.evouchersystem.evouchermanagementsystem.service.CustomerService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.PaymentService;

@RestController
public class PaymentController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private PaymentService paymentService;


    @PostMapping("/api/payment")
    public ResponseEntity<PaymentResponse> paymentHandler(
        @RequestHeader("Authorization") String jwt,
        @RequestBody Sorder sorder
        ) throws Exception 
    {
        Customer customer = customerService.findCustomerByJwt(jwt);
        Sorder savedOrder = paymentService.createOrder(customer, sorder);

        //PaymentResponse paymentResponse = paymentService.createDefaultPaymentLink(savedOrder);
        paymentService.proceedPaymentOrder(savedOrder, "Dummy Payment ID");

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setUrl("");

        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }
}
