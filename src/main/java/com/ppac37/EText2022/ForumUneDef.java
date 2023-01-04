/*
 */
package com.ppac37.EText2022;


import java.util.SortedSet;
import java.util.TreeSet;
import org.jsoup.nodes.Element;

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
    
    //
    //
    //
    @Override
    public void parseComment(Element eACommentId){
        parseComment(eACommentId, this);
    }
    /**
     * 
     * @param eACommentId
     * @param uneDef 
     */
    public static void parseComment(Element eACommentId, ForumUneDef uneDef){
        ForumComment.parseComment(eACommentId, uneDef);
        
        // TODO ici les parse propre a une definition du glossaire qui sont actuellement fait dans ForumComment.parseComment(eACommentId, uneDef);
    }
    
    //
    //
    // Les truc de sortie ( texte et html ) il faudrait au moins 3 version ( basic toString pour le débug) , (basic toStringMarkDown(int mode ) , 

    @Override
    public String toString() {
        return super.toString(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    /*
    // TODO Analyse pour un implement TableHeader like
    // ? implements qq chose pour forcer a avoir et pouvoir redéfinir 
    // ? que cela soit utilisable pour plus simplement implémenter un TableModel ? donc avec une enum de "mode" ( texte lien, id, modeShort, modeLong, ... et renderer editor ? )
    public String toStringMarkdown() {
        
    }
    
    public String toStringHTML() {
        
    }

*/

    //
    //
    //
    
    public void reloadReactionHitory(){
        //https://www.lesimprimantes3d.fr/forum/topic/50575-%F0%9F%8E%81-concours-de-no%C3%ABl-%F0%9F%8E%85%F0%9F%8C%B2-des-imprimantes-%C3%A0-gagner-%F0%9F%8E%81/?do=showReactionsComment&comment=524576&changed=1&reaction=all
        
        System.out.printf("TODO : getOnlineReactionHistori topic %s comment %s (user %s)\n",this.sujetId,this.commentId,this.commentAuteurNom );
        //TODO cahed or not
         if (!this.sujetId.isBlank()) {
             // TODO revoir l'implèmentation du cache pour ne pas avoir a bidouiller l'url comme cela 
             // cf je met dans la partie considéré comme le nom du sujet l'id du commentaire
             // car mon cache ne prend pas en compte si une url a des argument et là cela me permde de lui faire penser que c'est pas la mêm url donc il n'ecrasera pas le fichier du sujet ...
             // mais c'est vraiment pas maintenable et inchoérent de faire comme cela.
             
                String sUrlVersTopic = String.format(ForumSujet.li3dfrForumTopicTemplate, this.sujetId+"-"+this.commentId);
                UrlCParserForum urlCParser = new UrlCParserForum(sUrlVersTopic+
                        //"/"+
                                 "?do=showReactionsComment&comment="
                                + this.commentId//"524576"
                                + "&changed=1&reaction=all"
                        , true);
                //lienVersCommentaireBase = sUrlVersTopic + "?do=findComment&comment=";
            }
    }
}
