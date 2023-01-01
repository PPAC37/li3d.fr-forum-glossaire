/*
 */
package com.ppac37.EText2022;

import java.text.Collator;
import java.util.Locale;

/**
 *
 * @author q6
 */
public class ForumUneDefAlias implements Comparable<ForumUneDefAlias> {

    String a;

    /**
     *
     * @param a
     */
    public ForumUneDefAlias(String a) {
        this.a = a;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(ForumUneDefAlias o) {

        // Pause problème pour bien maitriser l'ordre ... cf codepoint value vs ordre d'usage de la lang ... ( Minuscule ?= majuscul ?= accentué, et ? '-' < '(' < a-Z ...
        //        return a.compareToIgnoreCase(o.a); 
        
        //Get the Collator for French and set its strength to PRIMARY
        Collator usCollator = Collator.getInstance(Locale.FRENCH);
        usCollator.setStrength(Collator.PRIMARY);
        return usCollator.compare(a, o.a);

    }

}
