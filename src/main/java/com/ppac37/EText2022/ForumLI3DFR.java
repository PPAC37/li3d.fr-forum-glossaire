/*
 */
package com.ppac37.EText2022;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pnikosis.html2markdown.HTML2Md;
import java.io.IOException;
import java.text.Collator;
import java.util.HashMap;
import java.util.Locale;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;

/**
 * Pour faire une copie local d'un message du forum lesimprimantes3d.fr.
 *
 * TODO extraction du message et vérification si contine ou non des liens de
 * telechargement de fichier ou des images ...
 * <p>
 * TODO : revoir l'utilisation de HTML2Md (pour les images ... comment proposer
 * une sauvegarde en local et garder les lien originaux ? c-a-d sourcer les
 * images ... ) A choisir modifier le document html pour avant l'export en .md
 * avoir fait une copie des images en local et avoir adapté les src="..." des
 * images ? Ou faire autrement ? ( dans HTML2Md encapsuler dans un commentaire ?
 * et faire le dl de limage et ajouter la nouvel image ? ) ? ??? et si j'a
 * besion de limiter la taille d'une image ? utiliser du code html ? ( pour
 * rester avec du markdown compatible avec github ! )
 *
 * <p>
 * Ajouter plus de bidouilles a HTML2Md pour garder les iframes ( revoie vers
 * sujet / message et video YouTube )? les liens des citation ?
 *
 * @author q6
 */
public class ForumLI3DFR {

    //import org.slf4j.Logger;
    //import org.slf4j.LoggerFactory;
    private static final Logger logger = LoggerFactory.getLogger(ForumLI3DFR.class);
    //logger.debug("version: {}", version);

    static boolean modeDev = true;

    /**
     *
     */
    public static final String HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754 = "https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/";

    static String[] urls = {// Jeux d'essai pour le dev.
        HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754 + "?sortby=date#comments"
            //,        "https://www.lesimprimantes3d.fr/forum/topic/50575-%F0%9F%8E%81-concours-de-no%C3%ABl-%F0%9F%8E%85%F0%9F%8C%B2-des-imprimantes-%C3%A0-gagner-%F0%9F%8E%81/"
    //             , ""    
    };

    /**
     *
     */
    static String lienVersCommentaireBase = HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754 + "?do=findComment&comment=";

    static SortedSet<ForumUneDef> lesDef = new TreeSet<>();
    static String enteteSommaireToUse = "";

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        logger.debug("debut: {}", ForumLI3DFR.class.getName());

        if (modeDev) {
            for (String sUrl : urls) {
                UrlCParserForum urlCParser = new UrlCParserForum(sUrl,true);
            }
        } else {
            // ?TODO gestion des arguments             
        }

        //
        System.out.printf("Nb comment with Def = %d\n", lesDef.size());

        UtilFileWriter fwIndexCommentMd = new UtilFileWriter("indexComment.md");

        // Sommaure mais au format .md
        boolean outDebugDefAlias = false;
        boolean outDebugCommentIdAndDefAlias = false;
        SortedMap<ForumUneDefAlias, String> aliasToId = new TreeMap<>();
        int cptTotalAlias = 0;
        for (ForumUneDef d : lesDef) {
            if (outDebugDefAlias) {
                System.out.printf(" %35s [%d](%s)\n", d.defNom, d.defNomAlias.size(), d.defNomAlias.toString());
            }

            if (outDebugCommentIdAndDefAlias) {
                System.out.printf(" comment-%s\t%-35s\t%d\t%s\n", d.commentId, d.defNom, d.defNomAlias.size(), d.defNomAlias.toString());
            }

            fwIndexCommentMd.append(String.format(" * [ ] [comment-%s - %s ](%s%s)\n", d.commentId, d.defNomAlias.toString(), lienVersCommentaireBase, d.commentId));

            cptTotalAlias += d.defNomAlias.size();
            for (String a : d.defNomAlias) {
                aliasToId.put(new ForumUneDefAlias(a), d.commentId);
            }
        }
        fwIndexCommentMd.flush();
        fwIndexCommentMd.close();

