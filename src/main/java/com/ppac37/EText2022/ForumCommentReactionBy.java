/*
 */
package com.ppac37.EText2022;

/**
 *
 * @author q6
 */
public class ForumCommentReactionBy {

    private String idComment;
    private String idUser;
    private String type;
    private String date;

    public ForumCommentReactionBy(String idComment, String idUser, String type, String date) {
        this.idComment = idComment;
        this.idUser = idUser;
        this.type = type;
        this.date = date;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
