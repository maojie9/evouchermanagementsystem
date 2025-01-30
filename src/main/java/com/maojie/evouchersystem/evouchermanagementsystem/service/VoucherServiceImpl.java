package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.domain.OrderStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Sorder;
import com.maojie.evouchersystem.evouchermanagementsystem.model.PaymentMethodDiscount;
import com.maojie.evouchersystem.evouchermanagementsystem.model.PromoCode;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Voucher;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.OrderRepository;
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
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Voucher> retrieveVoucherListByCustomer(Customer customer) {
        // Find the list of order first
        List<Voucher> vouchers = new ArrayList<>();
        List<Sorder> orderList = orderRepository.findByCustomer(customer);

        for(Sorder o : orderList){
            List<PromoCode> promoCodes = promoCodeRepository.findBySorder(o);
            for(PromoCode pc : promoCodes) {
                vouchers.addAll(voucherRepository.findByPromoCode(pc));
            }
        }
        return vouchers;

    }

    @Override
    public List<Voucher> retrieveGiftedVoucherReceivedByCustomer(Customer customer) {
        // Find the list of order that is not original from the customer
        List<Voucher> vouchers = new ArrayList<>();
        List<Sorder> orderList = orderRepository.findByCustomerNot(customer);

        for(Sorder o : orderList){
            List<PromoCode> promoCodes = promoCodeRepository.findBySorder(o);
            for(PromoCode pc : promoCodes) {
                vouchers.addAll(voucherRepository.findByCurrentCustomerAndPromoCode(customer, pc));
            }
        }
        return vouchers;

    }

    @Override
    public Voucher changeCustomer(Voucher voucher, Customer currentCustomer, Customer newCustomer) throws Exception {
        // Verify that the oldd customer can able to transfer first or not
        if(!currentCustomer.getMobileNoString().equals(voucher.getCurrentCustomer().getMobileNoString())) {
            throw new Exception("Unable to transfer the voucher as voucher had been transfered before");
        }

        // Obtain number of vouchers are transferred before
        Optional<VoucherList> voucherList = voucherListRepository.findById(voucher.getPromoCode().getSorder().getPaymentMethodDiscount().getVoucherList().getId());
        if(voucherList == null || 
            voucherList.isEmpty() || 
            voucherList.get().getStatus() != DBStatus.ACTIVE ||
            voucherList.get().getVoucherExpiryDate().compareTo(new Date()) < 0) {
                throw new Exception("Unable to transfer the voucher as voucher is unavailable");
            }

        if(voucherList.get().getVoucherGiftLimitPerCustomer() <=0) {
            throw new Exception("Unable to transfer the voucher as voucher is untransferable");
        }


        // Obtain number of vouchers are transfered before by the customer
        int numberOfVoucherAlreadyTransferedByCustomer = 0;
        List<PaymentMethodDiscount> paymentMethodDiscounts = voucherList.get().getPaymentMethodDiscounts();

        for(PaymentMethodDiscount pmd : paymentMethodDiscounts) {
            List<Sorder> numberOfOrdersPurchasedByCustomer = orderRepository.findByPaymentMethodDiscountAndStatusAndCustomer(pmd, OrderStatus.ACCEPTED, currentCustomer);
            for(Sorder o : numberOfOrdersPurchasedByCustomer) {
                List<PromoCode> promoCode = promoCodeRepository.findBySorder(o);
                if(promoCode!=null && !promoCode.isEmpty()) {
                    List<Voucher> vouchers = voucherRepository.findByCurrentCustomerNotAndPromoCode(currentCustomer, promoCode.get(0));
                    numberOfVoucherAlreadyTransferedByCustomer = numberOfVoucherAlreadyTransferedByCustomer + vouchers.size();
                }
            }
        }

        if(numberOfVoucherAlreadyTransferedByCustomer + 1 > voucherList.get().getVoucherGiftLimitPerCustomer()) {
            throw new Exception("Unable to process the transaction due to customer had use up the number of vouchers to transfer");
        }
        
        voucher.setCurrentCustomer(newCustomer);
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher voucherUsed(Customer customer, Voucher voucher) throws Exception {
        // Check the Voucher is it correct customer to use
        Optional<Voucher> assignVoucher = voucherRepository.findById(voucher.getId());
        if(assignVoucher.isEmpty()) {
            throw new Exception("Voucher not found");
        }
        if(!assignVoucher.get().getCurrentCustomer().getId().equals(customer.getId())) {
            throw new Exception("This customer is unauthorized to use this voucher");
        }
        
        if(voucher.getStatus() == DBStatus.ACTIVE) {
            voucher.setStatus(DBStatus.USED);
            return voucherRepository.save(voucher);
        }

        throw new Exception("Voucher had been utilized, unable to access to this voucher");
        
    }

    @Override
    public Voucher retrieveVoucherById(UUID voucherId) throws Exception {
        return voucherRepository.findById(voucherId).orElseThrow(() -> new Exception("Unable to fund the specific voucher"));
    }
    
}
