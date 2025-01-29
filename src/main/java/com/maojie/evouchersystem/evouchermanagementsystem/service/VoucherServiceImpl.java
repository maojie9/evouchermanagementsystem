package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Voucher;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.VoucherRepository;

@RestController
public class VoucherServiceImpl implements VoucherService{

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public List<Voucher> retrieveVoucherListByCurrentCustomer(Customer currentCustomer) {
        return voucherRepository.findByCurrentCustomer(currentCustomer);
    }

    @Override
    public Voucher changeCustomer(Voucher voucher, Customer newCustomer) {
        voucher.setCurrentCustomer(newCustomer);
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher voucherUsed(Voucher voucher) {
        voucher.setStatus(DBStatus.USED);
        return voucherRepository.save(voucher);
    }

    
}
