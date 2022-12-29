/*
 */
package com.ppac37.EText2022;

/**
 *
 * @author q6
 */
public class UneDefAlias implements Comparable<UneDefAlias>{
    String a;

    public UneDefAlias(String a) {
        this.a = a;
    }

    
    @Override
    public int compareTo(UneDefAlias o) {
        return a.compareToIgnoreCase(o.a);
    }
    
}
