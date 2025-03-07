package com.maojie.evouchersystem.evouchermanagementsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;
import com.maojie.evouchersystem.evouchermanagementsystem.service.CustomerService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.OwnerService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.VoucherListService;

@RestController
public class VoucherListController {

    @Autowired
    private OwnerService ownerService;
    @Autowired
    private VoucherListService voucherListService;
    @Autowired
    private CustomerService customerService;

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
        Owner owner = ownerService.findOwnerByJwt(jwt);
        voucherListService.removeVoucherList(voucherList, owner);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/api/voucherList/retrieveListOfAvailableVoucher")
    public ResponseEntity<List<VoucherList>> retrieveActiveVoucherList(@RequestHeader("Authorization") String jwt) throws Exception{
        Customer customer = customerService.findCustomerByJwt(jwt);
        if(customer == null) {
            throw new Exception("Only registered customer can access to the eStore");
        }
        List<VoucherList> retrieveVoucherLists = voucherListService.retrieveActiveVoucherListForCustomer();
        return new ResponseEntity<>(retrieveVoucherLists, HttpStatus.OK);
    }

    @GetMapping("/api/voucherList/retrieveVoucherDetails")
    public ResponseEntity<VoucherList> retrieveActiveVoucherList(@RequestHeader("Authorization") String jwt, @RequestBody VoucherList voucherList) throws Exception{
        Customer customer = customerService.findCustomerByJwt(jwt);
        if(customer == null) {
            throw new Exception("Only registered customer can access to the eStore");
        }
        VoucherList voucherListResponse = voucherListService.retrieveVoucherListById(voucherList.getId());
        return new ResponseEntity<>(voucherListResponse, HttpStatus.OK);
    }




}
