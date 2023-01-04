/*
 */
package com.ppac37.EText2022;

import com.pnikosis.html2markdown.HTML2Md;
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

    String commentDateCreation;

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

    //
    //
    //
    public static void parseComment(Element eACommentId, ForumUneDef uneDef) {
        Element nextElementSibling = eACommentId.nextElementSibling();
        if (false) {
            System.out.printf(" \"%s\"\n", eACommentId.attr("id"));
        }
        String tmpCommentId = "";
        if (eACommentId.attr("id").startsWith("comment-")) {
            tmpCommentId = eACommentId.attr("id").substring(8);

            if (false) {
                System.out.printf(" comment id \"%s\"\n", tmpCommentId);
            }
            uneDef.setCommentId(tmpCommentId);
        }

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
            uneDef.setReactionsTotals(totalR);

        }

        // le nom de l'auteur et le lien vers son profil
        Element h3AAuteur = nextElementSibling.selectFirst("h3.cAuthorPane_author > a");
        if (false) {
            System.out.printf("  Auteur : %s ( %s )\n", h3AAuteur.text(), h3AAuteur.attr("abs:href"));
        }
        uneDef.setCommentAuteurNom(h3AAuteur.text());
        uneDef.setCommentAuteurIdFromUrl(h3AAuteur.attr("abs:href"));
        // l'image de l'avatard de l'auteur
        Element auteurImg = nextElementSibling.selectFirst("div.cAuthorPane_photoWrap > a > img");
        if (false) {
            System.out.printf("  Auteur : %s ( %s )\n", auteurImg.attr("alt"), auteurImg.attr("src"));
        }
        // group
        Element auteurGroup = nextElementSibling.selectFirst("li[data-role=group]");
        if (false) {
            System.out.printf("  Auteur :   ( %s )\n", auteurGroup.text());
        }
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
                    uneDef.setDateCreation(sDateTime);
                    if (false) {
                        System.out.printf("  //  %s ( %s ) %s :: %s\n", d.attr("title"), d.text(), d.attr("datetime"), d.parent().text());
                    }
                } else {
                    uneDef.setDateModification(sDateTime);
                    if (false) {
                        System.out.printf("  //  %s ( %s ) %s :: %s // \"%s\"\n", d.attr("title"), d.text(), d.attr("datetime"), d.parent().text(), d.nextSibling().toString());
                    }
                    uneDef.setParModification(d.nextSibling().toString().substring(5));
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
        // le corps du commentaire
        Element commentContent = nextElementSibling.selectFirst("div[data-role=commentContent]");
        uneDef.setCommentCorpHTMLBrut(commentContent.html());
        if (true) {
            // L'ensemble des images pour ou non les sauver en local et modifier le src
            // pour le concours je n'est normalement pas a prednre en compte lesles citations...

            Document parseBodyFragment = Jsoup.parseBodyFragment(commentContent.html());

            // TODO il me faudrais tout de même avoir le nombre d'image total ( pour vérification )
            boolean separationDesImageEnCitation = true;
            if (separationDesImageEnCitation) {
                Elements selectCommentImgInCitation = parseBodyFragment.select("blockquote.ipsQuote img, div.ipsQuote_contents img");
                for (Element cimg : selectCommentImgInCitation) {

                    String imgSrc = cimg.attr("src");
                    if (cimg.hasClass("ipsEmoji")) {
                        // exception des emoji
                    } else {
                        uneDef.alImgsUrlDansCitation.add(imgSrc);
                    }
                }
                selectCommentImgInCitation.remove();
            }

            Elements selectCommentImg = parseBodyFragment.select("img[src]");
            for (Element cimg : selectCommentImg) {

                String imgSrc = cimg.attr("src");
                if (cimg.hasClass("ipsEmoji")) {
                    // exception des emoji
                } else {
                    uneDef.alImgsUrl.add(imgSrc);
                }
                // voila là il est posible de modifier le src ...
                //cimg.attr("src", "cacheBaseDir");
                // TODO en fait je voudrais garder les liens d'origine pour les mettre en "sources" (ou commentaire HTML
                // mais donc là ...
                // avoir le répertoire de mise en cache ?
                // ? utiliser ImageIO pour le telechargement ou le faire a coup de HttpRequest ?
            }
        }
        //
        if (false) {
            // pour debug
            System.out.printf("---\n%s\n---\n", commentContent.html());
            // TODO a revoir actuellement ma version de HTML2Md rapatrie les images dans un repertoire codé en dur et sans bien faire attention au posible ecrasement de deux images ayant le même nom ...
            System.out.printf("---\n%s\n---\n", HTML2Md.getTextContent(commentContent)); //HTML2Md.convertHtml(commentContent.html(), "UTF-8"));
        }
        //
        // TODO codage a revoir car on passe encore a un autre niveau abstraction ,  spécifique au glossaire.
        //
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
                        uneDef.setDefNom("0_" + c_elemH2.text());
                    } else {
                        System.out.printf(" H2: \"%s\"\n", c_elemH2.text());
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
                    uneDef.addDefNom(c_elemH2.text());
                }
            }
        }
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
        uneDef.setCommentCorpHTML(commentContent.html());
        if (elemTitreSommaire != null) {
            // Pour réutiliser l'entete du sommaire, on supprime le sommaire ! ?
            elemTitreSommaire.nextElementSiblings().remove(); //.attr("style", "background-color:#c0392b;");
            elemTitreSommaire.remove();
            if (false) {
                System.out.printf("%s\n", commentContent.html());
            }
            ForumLI3DFR.enteteSommaireToUse = commentContent.html();
        }
        ForumLI3DFR.lesDef.add(uneDef);
        // On essais de convertire le Commentaire au format .md ( TODO revoir le dl des images, couleurs du texte , couleur de fond du texte , ... )
        if (false) {
            System.out.printf("---\n%s\n---\n", HTML2Md.getTextContent(commentContent)); //HTML2Md.convertHtml(commentContent.html(), "UTF-8"));
        }
        if (false) {
            System.out.println();
        }
    }
}
