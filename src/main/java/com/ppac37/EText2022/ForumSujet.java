/*
 */
package com.ppac37.EText2022;

/**
 *
 * @author q6
 */
public class ForumSujet {

    /**
     * Template String.format pour d'un id arriver a une url de page sur le
     * forum.      <code>
     * String idTopic = "45754" ; // 45754 c'est normalement l'id du topic du glossaire
     * qui se trouve épinglé dans la section https://www.lesimprimantes3d.fr/forum/10-discussion-sur-les-imprimantes-3d/
     * 
     * String urlGenericVerTopic = String.format(li3dfrForumTopicTemplate, idTopic);
     * </code> Fonctionne uniquement cat le moteur du forum fait les
     * redirections lors des changemetn de titre ( TODO test unitaire pour le
     * formu )
     * <br>
     * TODO  + "?sortby=date#comments"
     * 
     */
    public static String li3dfrForumTopicTemplate = "https://www.lesimprimantes3d.fr/forum/topic/%s-x/";
    
    String sujetId;
}
