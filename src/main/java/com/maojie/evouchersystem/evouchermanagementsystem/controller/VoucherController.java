package com.maojie.evouchersystem.evouchermanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.model.AfterPurchaseVoucher;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;
import com.maojie.evouchersystem.evouchermanagementsystem.service.CustomerService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.PromoCodeService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.VoucherListService;

@RestController
public class VoucherController {

    @Autowired
    private VoucherListService voucherListService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PromoCodeService promoCodeService;

    @PostMapping("/api/voucher/afterPurchase")
    public ResponseEntity<String> afterPurchaseVoucher(@RequestHeader("Authorization") String jwt, @RequestBody AfterPurchaseVoucher order) throws Exception {
        VoucherList voucherList = voucherListService.retrieveVoucherById(order.getVoucherId());
        if(voucherList == null) {
            throw new Error("This voucher is not a valid voucher, unable to process the transaction. Money will be refunded");
        }
        if(voucherList.getVoucherLimitPerCustomer() < order.getNumberOfVoucherPurchased()) {
            throw new Error("Unable to process the transaction due to number of voucher customer allocated is more than the number of vouchers customer can buy. Money will be refunded");
        }

        Customer customer = customerService.findCustomerByJwt(jwt);
        promoCodeService.createPromoCode(customer, voucherList, order.getNumberOfVoucherPurchased());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /* 


    // Temporary testing purposes
    @PostMapping("/api/voucherList/createVoucherList")
    public ResponseEntity<VoucherList> createVoucher(@RequestHeader("Authorization") String jwt, @RequestBody VoucherList voucherList) throws Exception{
        Owner owner = ownerService.findOwnerByJwt(jwt);
        VoucherList newVoucherList = voucherListService.createVoucherList(owner, voucherList);

        return new ResponseEntity<>(newVoucherList, HttpStatus.CREATED);
    }

    @PostMapping("/api/voucherList/updateVoucherList")
    public ResponseEntity<VoucherList> updateVoucher(@RequestHeader("Authorization") String jwt, @RequestBody VoucherList voucherList) throws Exception{
        Owner owner = ownerService.findOwnerByJwt(jwt);
        VoucherList updatedVoucherList = voucherListService.updateVoucherList(owner, voucherList);

        return new ResponseEntity<>(updatedVoucherList, HttpStatus.ACCEPTED);
    }

    @GetMapping("/api/voucherList/retrieveListOfVoucherViaOwner")
    public ResponseEntity<List<VoucherList>> retrieveVoucherList(@RequestHeader("Authorization") String jwt) throws Exception{
        Owner owner = ownerService.findOwnerByJwt(jwt);
        List<VoucherList> retrieveVoucherLists = voucherListService.retrieveVoucherListByOwner(owner);
        return new ResponseEntity<>(retrieveVoucherLists, HttpStatus.OK);
    }

    @DeleteMapping("/api/voucherList/removeVoucherList")
    public ResponseEntity<String> removeVoucherList(@RequestHeader("Authorization") String jwt, @RequestBody VoucherList voucherList) throws Exception{
        voucherListService.removeVoucherList(voucherList);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/api/voucherList/retrieveListOfAvailableVoucher")
    public ResponseEntity<List<VoucherList>> retrieveActiveVoucherList(@RequestHeader("Authorization") String jwt) throws Exception{
        List<VoucherList> retrieveVoucherLists = voucherListService.retrieveActiveVoucherListForCustomer();
        return new ResponseEntity<>(retrieveVoucherLists, HttpStatus.OK);
    }


*/


}
