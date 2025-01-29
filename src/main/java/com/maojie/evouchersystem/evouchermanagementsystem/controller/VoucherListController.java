package com.maojie.evouchersystem.evouchermanagementsystem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;
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
    public ResponseEntity<VoucherList> createVoucher(@RequestHeader("Authorization") String jwt, @RequestBody VoucherList voucherList) throws Exception{
        List<VoucherList> isVoucherListExist = voucherListRepository.findByVoucherTitle(voucherList.getVoucherTitle());

        if(isVoucherListExist != null && isVoucherListExist.size()>0) {
            throw new Exception("This voucher is exist, please change the title of the voucher");
        }

        Owner owner = ownerService.findOwnerByJwt(jwt);
        VoucherList newVoucherList = voucherListService.createVoucherList(owner, voucherList);

        return new ResponseEntity<>(newVoucherList, HttpStatus.CREATED);
    }

    @PostMapping("/voucherList/updateVoucherList")
    public ResponseEntity<VoucherList> updateVoucher(@RequestHeader("Authorization") String jwt, @RequestBody VoucherList voucherList) throws Exception{
        List<VoucherList> isVoucherListExist = voucherListRepository.findByVoucherTitle(voucherList.getVoucherTitle());

        if(isVoucherListExist == null || isVoucherListExist.isEmpty()) {
            throw new Exception("This voucher is not exist, please create a new voucher");
        }

        if(isVoucherListExist.get(0).getStatus() != DBStatus.ACTIVE) {
            throw new Exception("This voucher is no longer available, unable to edit the voucher");
        }

        Owner owner = ownerService.findOwnerByJwt(jwt);
        VoucherList updatedVoucherList = voucherListService.updateVoucherList(owner, voucherList);

        return new ResponseEntity<>(updatedVoucherList, HttpStatus.ACCEPTED);
    }

    @GetMapping("/voucherList/retrieveListOfVoucherViaOwner")
    public ResponseEntity<List<VoucherList>> retrieveVoucherList(@RequestHeader("Authorization") String jwt) throws Exception{
        Owner owner = ownerService.findOwnerByJwt(jwt);
        List<VoucherList> retrieveVoucherLists = voucherListService.retrieveVoucherListByOwner(owner);
        return new ResponseEntity<>(retrieveVoucherLists, HttpStatus.OK);
    }

    @DeleteMapping("/voucherList/removeVoucherList")
    public ResponseEntity<String> removeVoucherList(@RequestHeader("Authorization") String jwt, @RequestBody VoucherList voucherList) throws Exception{
        Optional<VoucherList> isVoucherListExist = voucherListRepository.findById(voucherList.getId());

        if(isVoucherListExist == null || isVoucherListExist.isEmpty()) {
            throw new Exception("This voucher is not exist, unable to remove the voucher");
        }

        voucherListService.removeVoucherList(isVoucherListExist.get());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }




}
