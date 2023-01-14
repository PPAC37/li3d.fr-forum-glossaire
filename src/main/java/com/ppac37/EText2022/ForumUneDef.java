/*
 */
package com.ppac37.EText2022;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.jsoup.nodes.Element;

/**
 *
 *
 *
 * @author q6
 */
public class ForumUneDef extends ForumComment implements Comparable<Object>, DefinitionInterface {

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
        this.setCommentDateCreation( c.getDateCreation() );
        this.commentId = c.commentId;
        this.sujetId = c.sujetId;

        //this.commentDateCreation = c.commentDateCreation;
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
    public void parseComment(Element eACommentId) {
        parseComment(eACommentId, this);
    }

    /**
     *
     * @param eACommentId
     * @param uneDef
     */
    public static void parseComment(Element eACommentId, ForumUneDef uneDef) {
        ForumComment.parseComment(eACommentId, uneDef);

        // TODO ici les parse propre a une definition du glossaire qui sont actuellement fait dans ForumComment.parseComment(eACommentId, uneDef);
    }

    //
    //
    // Les truc de sortie ( texte et html ) il faudrait au moins 3 version ( basic toString pour le débug) , (basic toStringMarkDown(int mode ) , 
    @Override
    public String toString() {
        return String.format("%35s [%2d](%s)\n", defNom, defNomAlias.size(), defNomAlias.toString());
    }

    public String toStringFTab() {
        return String.format(" comment-id %s\t%-35s\t%d\t%s\n", commentId, defNom, defNomAlias.size(), defNomAlias.toString());
    }

    public String toStringMD_Coche(String rUrl) {
        return String.format(" * [ ] [comment-%s - %s ](%s)\n", commentId, defNomAlias.toString(), rUrl);
    }

    public String toStringMD_Index(String rUrl) {

        return String.format("## %s\n%s\n\n[lien vers source](%s)\n\n---\n", defNomAlias.toString(), commentCorpHTMLBrut, rUrl);
    }

    public String toStringMD_Detail() {
        return String.format(" comment-id %s\t%-35s\t%d\t%s\n", commentId, defNom, defNomAlias.size(), defNomAlias.toString());
    }

    public String toStringMD() {
        return String.format(" comment-id %s\t%-35s\t%d\t%s\n", commentId, defNom, defNomAlias.size(), defNomAlias.toString());
    }

    /*
html rendue .md github     
     <h2 dir="auto"><a id="user-content-abs-acrylonitrile-butadiène-styrène" class="anchor" aria-hidden="true" href="#abs-acrylonitrile-butadiène-styrène"><svg class="octicon octicon-link" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path fill-rule="evenodd" d="M7.775 3.275a.75.75 0 001.06 1.06l1.25-1.25a2 2 0 112.83 2.83l-2.5 2.5a2 2 0 01-2.83 0 .75.75 0 00-1.06 1.06 3.5 3.5 0 004.95 0l2.5-2.5a3.5 3.5 0 00-4.95-4.95l-1.25 1.25zm-4.69 9.64a2 2 0 010-2.83l2.5-2.5a2 2 0 012.83 0 .75.75 0 001.06-1.06 3.5 3.5 0 00-4.95 0l-2.5 2.5a3.5 3.5 0 004.95 4.95l1.25-1.25a.75.75 0 00-1.06-1.06l-1.25 1.25a2 2 0 01-2.83 0z"></path></svg></a>ABS (Acrylonitrile Butadiène Styrène)</h2>
     */
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
    public void reloadReactionHitory() {
        //https://www.lesimprimantes3d.fr/forum/topic/50575-%F0%9F%8E%81-concours-de-no%C3%ABl-%F0%9F%8E%85%F0%9F%8C%B2-des-imprimantes-%C3%A0-gagner-%F0%9F%8E%81/?do=showReactionsComment&comment=524576&changed=1&reaction=all

        System.out.printf("TODO : getOnlineReactionHistori topic %s comment %s (user %s)\n", this.sujetId, this.commentId, this.commentAuteurNom);
        //TODO cahed or not
        if (!this.sujetId.isBlank()) {
            // TODO revoir l'implèmentation du cache pour ne pas avoir a bidouiller l'url comme cela 
            // cf je met dans la partie considéré comme le nom du sujet l'id du commentaire
            // car mon cache ne prend pas en compte si une url a des argument et là cela me permde de lui faire penser que c'est pas la mêm url donc il n'ecrasera pas le fichier du sujet ...
            // mais c'est vraiment pas maintenable et inchoérent de faire comme cela.

            String sUrlVersTopic = String.format(ForumSujet.li3dfrForumTopicTemplate, this.sujetId + "-" + this.commentId);
if ( false ){
    //TODO a revoir bug du cache ...
            UrlCParserForum urlCParser = new UrlCParserForum(sUrlVersTopic
                    + //"/"+
                    "?do=showReactionsComment&comment="
                    + this.commentId//"524576"
                    + "&changed=1&reaction=all",
                     true);
}
            //lienVersCommentaireBase = sUrlVersTopic + "?do=findComment&comment=";
        }
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Set<String> getDefAlias() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getDefShortDesc() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getDefLongDesc() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
