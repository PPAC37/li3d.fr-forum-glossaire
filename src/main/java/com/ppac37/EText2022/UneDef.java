/*
 */
package com.ppac37.EText2022;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * TODO a revoir pour hériter d'une classe UnCommentaireDeSujet
 *
 *
 * @author q6
 */
public class UneDef implements Comparable<Object> {

    /*
    Si l'on se base sur le Nom des pièeces - Impression 3D.pdf de www.youtube.com/Legueroloco
    
    Image
    Noms français
    Noms anglais
    Description sommaire
    
     */
    String defNom = null;
    SortedSet<String> defNomAlias;

    //String idSujet;         
    
    String dateCreation;
    
    String dateModification;
    String parModification;

    String commentId;
    String commentAuteurId;
    String commentAuteurNom;

    String commentCorpHTML;
    //String commentMD;    

    public UneDef() {
        defNomAlias = new TreeSet<>();
    }

    //
    public void addDefNom(String s) {
        defNomAlias.add(s);
        if (defNom == null) {
            defNom = s;
        }
    }

    //
    public String getDefNom() {
        return defNom;
    }

    public void setDefNom(String defNom) {
        this.defNom = defNom;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDateModification() {
        return dateModification;
    }

    public void setDateModification(String dateModification) {
        this.dateModification = dateModification;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentCorpHTML() {
        return commentCorpHTML;
    }

    public void setCommentCorpHTML(String commentCorpHTML) {
        this.commentCorpHTML = commentCorpHTML;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return -1;
        } else {
            if (o instanceof UneDef) {
                UneDef d = (UneDef) o;
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

    public String getParModification() {
        return parModification;
    }

    public void setParModification(String parModification) {
        this.parModification = parModification;
    }

    public String getCommentAuteurId() {
        return commentAuteurId;
    }

    public void setCommentAuteurId(String commentAuteurId) {
        this.commentAuteurId = commentAuteurId;
    }

    public String getCommentAuteurNom() {
        return commentAuteurNom;
    }

    public void setCommentAuteurNom(String commentAuteurNom) {
        this.commentAuteurNom = commentAuteurNom;
    }

    
}
