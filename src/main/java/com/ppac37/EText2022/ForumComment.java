/*
 */
package com.ppac37.EText2022;

/**
 *
 * @author q6
 */
public class ForumComment implements Comparable<Object> {

    //String idSujet;
    String commentId;
    
    String commentDateCreation;
    
    String commentAuteurId;
    String commentAuteurNom;
    
    String commentCorpHTMLBrut;
    
    String commentModifDate;
    String commentModifParNom;

    @Override
    public int compareTo(Object o) {

        if (o == null) {
            return -1;
        } else {
            if (o instanceof ForumUneDef) {
                ForumUneDef d = (ForumUneDef) o;
                // TODO affinier avec la date de creation et de modification pour comparaison d'historique de commentaire 
                return this.commentId.compareToIgnoreCase(d.commentId);

            } else {
                // TODO
                System.err.printf("TODO UnDef comparaTo class %s\n", o.getClass().getSimpleName());
                return -1;
            }
        }
    }

    public String getCommentAuteurId() {
        return commentAuteurId;
    }

    public String getCommentAuteurNom() {
        return commentAuteurNom;
    }

    public String getCommentCorpHTMLBrut() {
        return commentCorpHTMLBrut;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getDateCreation() {
        return commentDateCreation;
    }

    public String getDateModification() {
        return commentModifDate;
    }

    public String getParModification() {
        return commentModifParNom;
    }

    public void setCommentAuteurId(String commentAuteurId) {
        this.commentAuteurId = commentAuteurId;
    }

    public void setCommentAuteurNom(String commentAuteurNom) {
        this.commentAuteurNom = commentAuteurNom;
    }

    public void setCommentCorpHTMLBrut(String commentCorpHTMLBrut) {
        this.commentCorpHTMLBrut = commentCorpHTMLBrut;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setDateCreation(String dateCreation) {
        this.commentDateCreation = dateCreation;
    }

    public void setDateModification(String dateModification) {
        this.commentModifDate = dateModification;
    }

    public void setParModification(String parModification) {
        this.commentModifParNom = parModification;
    }

}
