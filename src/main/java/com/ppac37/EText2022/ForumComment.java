/*
 */
package com.ppac37.EText2022;

import com.pnikosis.html2markdown.HTML2Md;
import static com.ppac37.EText2022.ForumLI3DFR.baseDirOutput;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Un commentaire d'un sujet du forum.
 *
 * @author q6
 */
public class ForumComment implements Comparable<Object> {

    String sujetId;
    String commentId;

    private String commentDateCreation;

    protected String commentAuteurId;
    protected String commentAuteurNom;

    String commentCorpHTMLBrut;

    String commentModifDate;
    String commentModifParNom;

    long reationsEnDateDe;
    /**
     * Attention forcement c'est les réactions a la date du fichier
     */
    private int reactionsTotals;
    // liste des réaction ( date de réaction, type de réaction ,utilisateur) 
    //cf https://www.lesimprimantes3d.fr/forum/topic/50575-X/?do=showReactionsComment&comment=526603&changed=1&reaction=all
    private ArrayList<ForumCommentReactionBy> reactions = new ArrayList<>();

    // liste des url des images contenus dans le commentaire ( exception des emojie )
    public ArrayList<String> alImgsUrl = new ArrayList<>();
    public ArrayList<String> alImgsUrlDansCitation = new ArrayList<>();

    
    public void saveInHistoryDir(String headTitle){
        UtilFileWriter fw = new UtilFileWriter(baseDirOutput + File.separator + "comment-"+this.commentId+".html");
        fw.append("<!DOCTYPE html>\n");
        fw.append("<html lang=\"fr\">\n");
        fw.append(" <head>\n");
        
        fw.append("  <title>");
        fw.append("comment-");
        fw.append(this.commentId);
        fw.append(" - ");
        fw.append(headTitle);
        fw.append("</title>\n");
        
        fw.append(" </head>\n");
        fw.append(" <body>\n");
        fw.append("<!-- BRUT comment-"+this.commentId+" DEBUT -->\n");
        fw.append(this.commentCorpHTMLBrut);        
        fw.append("<!-- BRUT comment-"+this.commentId+" FIN -->\n");
        fw.append(" </body>\n");
        fw.append("</html>\n");
        fw.flush();
        fw.close();
    }
    
