/*
 */
package com.ppac37.EText2022;

/**
 *
 * @author q6
 */
public class UtilTimeTaken {
    long t0 = System.currentTimeMillis();
    
        
        

    public UtilTimeTaken() {
        t0 = System.currentTimeMillis();
    }
    
    public void  outTimeTakenFromObjectCreation(String label){
        long t1 = System.currentTimeMillis();
        System.out.println(" "+label + " ( loaded in " + (t1 - t0) + " ms.)");
    }
        
}