        boolean useSommaireEnteteHardCoded = false;
        // la pour un sommaire en html
        UtilFileWriter fwIndexOnlySommaireHtml = new UtilFileWriter("indexComment.html");
        fwIndexOnlySommaireHtml.append("<!DOCTYPE html>\n");
        fwIndexOnlySommaireHtml.append("<html lang=\"fr\">\n");
        fwIndexOnlySommaireHtml.append("<head>\n");
        fwIndexOnlySommaireHtml.append("<title>");
        fwIndexOnlySommaireHtml.append("Sommaire glossaire");
        fwIndexOnlySommaireHtml.append("</title>\n");
        fwIndexOnlySommaireHtml.append("</head>\n");
        fwIndexOnlySommaireHtml.append("<body>\n");
        if (useSommaireEnteteHardCoded) {
            // TODO entete a obtenir depuis un autre fichier et non codé en dur ici ou depuis la page en ligne
            fwIndexOnlySommaireHtml.append("<p> Dans ce glossaire de l'impression 3D, vous trouverez des définitions qui se veulent simples et compréhensibles des mots techniques, liés à l'impression 3D FDM et à l’impression 3D résine, utilisés par les membres du forum ainsi que sur le blog du site </p> \n"
                    + "<p> &nbsp; </p> \n"
                    + "<p> <span style=\"color:#ffffff;\"><span style=\"background-color:#c0392b;\">Ce glossaire est en cours d'élaboration.</span></span> </p> \n"
                    + "<p> <span style=\"color:#ffffff;\"><span style=\"background-color:#c0392b;\">Si vous voulez y participer,</span></span> <a href=\"https://www.lesimprimantes3d.fr/forum/topic/45962-cr%C3%A9ation-dun-glossaire-de-limpression-3d/\" rel=\"\">rendez vous sur ce sujet</a>. </p> \n"
                    + "<p> &nbsp; </p> \n"
                    + "<p> <span style=\"background-color:#ffff00;\">Afin de faciliter votre recherche, vous pouvez utiliser le moteur de recherche de votre navigateur accessible via l'appui simultané sur les touches CTRL et F</span> </p> \n"
                    + "<p> &nbsp; </p>\n");

        } else {
            // en principe si un commentaire contenent un <h2>Sommaire</h2> a etait parsé
            //se qui se trouvé avant le sommaire dans se commentaire a etait mis dans enteteSommaireToUse ...

            fwIndexOnlySommaireHtml.append(enteteSommaireToUse);
        }
        fwIndexOnlySommaireHtml.append(String.format("<h2 style=\"text-align:center;\" >%s</h2>\n", " Sommaire "));

        // la pour un sommaire en html avec les definition 
        UtilFileWriter fwIndexSommaireEtCommentHtml = new UtilFileWriter("indexSommaireEtComment.html");
        fwIndexSommaireEtCommentHtml.append("<!DOCTYPE html>\n");
        fwIndexSommaireEtCommentHtml.append("<html lang=\"fr\">\n");
        fwIndexSommaireEtCommentHtml.append("<head>\n");
        fwIndexSommaireEtCommentHtml.append("<title>");
        fwIndexSommaireEtCommentHtml.append("Glossaire");
        fwIndexSommaireEtCommentHtml.append("</title>\n");
        fwIndexSommaireEtCommentHtml.append("</head>\n");
        fwIndexSommaireEtCommentHtml.append("<body>\n");
        fwIndexSommaireEtCommentHtml.append(String.format("<h2 style=\"text-align:center;\" >%s</h2>\n", " Sommaire "));

