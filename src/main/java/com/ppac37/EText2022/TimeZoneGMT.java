/*
 */
package com.ppac37.EText2022;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author q6
 */
public class TimeZoneGMT {
    public static void main(String[] args) {
        /*
        <strong>Modifié (le) <time datetime="2023-01-05T16:58:19Z" title="05/01/2023 17:58 " data-short="Jan 5">Janvier 5</time> par PPAC</strong>
        */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ROOT);
        
        String datetimeFromForum = "2023-01-05T16:58:19Z";// ATTENTION DATE en GMT+0
        long dataIpsquoteTimestamp = 0;// utilisable comme data-ipsquote-timestamp dans une citation du forum
        try {
            // gérer le GMT+0 et avoir un timestamp pour le forum ( ?nb de seconde et non de milliseconde depuis 1/01/1970 ) 
            dataIpsquoteTimestamp = sdf.parse(datetimeFromForum.substring(0, datetimeFromForum.length() - 1) + "GMT").getTime() / 1000;
            Date d = new Date(dataIpsquoteTimestamp*1000);
            System.out.printf("%s\n",datetimeFromForum);
            System.out.printf("%d\t%s\t%d\n",d.getTime(),d.toString(),d.getTimezoneOffset());
             System.out.printf("%d\t%s\t%d\n",d.getTime(),d.toLocaleString(),d.getTimezoneOffset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        Date d = new Date();
        System.out.printf("%s\n","new Date();");
        System.out.printf("%d\t%s\t%d\n",d.getTime(),d.toString(),d.getTimezoneOffset());
        System.out.printf("%d\t%s\t%d\n",d.getTime(),d.toLocaleString(),d.getTimezoneOffset());
    }
}
