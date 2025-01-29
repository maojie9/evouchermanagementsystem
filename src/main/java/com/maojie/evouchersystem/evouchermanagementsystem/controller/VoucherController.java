package com.maojie.evouchersystem.evouchermanagementsystem.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.model.AfterPurchaseVoucher;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.OrderHistory;
import com.maojie.evouchersystem.evouchermanagementsystem.model.TransferVoucher;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Voucher;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;
import com.maojie.evouchersystem.evouchermanagementsystem.service.CustomerService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.PromoCodeService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.VoucherListService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.VoucherService;

@RestController
public class VoucherController {

    @Autowired
    private VoucherListService voucherListService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PromoCodeService promoCodeService;

    @PostMapping("/api/voucher/afterPurchase")
    public ResponseEntity<String> afterPurchaseVoucher(@RequestHeader("Authorization") String jwt, @RequestBody AfterPurchaseVoucher order) throws Exception {
        VoucherList voucherList = voucherListService.retrieveVoucherListById(order.getVoucherListId());
        if(voucherList == null) {
            throw new Error("This voucher is not a valid voucher, unable to process the transaction. Money will be refunded");
        }
        if(voucherList.getVoucherExpiryDate().compareTo(new Date()) < 0) {
            throw new Error("This voucher is not a valid voucher, voucher had been expired, unable to process the transaction. Money will be refunded");
        }
        if(voucherList.getStatus() != DBStatus.ACTIVE) {
            throw new Error("This voucher is not a valid voucher, voucher had been inactive, unable to process the transaction. Money will be refunded");
        }
        if(voucherList.getVoucherLimitPerCustomer() < order.getNumberOfVoucherPurchased()) {
            throw new Error("Unable to process the transaction due to number of voucher customer allocated is more than the number of vouchers customer can buy. Money will be refunded");
        }

        Customer customer = customerService.findCustomerByJwt(jwt);
        promoCodeService.createPromoCode(customer, voucherList, order.getNumberOfVoucherPurchased());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/voucher/transferVoucher")
    public ResponseEntity<String> transferVoucher(@RequestHeader("Authorization") String jwt, @RequestBody TransferVoucher order) throws Exception {
        Voucher voucher = voucherService.retrieveVoucherById(order.getVoucherId());
        if(voucher == null) {
            throw new Error("This voucher is not a valid voucher, unable to transfer the transaction.");
        }
        if(voucher.getStatus() != DBStatus.ACTIVE) {
            throw new Error("This voucher is not a valid voucher, voucher had been inactive, unable to transfer the transaction.");
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


}
