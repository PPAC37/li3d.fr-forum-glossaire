/*
 */
package com.ppac37.EText2022;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author q6
 */
public class ForumUneEntreeConcours extends ForumUneDef implements Comparable<Object> {

    boolean isRejetedEntry = false;
    boolean isVerifiedValideEntry = false;

    public ForumUneEntreeConcours(ForumUneDef d) {
        super(d);
    }

    /**
     * Pour permetre de comparer et trier ...dans l'order du 1er gagnats(le plus de reactions) au dernier suivie des entrèes rejetés.
     * 
     * @param o
     * @return 
     */
    @Override
    public int compareTo(Object o) {        
        if (o == null) {
            return -1;
        } else {
            if (o instanceof ForumUneEntreeConcours) {
                ForumUneEntreeConcours d = (ForumUneEntreeConcours) o;
                if (d.isRejetedEntry != this.isRejetedEntry) {
                    // une entré rejeté face a une entré non rejeté, ce met en fin .
                    if (d.isRejetedEntry) {
                        return +1;
                    } else {
                        return -1;
                    }
                } else {
                    // On trie par nombre de réaction et par date de création du commentaire en cas d'égalité.
                    if (d.getReactionsTotals() == this.getReactionsTotals()) {
                        return d.getDateCreation().compareTo(this.getDateCreation());
                    } else {
                        return d.getReactionsTotals() - this.getReactionsTotals();
                    }
                }
            } else if (o instanceof ForumUneDef) {
                ForumUneDef d = (ForumUneDef) o;
                // On trie par nombre de réaction et par date de création du commentaire en cas d'égalité.
                if (d.getReactionsTotals() == this.getReactionsTotals()) {
                    return d.getDateCreation().compareTo(this.getDateCreation());
                } else {
                    return d.getReactionsTotals() - this.getReactionsTotals();
                }
            } else {
                // TODO
                System.err.printf("TODO UnDef comparaTo class %s\n", o.getClass().getSimpleName());
                return -1;
            }
        }
    }


    
}