    public void saveInHistoryDirPrety(String headTitle){
        UtilFileWriter fw = new UtilFileWriter(baseDirOutput + File.separator + "comment-"+this.commentId+" - "+headTitle+".html");
        fw.append("<!DOCTYPE html>\n");
        fw.append("<html lang=\"fr\">\n");
        fw.append(" <head>\n");
        
        fw.append("  <title>");
        fw.append("comment-");
        fw.append(this.commentId);
        fw.append(" - ");
        fw.append(headTitle);
        fw.append("</title>\n");
        
        fw.append(" </head>\n");
        //fw.append(" <body>\n");
        Document doc = Jsoup.parseBodyFragment(this.commentCorpHTMLBrut);
        doc.outputSettings().prettyPrint(true);
        
        fw.append("<!-- BRUT comment-"+this.commentId+" DEBUT -->\n");
        fw.append(doc.body().outerHtml());        
        
        fw.append("\n<!-- BRUT comment-"+this.commentId+" FIN -->\n");
        //fw.append(" </body>\n");
        fw.append("</html>\n");
        fw.flush();
        fw.close();
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
                // TODO affinier avec la date de creation et de modification pour comparaison d'historique de commentaire 
                return this.commentId.compareToIgnoreCase(d.commentId);

            } else {
                // TODO
                System.err.printf("TODO UnDef comparaTo class %s\n", o.getClass().getSimpleName());
                return -1;
            }
        }
    }

    public String getSujetId() {
        return sujetId;
    }

    public void setSujetId(String sujetId) {
        this.sujetId = sujetId;
    }

    public String getCommentDateCreation() {
        return commentDateCreation;
    }

    public void setCommentDateCreation(String commentDateCreation) {
        this.commentDateCreation = commentDateCreation;
    }

    public String getCommentModifDate() {
        return commentModifDate;
    }

    public void setCommentModifDate(String commentModifDate) {
        this.commentModifDate = commentModifDate;
    }

    public String getCommentModifParNom() {
        return commentModifParNom;
    }

    public void setCommentModifParNom(String commentModifParNom) {
        this.commentModifParNom = commentModifParNom;
    }

    public long getReationsEnDateDe() {
        return reationsEnDateDe;
    }

    public void setReationsEnDateDe(long reationsEnDateDe) {
        this.reationsEnDateDe = reationsEnDateDe;
    }

    public ArrayList<ForumCommentReactionBy> getReactions() {
        return reactions;
    }

    public void setReactions(ArrayList<ForumCommentReactionBy> reactions) {
        this.reactions = reactions;
    }

    public ArrayList<String> getAlImgsUrl() {
        return alImgsUrl;
    }

    public void setAlImgsUrl(ArrayList<String> alImgsUrl) {
        this.alImgsUrl = alImgsUrl;
    }

    public int getReactionsTotals() {
        return reactionsTotals;
    }

    public void setReactionsTotals(int reactionsTotals) {
        this.reactionsTotals = reactionsTotals;
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

    static String sInUrlPrefixUserId = "/forum/profile/";
    static String sInUrlSuffixUserId = "-";
    boolean debugSetCommentAuteurIdFromUrl = false;

    public void setCommentAuteurIdFromUrl(String commentAuteurUrl) {
        if (debugSetCommentAuteurIdFromUrl) {
            System.out.println("setCommentAuteurIdFromUrl: " + commentAuteurUrl);
            //https://www.lesimprimantes3d.fr/forum/profile/2-motard-geek/?wr=eyJh...
        }
        int posPrefixUserId = commentAuteurUrl.indexOf(sInUrlPrefixUserId);
        int posSuffixUserId = commentAuteurUrl.indexOf(sInUrlSuffixUserId);
        if (posPrefixUserId > -1 && posSuffixUserId > posPrefixUserId) {
            this.commentAuteurId = commentAuteurUrl.substring(posPrefixUserId + sInUrlPrefixUserId.length(), posSuffixUserId);
            if (debugSetCommentAuteurIdFromUrl) {
                System.out.println(" user id found : " + commentAuteurId);
            }
        } else {
            // TODO 
            System.out.println("setCommentAuteurIdFromUrl: no user id found in: " + commentAuteurUrl);
        }
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

    //
    //
    //
    
    /**
     * Fonction utilitaire pour peupler notre instance.
     * 
     * @param eACommentId element d'un <code>select("a[id^=comment-]");</code>
     */
    public void parseComment(Element eACommentId){
        //TODO rendre cela posible 
        //parseComment(eACommentId, this);
    }
    /**
     * Peupler l'instance passé en argument avec ce que l'on trouve dans la source.
     * TODO a décomposer ? revoir l'héritage qui fait quoi car là c'est prioncipalement le parse d'un commentaire de sujet plutot que d'un commentaire spécifique a une définition.
     * TODO je devrais peupler un objet ForumComment et non un ForumUneDef. ( méthode a crer dans ForumUneDef avec se qui ne devrait pas etre ici )
     * @param eACommentId la source un element d'un <code>select("a[id^=comment-]");</code>
     * @param leFCommentAsDef l'instance a peupler
     */
    public static void parseComment(Element eACommentId, ForumUneDef leFCommentAsDef) {
        
        ForumComment leFComment = (ForumComment)leFCommentAsDef;
        
        Element nextElementSibling = eACommentId.nextElementSibling();
        
        String tmpCommentId = parseAndPopulateCommentId(eACommentId, leFComment);

        parseAndPopulateReactionTotal(nextElementSibling, tmpCommentId, leFComment);

        parseAndPoputatAuteurNomAndId(nextElementSibling, leFComment);        
        
        parseAndPopulateDateTimeCAndM(nextElementSibling, leFComment);
        
        Element commentContent = parseAndPopulateCommentBrutContent(nextElementSibling, leFComment);
                
        parseAndPopulateCommentContentImg(commentContent, leFComment);
       
        //
        // TODO codage a revoir car on passe encore a un autre niveau abstraction ,  spécifique au glossaire.
        //
        
        boolean doSommaireBaseOnHTag = true;
        if ( doSommaireBaseOnHTag ){
            selectCommentContentTitles(commentContent);
        }
        
        
        boolean haveH2Sommaire = false;
        Element elemTitreSommaire = null;
        // voir si le coprs du commentaire contien un titre 2 et si se serai pas le sommaire
        if (true && commentContent != null) {
            Elements selectCommentH2 = commentContent.select("h2");
            if (!selectCommentH2.isEmpty()) {
                // le commentaire contien au moins un "titre 2"
                for (Element c_elemH2 : selectCommentH2) {
                    if (c_elemH2.text().equalsIgnoreCase("Sommaire")) {
                        haveH2Sommaire = true;
                        elemTitreSommaire = c_elemH2;
                        System.out.printf(" H2: \"%s\" comment id %s\n", c_elemH2.text(), eACommentId.attr("id").substring(8));
                        leFCommentAsDef.setDefNom("0_" + c_elemH2.text());
                    } else {
                        if ( false ) {
                            System.out.printf(" H2: \"%s\"\n", c_elemH2.text());
                        }
                    }
                }
            }
        }
        
        boolean haveDefinitions = false;
        // les elements en gras qui sont en puce ( spécifique au glossaire pour identifier le terme définie ... )
        if (true && commentContent != null) {
            Elements selectCommentH2 = commentContent.select("ul li strong");
            if (!selectCommentH2.isEmpty()) {
                haveDefinitions = true;
                // le commentaire contien au moins un "titre 2"
                for (Element c_elemH2 : selectCommentH2) {
                    if (false) {
                        System.out.printf(" * \"%s\"\n", c_elemH2.text());
                    }
                    //uneDef.setDefNom(c_elemH2.text());
                    leFCommentAsDef.addDefNom(c_elemH2.text());
                }
            }
        }
        alterIframe(haveH2Sommaire, commentContent);
        alterImgEmojie(commentContent);
        alterLi3dForumUrlsToLocal(haveH2Sommaire, commentContent);
        alterNonForumUrlsToHighlight(haveH2Sommaire, commentContent);
        alterLi3dForumUrlsRetourSommaire(commentContent);
       
        leFCommentAsDef.setCommentCorpHTML(commentContent.html());
        
        
        if (elemTitreSommaire != null) {
            // Pour réutiliser l'entete du sommaire, on supprime le sommaire ! ?
            elemTitreSommaire.nextElementSiblings().remove(); //.attr("style", "background-color:#c0392b;");
            elemTitreSommaire.remove();
            if (false) {
                System.out.printf("%s\n", commentContent.html());
            }
            ForumLI3DFR.enteteSommaireToUse = commentContent.html();
        }
        ForumLI3DFR.lesDef.add(leFCommentAsDef);
        // On essais de convertire le Commentaire au format .md ( TODO revoir le dl des images, couleurs du texte , couleur de fond du texte , ... )
        if (false) {
            System.out.printf("---\n%s\n---\n", HTML2Md.getTextContent(commentContent)); //HTML2Md.convertHtml(commentContent.html(), "UTF-8"));
        }
        if (false) {
            System.out.println();
        }
    }

    public static void alterLi3dForumUrlsRetourSommaire(Element commentContent) {
        boolean doRemoveRetourSommaire = true;
        boolean doRemoveRetourSommairePreview = false;
        if (doRemoveRetourSommaire) {
            //nettoyage de l'eventuel lien de retour au sommaire du glossaire et infos modification
            /*
            <p> &nbsp; </p>
            <p style="text-align:center;"> <a href="https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/" rel="">Retour au sommaire du glossaire</a> </p>
            */
            Element lienVersSujetGlossaire = commentContent.select("p>a[href^=\"https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/\"]").last();
            if (lienVersSujetGlossaire != null) {
                Element previousElementSibling = lienVersSujetGlossaire.parent().previousElementSibling();
                if (previousElementSibling != null && previousElementSibling.text().isBlank()) {
                    if (doRemoveRetourSommairePreview) {
                        previousElementSibling.attr("style", "background-color:#c0392b;");
                        lienVersSujetGlossaire.parent().attr("style", "background-color:#c0392b;");
                    } else {
                        previousElementSibling.remove();
                        lienVersSujetGlossaire.parent().remove();
                    }
                }
            }
        }
    }

    public static void alterNonForumUrlsToHighlight(boolean haveH2Sommaire, Element commentContent) {
        boolean doHighLithA = true;
        if (doHighLithA && !haveH2Sommaire) {
            Elements elemsA = commentContent.select("a");
            if (elemsA != null) {
                for (Element eA : elemsA) {
                    String eATitle = eA.attr("title");
                    String eAHref = eA.attr("href");
                    String eAClass = eA.attr("class");
                    String eAStyle = eA.attr("style");
                    String eA_Text = eA.text();
                    if (eAHref.startsWith("#")) {
                        //eA.attr("style", "background-color:green;" + eAStyle);
                    } else {
                        //eA.attr("style", "background-color:#c0392b;" + eAStyle);
                        if (eAHref.equals(eA_Text)) {
                        } else {
                            eA.after("<strong class=\"warn\">( href= " + eAHref + " )</strong>");
                        }
                    }
                }
            }
        }
    }

    public static void alterLi3dForumUrlsToLocal(boolean haveH2Sommaire, Element commentContent) {
        boolean doForLocal = true;
        boolean doForLocalPreview = false;
        if (doForLocal && !haveH2Sommaire) {
            Elements elemsA = commentContent.select("a[href~=" + ForumLI3DFR.HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754 + "]");
            if (elemsA != null) {
                for (Element eA : elemsA) {
                    String eAHref = eA.attr("href");
                    if (eAHref.length() > ForumLI3DFR.HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754.length()) {
                        eA.attr("href", "#" + eAHref.substring(eAHref.length() - 6));
                        //                            String eAStyle = eA.attr("style");
                        if (doForLocalPreview) {
                            eA.after("<strong class=\"warn\">( was href= " + eAHref + " )</strong>");
                        }
                    }
                }
            }
        }
    }

    public static void alterImgEmojie(Element commentContent) {
        boolean doEmojieAlt = true;
        boolean previewEmojieAlt = false;
        if (doEmojieAlt) {
            Elements eImg_ipsEmoji = commentContent.select("img.ipsEmoji");
            if (eImg_ipsEmoji != null) {
                for (Element emoj : eImg_ipsEmoji) {
                    String attrAlt = emoj.attr("alt");
                    emoj.before(attrAlt);
                    if (previewEmojieAlt) {
                        emoj.attr("style", "background-color:#c0392b;");
                    } else {
                        emoj.remove();
                    }
                }
            }
        }
    }

    public static void alterIframe(boolean haveH2Sommaire, Element commentContent) {
        //
        //
        //
        boolean doIFram = true;
        if (doIFram && !haveH2Sommaire) {
            Elements elemsIFrame = commentContent.select("iframe, img");
            if (elemsIFrame != null) {
                for (Element eIframe : elemsIFrame) {
                    String eIframeStyle = eIframe.attr("style");
                    String eIframeSrc = eIframe.attr("src");
                    String eIframedata_embed_src = eIframe.attr("data-embed-src");
                    eIframe.attr("style", "background-color:#c0392b;" + eIframeStyle);
                    if (!eIframeSrc.isBlank()) {
                        eIframe.after(" ( src= " + eIframeSrc + " ) ");
                    }
                    if (!eIframedata_embed_src.isBlank()) {
                        eIframe.after(" ( data-embed-src = " + eIframedata_embed_src + " ) ");
                    }
                }
            }
        }
    }

    public static void parseAndPopulateCommentContentImg(Element commentContent, ForumComment leFComment) {
        if (true) {
            // L'ensemble des images pour ou non les sauver en local et modifier le src
            // pour le concours je n'est normalement pas a prednre en compte lesles citations...
            
            //  Document parseBodyFragment = Jsoup.parseBodyFragment(commentContent.html());
            
            // TODO il me faudrais tout de même avoir le nombre d'image total ( pour vérification )
            boolean separationDesImageEnCitation = true;
            if (separationDesImageEnCitation) {
                Elements selectCommentImgInCitation = commentContent.select("blockquote.ipsQuote img, div.ipsQuote_contents img");
                for (Element cimg : selectCommentImgInCitation) {
                    
                    String imgSrc = cimg.attr("src");
                    if (cimg.hasClass("ipsEmoji")) {
                        // exception des emoji
                    } else {
                        leFComment.alImgsUrlDansCitation.add(imgSrc);
                    }
                }
                selectCommentImgInCitation.remove();
            }
            
            Elements selectCommentImg = commentContent.select("img[src]");
            for (Element cimg : selectCommentImg) {
                
                String imgSrc = cimg.attr("src");
                if (cimg.hasClass("ipsEmoji")) {
                    // exception des emoji
                } else {
                    leFComment.alImgsUrl.add(imgSrc);
                }
                // voila là il est posible de modifier le src ...
                //cimg.attr("src", "cacheBaseDir");
                // TODO en fait je voudrais garder les liens d'origine pour les mettre en "sources" (ou commentaire HTML
                // mais donc là ...
                // avoir le répertoire de mise en cache ?
                // ? utiliser ImageIO pour le telechargement ou le faire a coup de HttpRequest ?
            }
        }
    }

    public static Element parseAndPopulateCommentBrutContent(Element nextElementSibling, ForumComment leFComment) {
        // le corps du commentaire
        Element commentContent = nextElementSibling.selectFirst("div[data-role=commentContent]");
        leFComment.setCommentCorpHTMLBrut(commentContent.html());
        
         //
        if (false) {
            // pour debug
            System.out.printf("---\n%s\n---\n", commentContent.html());
            // TODO a revoir actuellement ma version de HTML2Md rapatrie les images dans un repertoire codé en dur et sans bien faire attention au posible ecrasement de deux images ayant le même nom ...
            System.out.printf("---\n%s\n---\n", HTML2Md.getTextContent(commentContent)); //HTML2Md.convertHtml(commentContent.html(), "UTF-8"));
        }
        
        return commentContent;
    }

    public static void parseAndPopulateDateTimeCAndM(Element nextElementSibling, ForumComment leFComment) {
        // heures creation et ?modification
        Elements dateMsg = nextElementSibling.select("time");
        String sTmpLastDateTime = "";
        for (Element d : dateMsg) {
            if (false) {
                System.out.printf("  dateMsg :  %s ( %s ) %s :: %s\n", d.attr("title"), d.text(), d.attr("datetime"), d.parent().text());
            }
            String sDateTime = d.attr("datetime");
            if (sDateTime.equals(sTmpLastDateTime)) {
                // on ignore car on a deja sortie cette date précédament
            } else {
                if (sTmpLastDateTime.isEmpty()) {
                    leFComment.setCommentDateCreation(sDateTime);
                    if (false) {
                        System.out.printf("  //  %s ( %s ) %s :: %s\n", d.attr("title"), d.text(), d.attr("datetime"), d.parent().text());
                    }
                } else {
                    leFComment.setDateModification(sDateTime);
                    if (false) {
                        System.out.printf("  //  %s ( %s ) %s :: %s // \"%s\"\n", d.attr("title"), d.text(), d.attr("datetime"), d.parent().text(), d.nextSibling().toString());
                    }
                    leFComment.setParModification(d.nextSibling().toString().substring(5));
                    // on enleve cette info
                    /*
                    <span class="ipsType_reset ipsType_medium ipsType_light" data-excludequote=""> <strong>Modifié (le) <time datetime="2022-02-05T20:32:36Z" title="05/02/2022 20:32 " data-short="Feb 5">Février 5</time> par Yo'</strong> </span>
                    */
                    boolean doRemoveModified = true;
                    boolean doRemoveModifiedPreview = false;
                    if (doRemoveModified) {
                        if (doRemoveModifiedPreview) {
                            d.parent().parent().attr("style", "background-color:#c0392b;");
                        } else {
                            d.parent().parent().remove();
                        }
                    }
                }
            }
            sTmpLastDateTime = sDateTime;
        }
    }

    public static void parseAndPoputatAuteurNomAndId(Element nextElementSibling, ForumComment leFComment) {
        // le nom de l'auteur et le lien vers son profil
        Element h3AAuteur = nextElementSibling.selectFirst("h3.cAuthorPane_author > a");
        if (false) {
            System.out.printf("  Auteur : %s ( %s )\n", h3AAuteur.text(), h3AAuteur.attr("abs:href"));
        }
        leFComment.setCommentAuteurNom(h3AAuteur.text());
        leFComment.setCommentAuteurIdFromUrl(h3AAuteur.attr("abs:href"));
        
        // l'image de l'avatard de l'auteur
        if (false) {
        Element auteurImg = nextElementSibling.selectFirst("div.cAuthorPane_photoWrap > a > img");
            System.out.printf("  Auteur : %s ( %s )\n", auteurImg.attr("alt"), auteurImg.attr("src"));
        }
        
        // group
        if (false) {
        Element auteurGroup = nextElementSibling.selectFirst("li[data-role=group]");
            System.out.printf("  Auteur :   ( %s )\n", auteurGroup.text());
        }
        
        
    }

    public static void parseAndPopulateReactionTotal(Element nextElementSibling, String tmpCommentId, ForumComment leFComment) {
        // ? nblike
        Elements selectReact_reactions = nextElementSibling.select("ul.ipsReact_reactions li.ipsReact_reactCount");
        if (selectReact_reactions != null) {
            if (false) {
                System.out.println("Reactiosn? :");
                System.out.println(selectReact_reactions.text());
            }
            if (false) {
                System.out.printf("%s reaction : %s\n", tmpCommentId, selectReact_reactions.text());
            }
            int totalR = 0;
            for (Element r : selectReact_reactions) {
                String rAlt = r.selectFirst("img").attr("alt");
                String rText = r.text();
                int asNb = 0;
                try {
                    asNb = Integer.parseInt(rText);
                    totalR += asNb;
                } catch (NumberFormatException e) {
                    
                }
                
                if (false) {
                    System.out.printf(" \"%s\" : \"%s\"\n", rText, rAlt);
                }
            }
            if (false) {
                System.out.printf(" Total : %d\n", totalR);
            }
            leFComment.setReactionsTotals(totalR);
            
        }
    }

    public static String parseAndPopulateCommentId(Element eACommentId, ForumComment leFComment) {
        if (false) {
            System.out.printf(" \"%s\"\n", eACommentId.attr("id"));
        }
        String tmpCommentId = "";
        if (eACommentId.attr("id").startsWith("comment-")) {
            tmpCommentId = eACommentId.attr("id").substring(8);
            
            if (false) {
                System.out.printf(" comment id \"%s\"\n", tmpCommentId);
            }
            leFComment.setCommentId(tmpCommentId);
        }
        return tmpCommentId;
    }

    static boolean  debugSelectCommentContentTitles = false;
    public static Elements selectCommentContentTitles(Element commentContent) {
        Elements selectH1toH5 = commentContent.select("h1,h2,h3,h4,h5");
        if (!selectH1toH5.isEmpty() && debugSelectCommentContentTitles ) {
            // le commentaire contien au moins un "titre 2"
            for (Element e : selectH1toH5) {
                
                System.out.printf(" Titre: %s \"%s\"\n",e.tagName(), e.text());
                if (e.text().trim().equalsIgnoreCase("Sommaire")) {
                    
                } else {
                    
                }
            }
        }
        return selectH1toH5;
    }
    public static Elements selectCommentContentTitles(ForumComment commentContent) {
         Document parseBodyFragment = Jsoup.parseBodyFragment(commentContent.getCommentCorpHTMLBrut());
         return selectCommentContentTitles(parseBodyFragment.body());
            
    }

    static String pattern_yyyyMMddTHHmmssZ = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * 
     * @param sForumDatetime
     * @return a long to use as attrib for an attrib data-ipsquote-timestamp
     */
    private long getForumTimeStampFromForumDateTime(String sForumDatetime) {
        long lForumTimestamp = 0;//
        SimpleDateFormat sdf = new SimpleDateFormat(pattern_yyyyMMddTHHmmssZ);
        String parsableDateTime = sForumDatetime;
        parsableDateTime = parsableDateTime.substring(0, parsableDateTime.length() - 1) + "GMT";
        try {

            lForumTimestamp = sdf.parse(parsableDateTime).getTime() / 1000;
        } catch (ParseException e) {
            System.err.printf("err: parsing forum datetime (%s) to forum timestamp with %s pattern\n",
                    parsableDateTime,
                    pattern_yyyyMMddTHHmmssZ);
            //e.printStackTrace();
        }
        return lForumTimestamp;
    }

    /**
     * Append to an element the structure of the Forum citation ...
     * @param elementToAppendTheCitationElementsCreated an element in a Jsoup Document wherre to append all the "bordel" ...
     * @param citationMsg seems to be errased when the forum editor recive it mais du genre "     Le 30/11/2022 at 19:47, PPAC a dit" et y a un "&nbsp;:\n" ...
     * @return the Element where to append the contente of the citation
     */
    public Element createElementAsCitation(Element elementToAppendTheCitationElementsCreated, String citationMsg) {
        /*
        <span data-cke-copybin-start="1">​</span>
        <div tabindex="-1" contenteditable="false" data-cke-widget-wrapper="1" data-cke-filter="off" class="cke_widget_wrapper cke_widget_block cke_widget_ipsquote cke_widget_wrapper_ipsQuote cke_widget_focused cke_widget_selected" data-cke-display-name="blockquote" data-cke-widget-id="1" role="region" aria-label="Élément blockquote">
        <blockquote class="ipsQuote cke_widget_element" data-ipsquote="" data-gramm="false" data-ipsquote-timestamp="1669834058" data-ipsquote-userid="33940" data-ipsquote-username="PPAC" data-ipsquote-contentapp="forums" data-ipsquote-contenttype="forums" data-ipsquote-contentclass="forums_Topic" data-ipsquote-contentid="50419" data-ipsquote-contentcommentid="522096" data-cke-widget-keep-attr="0" data-widget="ipsquote" data-cke-widget-data="%7B%22classes%22%3A%7B%22ipsQuote%22%3A1%7D%7D">
        <div class="ipsQuote_citation">
        Le 30/11/2022 at 19:47, PPAC a dit&nbsp;:
        </div>
        <div class="ipsQuote_contents ipsClearfix cke_widget_editable" data-gramm="false" contenteditable="true" data-cke-widget-editable="content" data-cke-enter-mode="1">
        <p><br></p>
        <p><a class="ipsAttachLink ipsAttachLink_image" data-fileext="jpeg"
        data-fileid="153534" data-cke-saved-href="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.jpeg.f9da9cf270a773ee80fc05af8f0d4063.jpeg"
        href="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.jpeg.f9da9cf270a773ee80fc05af8f0d4063.jpeg"
        rel=""
        data-fullurl="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.jpeg.f9da9cf270a773ee80fc05af8f0d4063.jpeg">
        <img alt="image.thumb.jpeg.4c61ae5faf0a5cc547bc77f4c034ea20.jpeg"
        class="ipsImage ipsImage_thumbnailed" data-fileid="153534"
        data-ratio="75.00" style="width:400px;height:auto;" width="1000"
        data-cke-saved-src="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.thumb.jpeg.4c61ae5faf0a5cc547bc77f4c034ea20.jpeg" src="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.thumb.jpeg.4c61ae5faf0a5cc547bc77f4c034ea20.jpeg"></a>&nbsp;<a class="ipsAttachLink ipsAttachLink_image" data-fileext="jpeg" data-fileid="153535" data-cke-saved-href="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.jpeg.7dc02592a14247a15fceac98d1b1d63f.jpeg" href="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.jpeg.7dc02592a14247a15fceac98d1b1d63f.jpeg" rel="" data-fullurl="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.jpeg.7dc02592a14247a15fceac98d1b1d63f.jpeg"><img alt="image.thumb.jpeg.0aed4ee6ba3c1308b7c5735a15a8e2eb.jpeg" class="ipsImage ipsImage_thumbnailed" data-fileid="153535" data-ratio="75.00" style="width:400px;height:auto;" width="1000" data-cke-saved-src="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.thumb.jpeg.0aed4ee6ba3c1308b7c5735a15a8e2eb.jpeg" src="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.thumb.jpeg.0aed4ee6ba3c1308b7c5735a15a8e2eb.jpeg"></a>&nbsp;</p>
        </div>
        </blockquote><span class="cke_reset cke_widget_drag_handler_container" style="background: url(&quot;//www.lesimprimantes3d.fr/forum/applications/core/interface/ckeditor/ckeditor/plugins/widget/images/handle.png&quot;) rgba(220, 220, 220, 0.5); top: -15px; left: 0px;"><img class="cke_reset cke_widget_drag_handler" data-cke-widget-drag-handler="1" src="data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==" width="15" title="Cliquer et glisser pour déplacer" height="15" role="presentation"></span>
        </div><span data-cke-copybin-end="1">​</span>
         */
        //TODO a revoir pour generaliser
        Element append = elementToAppendTheCitationElementsCreated.append("<span data-cke-copybin-start=\"1\">\u200b</span>" + "<div " + "tabindex=\"-1\" " + "contenteditable=\"false\" " + "data-cke-widget-wrapper=\"1\" " + "data-cke-filter=\"off\" " + "class=\"cke_widget_wrapper cke_widget_block cke_widget_ipsquote cke_widget_wrapper_ipsQuote cke_widget_focused cke_widget_selected\" " + "data-cke-display-name=\"blockquote\" " + "data-cke-widget-id=\"1\" " + "role=\"region\" " + "aria-label=\"\u00c9l\u00e9ment blockquote\"" + ">\n" + "   <blockquote " + "class=\"ipsQuote cke_widget_element\" " + "data-ipsquote=\"\" " + "data-gramm=\"false\" " + "data-ipsquote-timestamp=\"" + getForumTimeStampFromForumDateTime(this.getCommentDateCreation()) + "\" " + "data-ipsquote-userid=\"" + this.commentAuteurId + "\" " + "data-ipsquote-username=\"" + this.commentAuteurNom // TODO surment a encodé ? ou les '"' a virer ou "proteger" car valeur d'un attribut d'un tag html ... ?
         + "\" " + "data-ipsquote-contentapp=\"forums\" " + "data-ipsquote-contenttype=\"forums\" " + "data-ipsquote-contentclass=\"forums_Topic\" " + "data-ipsquote-contentid=\"" + this.sujetId + "\" " + "data-ipsquote-contentcommentid=\"" + this.commentId + "\" " + "data-cke-widget-keep-attr=\"0\" " + "data-widget=\"ipsquote\" " + "data-cke-widget-data=\"%7B%22classes%22%3A%7B%22ipsQuote%22%3A1%7D%7D\">\n" + "    <div class=\"ipsQuote_citation\">\n" + citationMsg + "&nbsp;:\n" + "    </div>\n" + "    <div " + "class=\"ipsQuote_contents ipsClearfix cke_widget_editable\" " + "data-gramm=\"false\" contenteditable=\"true\" " + "data-cke-widget-editable=\"content\" " + "data-cke-enter-mode=\"1\">\n" + "    </div>\n" + "   </blockquote>" + "<span " + "class=\"cke_reset cke_widget_drag_handler_container\" " + "style=\"background: url(&quot;//www.lesimprimantes3d.fr/forum/applications/core/interface/ckeditor/ckeditor/plugins/widget/images/handle.png&quot;) rgba(220, 220, 220, 0.5); top: -15px; left: 0px;\">" + "<img class=\"cke_reset cke_widget_drag_handler\" " + "data-cke-widget-drag-handler=\"1\" " + "src=\"data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==\" " + "width=\"15\" title=\"Cliquer et glisser pour d\u00e9placer\" height=\"15\" " + "role=\"presentation\">" + "</span>\n" + "  </div>" + "<span data-cke-copybin-end=\"1\">\u200b</span>");
        Element resDivQuote = append.selectFirst("div.ipsQuote_contents");
        return resDivQuote;
    }

    /**
     * TODO a revoir trop de trucs codé en dur.( le style pas exemple ... )
    Pour ne citer que les images (non emojie) contenus dans le Coprs Du CommentaireBrut, avec modification du style de ses image pour les avoir en vignettes.
     * @param imgStyleToUse une valeur de style css, du genre de "width:auto;height:200px;" ou de "max-width:150px;max-height:150px;width: auto;height: auto;".
     * @return
     */
    public String createThumbsCitation(String imgStyleToUse) {
        String citationMsg = this.alImgsUrl.size() + " image(s) du commentaire de " + this.commentAuteurNom;
        Document docRes = Jsoup.parse("");
        Element bodyDocRes = docRes.body();
        Element resDivQuote = createElementAsCitation(bodyDocRes, citationMsg);
        Document docOrig = Jsoup.parse(getCommentCorpHTMLBrut());
        if (resDivQuote != null) {
            //selectFirst.appendChildren(docOrig.select("a.ipsAttachLink_image"));
            {
                Elements selectOriImgs = docOrig.select("img");
                if (selectOriImgs != null) {
                    for (Element e : selectOriImgs) {
                        Element imgParent = e.parent();
                        if (e.classNames().contains("ipsEmoji")) {
                            // Les caractères unicode emoji sont transformé en image par l'éditeur ...
                            // il faut donc ne pas afficher ses images car se sont les emojis
                        } else {
                            // donc voila une image
                            //bidouille pour avoir des vignettes ( mais implique que le client soit avec un navigateur compatible html5 ? )
                            e.attr("style", imgStyleToUse);
                            // TODO a revoir pour éviter de reprendre un lien malvélliant
                            // donc uniquement si le lien cible le forum !
                            if (imgParent.is("a") && imgParent.attr("href").startsWith("https://www.lesimprimantes3d.fr/")) {
                                resDivQuote.appendChild(imgParent);
                            } else {
                                // TODO pas un lien vers le forum alors mettre une bordure en rouge ou un logo de "warnig external link" ...
                                resDivQuote.appendChild(e);
                            }
                            boolean warnExternalImg = true;
                            if (!e.attr("src").startsWith("https://www.lesimprimantes3d.fr/") && warnExternalImg) {
                                resDivQuote.appendText("( ! img src externe " + e.attr("src") + " ) ");
                            } else {
                                resDivQuote.appendText(" ");
                            }
                        }
                    }
                }
            }
            Elements imgs = resDivQuote.select("img");
            if (imgs != null) {
                for (Element e : imgs) {
                    e.attr("style", imgStyleToUse);
                }
            }
        }
        return docRes.body().html();
    }
}
