package com.maojie.evouchersystem.evouchermanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.OrderHistory;
import com.maojie.evouchersystem.evouchermanagementsystem.model.TransferVoucher;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Voucher;
import com.maojie.evouchersystem.evouchermanagementsystem.service.CustomerService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.VoucherService;

@RestController
public class VoucherController {

    @Autowired
    private VoucherService voucherService;
    @Autowired
    private CustomerService customerService;


    @PostMapping("/api/voucher/transferVoucher")
    public ResponseEntity<String> transferVoucher(@RequestHeader("Authorization") String jwt, @RequestBody TransferVoucher order) throws Exception {
        Voucher voucher = voucherService.retrieveVoucherById(order.getVoucherId());
        if(voucher == null) {
            throw new Exception("This voucher is not a valid voucher, unable to transfer the transaction.");
        }
        if(voucher.getStatus() != DBStatus.ACTIVE) {
            throw new Exception("This voucher is not a valid voucher, voucher had been inactive, unable to transfer the transaction.");
        }

        Customer currentCustomer = customerService.findCustomerByJwt(jwt);
        Customer newCustomer = customerService.findCustomerByMobileNumberString(order.getNewCustomerBasedOnMobileNoString());
        voucherService.changeCustomer(voucher, currentCustomer, newCustomer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/api/voucher/listVoucherForCustomer")
    public ResponseEntity<OrderHistory> listVoucherForCustomer(@RequestHeader("Authorization") String jwt) throws Exception {
        Customer customer = customerService.findCustomerByJwt(jwt);
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setMainVoucher(voucherService.retrieveVoucherListByCustomer(customer));
        orderHistory.setVoucherFromOtherCustomer(voucherService.retrieveGiftedVoucherReceivedByCustomer(customer));

        return new ResponseEntity<>(orderHistory, HttpStatus.OK);
    }

    @PostMapping("/api/voucher/useVoucher")
    public ResponseEntity<String> utilizeHandler(
        @RequestHeader("Authorization") String jwt,
        @RequestBody Voucher voucher
        ) throws Exception 
    {
        Customer customer = customerService.findCustomerByJwt(jwt);
        voucherService.voucherUsed(customer, voucher);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
