package com.maojie.evouchersystem.evouchermanagementsystem.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the status field for all the tables in the database
 * 
 */
public enum DBStatus {
    ACTIVE(1),      // For all tables
    EXPIRED(2),     // For Voucher which is explecit expired
    REMOVED(3),     // For Customer and Users delete the account


    ;


    public final int statusCode;

    private DBStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    private static final Map<Integer, DBStatus> BY_STATUSCODE = new HashMap<>();

    static {
        for (DBStatus e: values()) {
            BY_STATUSCODE.put(e.statusCode, e);
        }
    }

    public static DBStatus valueOfStatusCode(int statusCode) {
        return BY_STATUSCODE.get(statusCode);
    }

}
