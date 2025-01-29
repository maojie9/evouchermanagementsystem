package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.PromoCode;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Voucher;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.PromoCodeRepository;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.VoucherListRepository;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.VoucherRepository;

@RestController
public class VoucherServiceImpl implements VoucherService{

    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private PromoCodeRepository promoCodeRepository;
    @Autowired
    private VoucherListRepository voucherListRepository;

    @Override
    public List<Voucher> retrieveVoucherListByCurrentCustomer(Customer currentCustomer) {
        return voucherRepository.findByCurrentCustomer(currentCustomer);
    }

    @Override
    public Voucher changeCustomer(Voucher voucher, Customer currentCustomer, Customer newCustomer) throws Exception {
        // Verify that the oldd customer can able to transfer first or not
        if(!currentCustomer.getMobileNoString().equals(voucher.getCurrentCustomer().getMobileNoString())) {
            throw new Exception("Unable to transfer the voucher as voucher had been transfered before");
        }

        // Obtain number of vouchers are transferred before
        PromoCode appointedPromoCode = promoCodeRepository.findById(voucher.getPromoCode().getId()).get();
        if(appointedPromoCode == null || appointedPromoCode.getStatus() != DBStatus.ACTIVE) {
            throw new Exception("Unable to transfer the voucher as voucher is unavailable");
        }

        Optional<VoucherList> appointedVoucherList = voucherListRepository.findById(appointedPromoCode.getVoucherList().getId());
        if(appointedVoucherList == null ||  appointedVoucherList.get().getStatus() != DBStatus.ACTIVE) {
            throw new Exception("Unable to transfer the voucher as voucher is unavailable");
        }
        if(appointedVoucherList.get().getVoucherGiftLimitPerCustomer() <=0) {
            throw new Exception("Unable to transfer the voucher as voucher is untransferable");
        }

        int numberOfVoucherAlreadyTransfered = 0;
        
        List<PromoCode> numberOfPromoCodePurchasedByCurrentCustomer = promoCodeRepository.findByVoucherListAndCustomer(appointedVoucherList.get(), currentCustomer);
        for(PromoCode pc : numberOfPromoCodePurchasedByCurrentCustomer) {
            numberOfVoucherAlreadyTransfered = numberOfVoucherAlreadyTransfered + voucherRepository.findByCurrentCustomerNotAndPromoCode(currentCustomer, pc).size();
        }

        if(numberOfVoucherAlreadyTransfered + 1 > appointedVoucherList.get().getVoucherGiftLimitPerCustomer()) {
            throw new Error("Unable to process the transaction due to customer had use up the number of vouchers to transfer");
        }
        
        voucher.setCurrentCustomer(newCustomer);
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher voucherUsed(Voucher voucher) throws Exception {
        if(voucher.getStatus() == DBStatus.ACTIVE) {
            voucher.setStatus(DBStatus.USED);
            return voucherRepository.save(voucher);
        }

        throw new Exception("Voucher had been utilized, unable to access to this voucher");
        
    }

    @Override
    public Voucher retrieveVoucherById(UUID voucherId) {
        return voucherRepository.findById(voucherId).get();
    }

    
}