        // la pour une autre version en html mais avec plus de lien pour navigation local ( TODO gestion de tous les liens est de elements externe ( images, iframe video, iframe vers sujet ou commentaire. ) 
        UtilFileWriter fwIndexHtml_avec_lien_et_id_pour_navigation_embarque = new UtilFileWriter("indexCommentEmbarq.html");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("<!DOCTYPE html>\n");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("<html lang=\"fr\">\n");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("<head>\n");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("<title>");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("Glossaire nav interne");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("</title>\n");
        //TODO style css <link rel="stylesheet" type="text/css" href="impression.css" media="print">
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("""
                                            <style>
                                               <!--
                                               
                                                  @media screen {
                                                       .warn { background-color :yellow;}             
                                                  
                                                  }
                                                  
                                                  @media print {
                                                      .notPrintable {
                                                          display: none;
                                                      }
                                                  /*
                                                      a:link:after, a:visited:after {
                                                          content: " (" attr(href) ") ";
                                                          font-size: 90%;
                                                      }
                                                  */
                                                      body { /* Modifications : la couleur de fond de page - la police - l'unité utilisée pour la taille de la police  */
                                                          background-color :
                                                              #fff;
                                                          font-family :Serif;
                                                          font-size :15pt;
                                                      }
                                                  
                                                      #page { /* Modifications : suppression de la bordure - marges */
                                                          margin :0;
                                                          border :none;
                                                      }
                                                  
                                                      #banner, #menuright, #footer { /* Les éléments qui ne seront pas affichés  */
                                                          display :none;
                                                      }
                                                  
                                                      h1#top { /* Affichage du titre */
                                                          margin :0;
                                                          padding :0;
                                                          text-indent :0;
                                                          line-height :25pt;
                                                          font-size :25pt;
                                                      }
                                                  
                                                      h2, h3, #contenu h3, #contenu a, a { /* Modification de la couleur des titres et liens */
                                                          color :
                                                              #000;
                                                      }
                                                  }
                                                  
                                                  @media screen, print {
                                                  
                                                  }
                                               -->
                                            </style>
                                            """);

        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("</head>\n");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("<body>\n");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<h2 style=\"text-align:center;\" id=\"debut\">%s</h2>\n", " Sommaire "));

        System.out.printf("Nb TotalAliasDef = %d\n", cptTotalAlias);

        // Pour générer une navigation dans le sommaire
        boolean outDebugNavCharGroupe = false;
        boolean outGroupeSize = true;
        String navigationSommaire = "";
        if (true) {
            String lastFirstChar = "";
            int ctpAfterLastChangeLastFirstChar = 0;
            Collator usCollator = Collator.getInstance(Locale.FRENCH);
            usCollator.setStrength(Collator.PRIMARY);
            for (ForumUneDefAlias k : aliasToId.keySet()) {

                // Pour mettre un titre3 avec le caractére de début du groupe            
                if (true) {
                    String tmpFisrtChar = k.a.substring(0, 1);
                    //TODO généraliser se fix
                    if ("É".equals(tmpFisrtChar)) {
                        tmpFisrtChar = "E";
                    }
                    ctpAfterLastChangeLastFirstChar++;
                    if (usCollator.compare(lastFirstChar, tmpFisrtChar) != 0) {
                        if (outGroupeSize && ctpAfterLastChangeLastFirstChar != 0 && !lastFirstChar.isEmpty()) {
                            navigationSommaire += String.format("(%d) - ", ctpAfterLastChangeLastFirstChar);
                        }
                        ctpAfterLastChangeLastFirstChar = 0;
                        if (outDebugNavCharGroupe) {
                            System.out.printf("%s\n", tmpFisrtChar);
                        }

                        navigationSommaire += String.format("<a href=\"#%s\">%s</a> ", tmpFisrtChar, tmpFisrtChar);
                    }

                    lastFirstChar = tmpFisrtChar;
                }
            }
            if (outGroupeSize && !lastFirstChar.isEmpty()) {
                navigationSommaire += String.format("(%d) . ", ctpAfterLastChangeLastFirstChar + 1);
            }
            fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<div>%s</div>\n", navigationSommaire));
        }

        // Pour générer les groupes du sommaire
        boolean outDebugCharGroupe = false;
        boolean outDebugAliasDefToIdComment = false;
        String lastFirstChar = "";
        Collator usCollator = Collator.getInstance(Locale.FRENCH);
        usCollator.setStrength(Collator.PRIMARY);
        for (ForumUneDefAlias k : aliasToId.keySet()) {

            // Pour mettre un titre3 avec le caractére de début du groupe            
            if (true) {
                String tmpFisrtChar = k.a.substring(0, 1);
                //TODO généraliser se fix
                if ("É".equals(tmpFisrtChar)) {
                    tmpFisrtChar = "E";
                }
                if (usCollator.compare(lastFirstChar, tmpFisrtChar) != 0) {

                    if (outDebugCharGroupe) {
                        System.out.printf("%s\n", tmpFisrtChar);
                    }

                    fwIndexOnlySommaireHtml.append(String.format("<h3>%s</h3>\n", tmpFisrtChar));
                    fwIndexSommaireEtCommentHtml.append(String.format("<h3>%s</h3>\n", tmpFisrtChar));
                    fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<h3 id=\"%s\">%s</h3>\n", tmpFisrtChar, tmpFisrtChar));
                }
                lastFirstChar = tmpFisrtChar;
            }

            if (outDebugAliasDefToIdComment) {
                System.out.printf(" %s\t%s\n", k.a, aliasToId.get(k));
            }

            fwIndexOnlySommaireHtml
                    .append(String.format("<a href=\"%s%s\">%s</a>\n<br>\n", lienVersCommentaireBase, aliasToId.get(k), k.a));

            fwIndexSommaireEtCommentHtml
                    .append(String.format("<a href=\"%s%s\" >%s</a>\n<br>\n", lienVersCommentaireBase, aliasToId.get(k), k.a));

            if (false) { // a vers forum
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<a href=\"%s%s\"  target=\"_blank\">%s</a>\n", lienVersCommentaireBase, aliasToId.get(k), k.a));
            } else {
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<a href=\"#%s\" >%s</a>\n", aliasToId.get(k), k.a));
            }
            fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                    .append(String.format("<br>\n"));
        }

        fwIndexOnlySommaireHtml
                .append(String.format("<br><p>Total %d alias pour %d définitions.</p>\n", cptTotalAlias, lesDef.size() - 1));// -1 pour le commentaire qui contien le sommaire qui n'a pas d'alias

        fwIndexSommaireEtCommentHtml
                .append(String.format("<br><p>Total %d alias pour %d définitions.</p>\n", cptTotalAlias, lesDef.size() - 1));// -1 pour le commentaire qui contien le sommaire qui n'a pas d'alias
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                .append(String.format("<br><p>Total %d alias pour %d définitions.</p>\n", cptTotalAlias, lesDef.size() - 1));// -1 pour le commentaire qui contien le sommaire qui n'a pas d'alias

        //
        // Les définition
        //
        //fwIndexHtml.append(String.format("<h2 style=\"text-align:center;\" >%s</h2>\n", " Definitions "));
