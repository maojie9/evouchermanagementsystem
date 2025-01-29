package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;


import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;
import com.maojie.evouchersystem.evouchermanagementsystem.model.PaymentMethodDiscount;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.PaymentMethodDiscountRepository;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.VoucherListRepository;

@RestController
public class VoucherListServiceImpl implements VoucherListService{
    
    @Autowired
    private VoucherListRepository voucherListRepository;

    @Autowired
    private PaymentMethodDiscountRepository paymentMethodDiscountRepository;

    @Override
    @Transactional
    public VoucherList createVoucherList(Owner owner, VoucherList voucherList) throws Exception {
        List<VoucherList> isVoucherListExist = voucherListRepository.findByVoucherTitle(voucherList.getVoucherTitle());

        if(isVoucherListExist != null && isVoucherListExist.size()>0) {
            throw new Exception("This voucher is exist, please change the title of the voucher");
        }

        VoucherList newVoucherList = new VoucherList();

        newVoucherList.setOwner(owner);

        newVoucherList.setVoucherTitle(voucherList.getVoucherTitle());
        newVoucherList.setVoucherDesctiption(voucherList.getVoucherDesctiption());
        newVoucherList.setVoucherExpiryDate(voucherList.getVoucherExpiryDate());
        newVoucherList.setVoucherImage(voucherList.getVoucherImage());
        newVoucherList.setVoucherAmount(voucherList.getVoucherAmount());
        newVoucherList.setVoucherQuantity(voucherList.getVoucherQuantity());
        newVoucherList.setVoucherLimitPerCustomer(voucherList.getVoucherLimitPerCustomer());
        newVoucherList.setVoucherGiftLimitPerCustomer(voucherList.getVoucherGiftLimitPerCustomer());

        newVoucherList.setPaymentMethodDiscounts(voucherList.getPaymentMethodDiscounts());
        newVoucherList.setStatus(DBStatus.ACTIVE);

        VoucherList savedVoucherList = voucherListRepository.save(newVoucherList);

        for(PaymentMethodDiscount pmd : newVoucherList.getPaymentMethodDiscounts()){
            pmd.setVoucherList(savedVoucherList);
        }
        paymentMethodDiscountRepository.saveAll(newVoucherList.getPaymentMethodDiscounts());

        return savedVoucherList;

    }

    @Override
    @Transactional
    public VoucherList updateVoucherList(Owner owner, VoucherList voucherList) throws Exception {
        List<VoucherList> isVoucherListExist = voucherListRepository.findByVoucherTitle(voucherList.getVoucherTitle());
        if(isVoucherListExist == null || isVoucherListExist.isEmpty()) {
            throw new Exception("This voucher is not exist, please create a new voucher");
        }
        if(isVoucherListExist.get(0).getStatus() != DBStatus.ACTIVE) {
            throw new Exception("This voucher is no longer available, unable to edit the voucher");
        }

        VoucherList newVoucherList = new VoucherList();

        newVoucherList.setOwner(owner);
        newVoucherList.setId(voucherList.getId());

        newVoucherList.setVoucherTitle(voucherList.getVoucherTitle());
        newVoucherList.setVoucherDesctiption(voucherList.getVoucherDesctiption());
        newVoucherList.setVoucherExpiryDate(voucherList.getVoucherExpiryDate());
        newVoucherList.setVoucherImage(voucherList.getVoucherImage());
        newVoucherList.setVoucherAmount(voucherList.getVoucherAmount());
        newVoucherList.setVoucherQuantity(voucherList.getVoucherQuantity());
        newVoucherList.setVoucherLimitPerCustomer(voucherList.getVoucherLimitPerCustomer());
        newVoucherList.setVoucherGiftLimitPerCustomer(voucherList.getVoucherGiftLimitPerCustomer());

        newVoucherList.setPaymentMethodDiscounts(voucherList.getPaymentMethodDiscounts());
        newVoucherList.setStatus(DBStatus.ACTIVE);

        VoucherList savedVoucherList = voucherListRepository.save(newVoucherList);

        for(PaymentMethodDiscount pmd : newVoucherList.getPaymentMethodDiscounts()){
            pmd.setVoucherList(savedVoucherList);
        }
        paymentMethodDiscountRepository.saveAll(newVoucherList.getPaymentMethodDiscounts());
        
        return savedVoucherList;
    }

    @Override
    public List<VoucherList> retrieveVoucherListByOwner(Owner owner) {
        return voucherListRepository.findByOwner(owner);
    }

    @Override
    public VoucherList removeVoucherList(VoucherList voucherList) throws Exception {
         Optional<VoucherList> isVoucherListExist = voucherListRepository.findById(voucherList.getId());

        if(isVoucherListExist == null || isVoucherListExist.isEmpty()) {
            throw new Exception("This voucher is not exist, unable to remove the voucher");
        }
        
        voucherList.setStatus(DBStatus.INACTIVE);
        return voucherListRepository.save(voucherList);

    }

    @Override
    public List<VoucherList> retrieveActiveVoucherListForCustomer() {
        return voucherListRepository.findByVoucherExpiryDateAfterAndStatus(new Date(),DBStatus.ACTIVE);
    }


}
