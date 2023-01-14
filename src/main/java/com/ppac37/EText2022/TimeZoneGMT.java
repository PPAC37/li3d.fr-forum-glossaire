/*
 */
package com.ppac37.EText2022;

import java.util.Date;

/**
 *
 * @author q6
 */
public class TimeZoneGMT {
    public static void main(String[] args) {
        Date d = new Date();
        System.out.println(""+d.getTimezoneOffset());
    }
}