//        fwIndexSommaireEtCommentHtml.append(String.format("<h2 style=\"text-align:center;\" >%s</h2>\n", " Definitions "));
//        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<h2 style=\"text-align:center;\" >%s</h2>\n", " Definitions "));
        for (ForumUneDef d : lesDef) {
            if (false) {
                System.out.printf(" %35s [%d](%s)\n", d.defNom, d.defNomAlias.size(), d.defNomAlias.toString());
            }

            if (false) {
                System.out.printf(" comment-id %s\t%-35s\t%d\t%s\n", d.commentId, d.defNom, d.defNomAlias.size(), d.defNomAlias.toString());
            }

            if (d.defNomAlias.isEmpty()) {

                fwIndexSommaireEtCommentHtml
                        .append(String.format("<hr><a href=\"%s%s\" target=\"_blank\" >comment-id %s :: %s</a>\n", lienVersCommentaireBase, d.commentId, d.commentId, d.defNomAlias));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<hr><a id=\"%s\" target=\"_blank\" href=\"%s%s\">comment-id %s :: %s</a> // <a href=\"#debut\" style=\"text-align:center;\" >(retour sommaire local)</a>\n", d.commentId, lienVersCommentaireBase, d.commentId, d.commentId, d.defNomAlias));
                //.append(String.format("<div>%s</div>\n<br>\n", ));
                fwIndexSommaireEtCommentHtml
                        .append(String.format("<div><details><summary>...</summary>%s</details></div>\n", d.commentCorpHTML));

                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<div><code>C %s %s</code></div>\n", d.commentDateCreation, d.commentAuteurNom));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<div><code>M %s %s</code></div>\n", d.commentModifDate, d.commentModifParNom));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<div><details><summary>...</summary>%s</details></div>\n", d.commentCorpHTML));

            } else {
                fwIndexSommaireEtCommentHtml
                        .append(String.format("<hr><a href=\"%s%s\" target=\"_blank\" >comment-id %s :: %s</a>\n", lienVersCommentaireBase, d.commentId, d.commentId, d.defNomAlias));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<hr><a id=\"%s\" href=\"%s%s\" target=\"_blank\">comment-id %s :: %s</a> // <a href=\"#debut\" style=\"text-align:center;\" >(retour sommaire local)</a>\n", d.commentId, lienVersCommentaireBase, d.commentId, d.commentId, d.defNomAlias));
                //.append(String.format("<div>%s</div>\n<br>\n", ));
                fwIndexSommaireEtCommentHtml
                        .append(String.format("<div>%s</div>\n", d.commentCorpHTML));

                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<div><code>C %s %s</code></div>\n", d.commentDateCreation, d.commentAuteurNom));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<div><code>M %s %s</code></div>\n", d.commentModifDate, d.commentModifParNom));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<div>%s</div>\n", d.commentCorpHTML));
            }

            if (false) {
                fwIndexSommaireEtCommentHtml.
                        append(String.format(" comment-id %s\t%-35s\t%d\t%s\n", d.commentId, d.defNom, d.defNomAlias.size(), d.defNomAlias.toString()));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.
                        append(String.format(" comment-id %s\t%-35s\t%d\t%s\n", d.commentId, d.defNom, d.defNomAlias.size(), d.defNomAlias.toString()));
            }
        }

        fwIndexOnlySommaireHtml.append("</body>\n");
        fwIndexOnlySommaireHtml.append("</html>\n");
        fwIndexOnlySommaireHtml.flush();
        fwIndexOnlySommaireHtml.close();

        fwIndexSommaireEtCommentHtml.append("</body>\n");
        fwIndexSommaireEtCommentHtml.append("</html>\n");
        fwIndexSommaireEtCommentHtml.flush();
        fwIndexSommaireEtCommentHtml.close();

        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("</body>\n");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("</html>\n");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.flush();
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.close();

        System.out.printf("FIN : %s\n", ForumLI3DFR.class.getName());
        System.out.flush();
    }

    /**
     * TODO a revoir - Séparer les niveau d'abstraction ( Parse vs Reformatage
     * par creation de multiple class d'heriage ... bien décomposer en méthodes
     * ... )
     *
     * @param sUrl
     * @return
     * @throws IOException
     */
    public static Document loadMayByCachedDocumentFromUrl(String sUrl) throws IOException {

        HashMap<String, String> mapUrlElem = new HashMap<>();

        long t0 = System.currentTimeMillis();
        //Connection connect = Jsoup.connect(sUrlSectionPetg);

        Document doc = UrlCDownloderCache.cacheAndParseUrl(sUrl, false, UrlCDownloderCache.debugPrintUrlHeaders, false);//connect.get();
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);

        long t1 = System.currentTimeMillis();
        System.out.println(sUrl + " ( in " + (t1 - t0) + " ms.)");
        //TODO si cache ex: Document doc = Jsoup.parse(bookmarkHtmlFile, "UTF-8");

        if (false) {
            Elements titles = doc.select("title");
            //print all titles in main page
            for (Element e : titles) {
                System.out.println("text: " + e.text());
                if (false) {
                    System.out.println("html: " + e.html());
                }
            }
        }

        long t3 = System.currentTimeMillis();
        if (false) {
            System.out.println("in " + (t3 - t1) + " ms.");
        }

        if (false) {
            UrlCParser.outDocumentHeadMetaProperty(doc);
        }

        // Le fil d'arian :: 
        // Si il y a un fil d'ariane ( " Acceuil > NomSection [ > SousSection ] > TitreSujet " )
        // "Accueil > Les imprimantes 3D > Discussion sur les imprimantes 3D > Glossaire de l'impression 3D"
        boolean outFilDArian = false;
        if (outFilDArian) {
            String sSelectNavBar = "nav.ipsBreadcrumb_top  > ul[data-role=breadcrumbList] > li";
            Elements elemNavBar = doc.select(sSelectNavBar);
            System.out.println("Total (" + sSelectNavBar + "): " + elemNavBar.size());
            for (Element e : elemNavBar) {
                System.out.printf("  >%d %s\n", e.childrenSize(), e.html());
            }
        }

        // les elements de class "article" (sont les commentaire) 
        // et là on va directement chercher le sous element proche du coprs du commentaire
        boolean doComments = true;
        if (doComments) {
            String sSelect_A_id_comment = "a[id^=comment-]";
            Elements allElemAIdComment = doc.select(sSelect_A_id_comment);

            if (false) {
                System.out.println("Total (" + sSelect_A_id_comment + "): " + allElemAIdComment.size());
            }

            for (Element eACommentId : allElemAIdComment) {

                ForumUneDef uneDef = new ForumUneDef();

                Element nextElementSibling = eACommentId.nextElementSibling();
                if (false) {
                    System.out.printf(" \"%s\"\n", eACommentId.attr("id"));
                }

                String tmpCommentId = "";
                if (eACommentId.attr("id").startsWith("comment-")) {
                    tmpCommentId = eACommentId.attr("id").substring(8);
                    if (false) {
                        System.out.printf("%s%s\n", lienVersCommentaireBase, tmpCommentId);
                    }
                    if (false) {
                        System.out.printf(" comment id \"%s\"\n", tmpCommentId);
                    }

                    uneDef.setCommentId(tmpCommentId);
                }

                // le nom de l'auteur et le lien vers son profil
                Element h3AAuteur = nextElementSibling.selectFirst("h3.cAuthorPane_author > a");
                if (true) {
                    System.out.printf("  Auteur : %s ( %s )\n", h3AAuteur.text(), h3AAuteur.attr("abs:href"));
                }
                uneDef.setCommentAuteurNom(h3AAuteur.text());
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
                            System.out.printf("  //  %s ( %s ) %s :: %s\n", d.attr("title"), d.text(), d.attr("datetime"), d.parent().text());
                        } else {
                            uneDef.setDateModification(sDateTime);
                            System.out.printf("  //  %s ( %s ) %s :: %s // \"%s\"\n", d.attr("title"), d.text(), d.attr("datetime"), d.parent().text(), d.nextSibling().toString());
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
                    Elements selectCommentImg = commentContent.select("img[src]");
                    for (Element cimg : selectCommentImg) {
                        String imgSrc = cimg.attr("src");
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
                    Elements elemsA = commentContent.select("a[href~=" + HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754 + "]");
                    if (elemsA != null) {
                        for (Element eA : elemsA) {
                            String eAHref = eA.attr("href");
                            if (eAHref.length() > HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754.length()) {

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
                    elemTitreSommaire.nextElementSiblings().remove();//.attr("style", "background-color:#c0392b;");
                    elemTitreSommaire.remove();

                    if (false) {
                        System.out.printf("%s\n", commentContent.html());
                    }
                    enteteSommaireToUse = commentContent.html();
                }

                lesDef.add(uneDef);

                // On essais de convertire le Commentaire au format .md ( TODO revoir le dl des images, couleurs du texte , couleur de fond du texte , ... )
                if (false) {
                    System.out.printf("---\n%s\n---\n", HTML2Md.getTextContent(commentContent)); //HTML2Md.convertHtml(commentContent.html(), "UTF-8"));
                }
                if (false) {
                    System.out.println();
                }
            }// fin boucle pour chaque commentaire 

            System.out.println();

        }

        System.out.flush();

        return doc;
    }

}
