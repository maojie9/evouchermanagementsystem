package com.maojie.evouchersystem.evouchermanagementsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.VoucherListRepository;
import com.maojie.evouchersystem.evouchermanagementsystem.service.OwnerService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.VoucherListService;

@RestController
public class VoucherListController {
    
    @Autowired
    private VoucherListRepository voucherListRepository;

    @Autowired
    private OwnerService ownerService;
    @Autowired
    private VoucherListService voucherListService;

    // Temporary testing purposes
    @PostMapping("/voucherList/createVoucherList")
    public ResponseEntity<VoucherList> registerOwner(@RequestHeader("Authorization") String jwt, @RequestBody VoucherList voucherList) throws Exception{
        List<VoucherList> isVoucherListExist = voucherListRepository.findByVoucherTitle(voucherList.getVoucherTitle());

        if(isVoucherListExist != null && isVoucherListExist.size()>0) {
            throw new Exception("This voucher is exist, please change the title of the voucher");
        }

        Owner owner = ownerService.findOwnerByJwt(jwt);
        VoucherList newVoucherList = voucherListService.createVoucherList(owner, voucherList);

        return new ResponseEntity<>(newVoucherList, HttpStatus.CREATED);
    }

}
