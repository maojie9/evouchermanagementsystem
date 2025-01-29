package com.maojie.evouchersystem.evouchermanagementsystem.domain;

/**
 * This is the status field for all the tables in the database
 * 
 */
public enum DBStatus {
    ACTIVE ,            // For all tables
    EXPIRED,            // For Voucher which is explecit expired
    REMOVED,            // For Customer and Users delete the account
    DELETEDBYCUSTOMER,  // If Customer or owner removed, it will delete the vouchers and promo code too
    INACTIVE,           // Inactive is for voucherList table only
    USED                // Used for vouchers had been used

}
