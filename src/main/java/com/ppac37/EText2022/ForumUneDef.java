/*
 */
package com.ppac37.EText2022;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 *
 *
 * @author q6
 */
public class ForumUneDef extends ForumComment implements Comparable<Object> {

    /*
    Si l'on se base sur "le Nom des pièces - Impression 3D.pdf" de www.youtube.com/Legueroloco
    
    Image
    Noms français
    Noms anglais
    Description sommaire
    
     */
    String defNom = null;
    SortedSet<String> defNomAlias;
    String commentCorpHTML;

    /**
     *
     */
    public ForumUneDef() {
        defNomAlias = new TreeSet<>();
    }
    public ForumUneDef(ForumComment c) {
        super();
        this.commentAuteurId = c.commentAuteurId;
        this.commentAuteurNom = c.commentAuteurNom;
        
        this.alImgsUrl = c.alImgsUrl;
        this.commentCorpHTMLBrut = c.commentCorpHTMLBrut;
        this.commentDateCreation = c.commentDateCreation;
        this.commentId = c.commentId;
        
        this.commentDateCreation = c.commentDateCreation;
        this.commentModifDate = c.commentModifDate;
        this.commentModifParNom = c.commentModifParNom;
        
        this.setReactionsTotals(c.getReactionsTotals());
                
        defNomAlias = new TreeSet<>();
    }

    /**
     *
     * @param s
     */
    public void addDefNom(String s) {
        defNomAlias.add(s);
        if (defNom == null) {
            defNom = s;
        }
    }

    /**
     *
     * @return
     */
    public String getDefNom() {
        return defNom;
    }

    /**
     *
     * @param defNom
     */
    public void setDefNom(String defNom) {
        this.defNom = defNom;
    }

    /**
     *
     * @return
     */
    public String getCommentCorpHTML() {
        return commentCorpHTML;
    }

    /**
     *
     * @param commentCorpHTML
     */
    public void setCommentCorpHTML(String commentCorpHTML) {
        this.commentCorpHTML = commentCorpHTML;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return -1;
        } else {
            if (o instanceof ForumUneDef) {
                ForumUneDef d = (ForumUneDef) o;
                if (false) {
                    return this.defNom.compareToIgnoreCase(d.defNom);
                }
                // TODO affinier avec la date de creation et de modification pour comparaison d'historique de commentaire 
                return this.commentId.compareToIgnoreCase(d.commentId);

            } else {
                // TODO
                System.err.printf("TODO UnDef comparaTo class %s\n", o.getClass().getSimpleName());
                return -1;
            }
        }
    }

}
