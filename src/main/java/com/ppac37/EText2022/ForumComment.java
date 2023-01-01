/*
 */
package com.ppac37.EText2022;

/**
 * Un commentaire  d'un sujet du forum.
 * @author q6
 */
public class ForumComment implements Comparable<Object> {

    String commentId;
    
    String commentDateCreation;
    
    String commentAuteurId;
    String commentAuteurNom;
    
    String commentCorpHTMLBrut;
    
    String commentModifDate;
    String commentModifParNom;

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
                // TODO affinier avec la date de creation et de modification pour comparaison d'historique de commentaire 
                return this.commentId.compareToIgnoreCase(d.commentId);

            } else {
                // TODO
                System.err.printf("TODO UnDef comparaTo class %s\n", o.getClass().getSimpleName());
                return -1;
            }
        }
    }

    /**
     *
     * @return
     */
    public String getCommentAuteurId() {
        return commentAuteurId;
    }

    /**
     *
     * @return
     */
    public String getCommentAuteurNom() {
        return commentAuteurNom;
    }

    /**
     *
     * @return
     */
    public String getCommentCorpHTMLBrut() {
        return commentCorpHTMLBrut;
    }

    /**
     *
     * @return
     */
    public String getCommentId() {
        return commentId;
    }

    /**
     *
     * @return
     */
    public String getDateCreation() {
        return commentDateCreation;
    }

    /**
     *
     * @return
     */
    public String getDateModification() {
        return commentModifDate;
    }

    /**
     *
     * @return
     */
    public String getParModification() {
        return commentModifParNom;
    }

    /**
     *
     * @param commentAuteurId
     */
    public void setCommentAuteurId(String commentAuteurId) {
        this.commentAuteurId = commentAuteurId;
    }

    /**
     *
     * @param commentAuteurNom
     */
    public void setCommentAuteurNom(String commentAuteurNom) {
        this.commentAuteurNom = commentAuteurNom;
    }

    /**
     *
     * @param commentCorpHTMLBrut
     */
    public void setCommentCorpHTMLBrut(String commentCorpHTMLBrut) {
        this.commentCorpHTMLBrut = commentCorpHTMLBrut;
    }

    /**
     *
     * @param commentId
     */
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    /**
     *
     * @param dateCreation
     */
    public void setDateCreation(String dateCreation) {
        this.commentDateCreation = dateCreation;
    }

    /**
     *
     * @param dateModification
     */
    public void setDateModification(String dateModification) {
        this.commentModifDate = dateModification;
    }

    /**
     *
     * @param parModification
     */
    public void setParModification(String parModification) {
        this.commentModifParNom = parModification;
    }

}
