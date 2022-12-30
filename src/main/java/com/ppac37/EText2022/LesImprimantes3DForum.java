/*
 */
package com.ppac37.EText2022;

import com.pnikosis.html2markdown.HTML2Md;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.Collator;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
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
public class LesImprimantes3DForum {

    static String cacheBaseDir = "./www_cache_w/";//TODO a revoir car va changer dans la dist 

    public static NumberFormat numberFormat = NumberFormat.getInstance();
    public static DateFormat dateFormat = DateFormat.getInstance();

    public static boolean debugPrintUrlHeaders = false;
    public static boolean debugTimming = false;
    static boolean modeDev = true;

    public static final String HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754 = "https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/";

    static String[] urls = {// Jeux d'essai pour le dev.
        HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754 + "?sortby=date#comments"
//            ,
//        HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754 + "page/2/?sortby=date#comments",
//        HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754 + "page/3/?sortby=date#comments"
    //             , ""    
    };

    /**
     *
     */
    static String lienVersCommentaireBase = HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754 + "?do=findComment&comment=";

    static SortedSet<UneDef> lesDef = new TreeSet<>();
    static String enteteSommaireToUse = "";

    public static void main(String[] args) {

        System.out.printf("DEBUT : %s\n", LesImprimantes3DForum.class.getName());

        boolean doToLastPage = true;
        if (modeDev) {
            for (String sUrl : urls) {
                System.out.printf(" %s\n", sUrl);
                try {
                    // TODO analyse de l'url (si ou non une parties de parametres)
                    Document doc = loadMayByCachedDocumentFromUrl(sUrl);
                    if (doToLastPage) {
                        if (true) {
                            // TODO int pageMax = getSujetMaxPage(doc);
                            int iPageNumActive = -1;
                            int iPageNumLas = -1;
                            String sUrlNextPage = "";

                            Element pActive = doc.selectFirst("li.ipsPagination_active a");
                            if (pActive != null) {
                                String sNumPage = pActive.attr("data-page");

                                System.out.printf("Page %s\n", sNumPage);
                                try {
                                    iPageNumActive = Integer.parseInt(sNumPage);
                                } catch (NumberFormatException e) {
                                }
                            }

                            Element pNext = doc.selectFirst("li.ipsPagination_next a");
                            if (pNext != null) {
                                String sNumPage = pNext.attr("data-page");

                                System.out.printf("Page Next %s\n", sNumPage);
                                sUrlNextPage = pNext.attr("abs:href");
                            }

                            Element pLast = doc.selectFirst("li.ipsPagination_last a");
                            if (pLast != null) {
                                String sNumPage = pLast.attr("data-page");
                                System.out.printf("Last Page %s\n", sNumPage);
                                try {
                                    iPageNumLas = Integer.parseInt(sNumPage);
                                } catch (NumberFormatException e) {
                                }
                            }

                            if (iPageNumActive < iPageNumLas) {
                                for (int numPage = (iPageNumActive + 1); numPage <= iPageNumLas; numPage++) {
                                    String sUrlOtherPageToDo = sUrlNextPage.replaceFirst("/page/.*/", "/page/" + numPage + "/");
                                    System.out.printf("WillDo Next Url %s\n", sUrlOtherPageToDo);
                                    loadMayByCachedDocumentFromUrl(sUrlOtherPageToDo);
                                }
                            }

                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(LesImprimantes3DForum.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            // TODO gestion des arguments             
        }

        //
        System.out.printf("Nb comment with Def = %d\n", lesDef.size());

        UtilFileWriter fwIndexCommentMd = new UtilFileWriter("indexComment.md");

        //
        SortedMap<UneDefAlias, String> aliasToId = new TreeMap<>();
        int cptTotalAlias = 0;
        for (UneDef d : lesDef) {
            if (false) {
                System.out.printf(" %35s [%d](%s)\n", d.defNom, d.defNomAlias.size(), d.defNomAlias.toString());
            }

            if (false) {
                System.out.printf(" comment-%s\t%-35s\t%d\t%s\n", d.commentId, d.defNom, d.defNomAlias.size(), d.defNomAlias.toString());
            }

            fwIndexCommentMd.append(String.format(" * [ ] [comment-%s - %s ](%s%s)\n", d.commentId, d.defNomAlias.toString(), lienVersCommentaireBase, d.commentId));

            cptTotalAlias += d.defNomAlias.size();
            for (String a : d.defNomAlias) {
                aliasToId.put(new UneDefAlias(a), d.commentId);
            }
        }
        fwIndexCommentMd.flush();
        fwIndexCommentMd.close();

        // la pour un sommaire en html
        UtilFileWriter fwIndexOnlySommaireHtml = new UtilFileWriter("indexComment.html");
        if (false) {
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
        fwIndexSommaireEtCommentHtml.append(String.format("<h2 style=\"text-align:center;\" >%s</h2>\n", " Sommaire "));
        // la pour une autre version en html mais avec plus de lien pour navigation local ( TODO gestion de tous les liens est de elements externe ( images, iframe video, iframe vers sujet ou commentaire. ) 
        UtilFileWriter fwIndexHtml_avec_lien_et_id_pour_navigation_embarque = new UtilFileWriter("indexCommentEmbarq.html");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<h2 style=\"text-align:center;\" id=\"debut\">%s</h2>\n", " Sommaire "));

        System.out.printf("Nb TotalAliasDef = %d\n", cptTotalAlias);

        // Pour générer une navigation dans le sommaire
        // TODO a revoir pour éventuellement avoir le nombre d'elements dans chaque groupe... (mais cela change l'approche )
        String navigationSommaire = "";
        if (true) {
            String lastFirstChar = "";
            int ctpAfterLastChangeLastFirstChar = 0;
            Collator usCollator = Collator.getInstance(Locale.FRENCH);
            usCollator.setStrength(Collator.PRIMARY);
            for (UneDefAlias k : aliasToId.keySet()) {

                // Pour mettre un titre3 avec le caractére de début du groupe            
                if (true) {
                    String tmpFisrtChar = k.a.substring(0, 1);
                    //TODO généraliser se fix
                    if ("É".equals(tmpFisrtChar)) {
                        tmpFisrtChar = "E";
                    }
                    ctpAfterLastChangeLastFirstChar++;
                    if (usCollator.compare(lastFirstChar, tmpFisrtChar) != 0) {
                        if (ctpAfterLastChangeLastFirstChar != 0 && !lastFirstChar.isEmpty()) {
                            navigationSommaire += String.format("(%d) - ", ctpAfterLastChangeLastFirstChar);
                        }
                        ctpAfterLastChangeLastFirstChar = 0;
                        if (false) {
                            System.out.printf("%s\n", tmpFisrtChar);
                        }

                        navigationSommaire += String.format("<a href=\"#%s\">%s</a> ", tmpFisrtChar, tmpFisrtChar);
                    }

                    lastFirstChar = tmpFisrtChar;
                }
            }
            if (!lastFirstChar.isEmpty()) {
                navigationSommaire += String.format("(%d) . ", ctpAfterLastChangeLastFirstChar);
            }
            fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<div>%s</div>\n", navigationSommaire));
        }

        // Pour générer les groupes du sommaire
        String lastFirstChar = "";
        Collator usCollator = Collator.getInstance(Locale.FRENCH);
        usCollator.setStrength(Collator.PRIMARY);
        for (UneDefAlias k : aliasToId.keySet()) {

            // Pour mettre un titre3 avec le caractére de début du groupe            
            if (true) {
                String tmpFisrtChar = k.a.substring(0, 1);
                //TODO généraliser se fix
                if ("É".equals(tmpFisrtChar)) {
                    tmpFisrtChar = "E";
                }
                if (usCollator.compare(lastFirstChar, tmpFisrtChar) != 0) {

                    if (false) {
                        System.out.printf("%s\n", tmpFisrtChar);
                    }

                    fwIndexOnlySommaireHtml.append(String.format("<h3>%s</h3>\n", tmpFisrtChar));
                    fwIndexSommaireEtCommentHtml.append(String.format("<h3>%s</h3>\n", tmpFisrtChar));
                    fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<h3 id=\"%s\">%s</h3>\n", tmpFisrtChar, tmpFisrtChar));
                }
                lastFirstChar = tmpFisrtChar;
            }

            if (false) {
                System.out.printf(" %s\t%s\n", k.a, aliasToId.get(k));
            }
            fwIndexOnlySommaireHtml
                    .append(String.format("<a href=\"%s%s\">%s</a>\n<br>\n", lienVersCommentaireBase, aliasToId.get(k), k.a));

            fwIndexSommaireEtCommentHtml
                    .append(String.format("<a href=\"%s%s\" >%s</a>\n<br>\n", lienVersCommentaireBase, aliasToId.get(k), k.a));
            fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                    .append(String.format("<a href=\"%s%s\"  target=\"_blank\">%s</a> <a href=\"#%s\" >(local)</a>\n<br>\n", lienVersCommentaireBase, aliasToId.get(k), k.a, aliasToId.get(k)));
        }

        fwIndexOnlySommaireHtml
                .append(String.format("<br><p>Total %d alias pour %d définitions.</p><br>\n", cptTotalAlias, lesDef.size() - 1));// -1 pour le commentaire qui contien le sommaire qui n'a pas d'alias

        fwIndexSommaireEtCommentHtml
                .append(String.format("<br><p>Total %d alias pour %d définitions.</p><br>\n", cptTotalAlias, lesDef.size() - 1));// -1 pour le commentaire qui contien le sommaire qui n'a pas d'alias
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                .append(String.format("<br><p>Total %d alias pour %d définitions.</p><br>\n", cptTotalAlias, lesDef.size() - 1));// -1 pour le commentaire qui contien le sommaire qui n'a pas d'alias

        //
        //
        //fwIndexHtml.append(String.format("<h2 style=\"text-align:center;\" >%s</h2>\n", " Definitions "));
        fwIndexSommaireEtCommentHtml.append(String.format("<h2 style=\"text-align:center;\" >%s</h2>\n", " Definitions "));
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<h2 style=\"text-align:center;\" >%s</h2>\n", " Definitions "));
        for (UneDef d : lesDef) {
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
                        .append(String.format("<div><details><summary>...</summary>%s</details></div>\n<br>\n", d.commentCorpHTML));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<div><details><summary>...</summary>%s</details></div>\n<br>\n", d.commentCorpHTML));

            } else {
                fwIndexSommaireEtCommentHtml
                        .append(String.format("<hr><a href=\"%s%s\" target=\"_blank\" >comment-id %s :: %s</a>\n", lienVersCommentaireBase, d.commentId, d.commentId, d.defNomAlias));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<hr><a id=\"%s\" href=\"%s%s\" target=\"_blank\">comment-id %s :: %s</a> // <a href=\"#debut\" style=\"text-align:center;\" >(retour sommaire local)</a>\n", d.commentId, lienVersCommentaireBase, d.commentId, d.commentId, d.defNomAlias));
                //.append(String.format("<div>%s</div>\n<br>\n", ));
                fwIndexSommaireEtCommentHtml
                        .append(String.format("<div>%s</div>\n<br>\n", d.commentCorpHTML));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<div>%s</div>\n<br>\n", d.commentCorpHTML));
            }

            if (false) {
                fwIndexSommaireEtCommentHtml.
                        append(String.format(" comment-id %s\t%-35s\t%d\t%s\n", d.commentId, d.defNom, d.defNomAlias.size(), d.defNomAlias.toString()));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.
                        append(String.format(" comment-id %s\t%-35s\t%d\t%s\n", d.commentId, d.defNom, d.defNomAlias.size(), d.defNomAlias.toString()));
            }
        }

        fwIndexOnlySommaireHtml.flush();
        fwIndexOnlySommaireHtml.close();

        fwIndexSommaireEtCommentHtml.flush();
        fwIndexSommaireEtCommentHtml.close();

        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.flush();
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.close();

        System.out.printf("FIN : %s\n", LesImprimantes3DForum.class.getName());
        System.out.flush();
    }

    public static Document loadMayByCachedDocumentFromUrl(String sUrl) throws IOException {

        HashMap<String, String> mapUrlElem = new HashMap<>();

        long t0 = System.currentTimeMillis();
        //Connection connect = Jsoup.connect(sUrlSectionPetg);

        Document doc = cacheAndParseUrl(sUrl, false, debugPrintUrlHeaders, false);//connect.get();
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
            outDocumentHeadMetaProperty(doc);
        }

        if (true) {
            // TODO int pageMax = getSujetMaxPage(doc);
            int page_max = 1;
            Element pActive = doc.selectFirst("li.ipsPagination_active a");
            if (pActive != null) {
                String sNumPage = pActive.attr("data-page");

                System.out.printf("Page %s\n", sNumPage);
            }
            Element pLast = doc.selectFirst("li.ipsPagination_last a");
            if (pLast != null) {
                String sNumPage = pLast.attr("data-page");
                System.out.printf("Last Page %s\n", sNumPage);
            }

        }

        //nav.ipsBreadcrumb  > <ul data-role="breadcrumbList"> li ...
        if (false) {
            String sSelectNavBar = "nav.ipsBreadcrumb_top  > ul[data-role=breadcrumbList] > li";
            Elements elemNavBar = doc.select(sSelectNavBar);
            System.out.println("Total (" + sSelectNavBar + "): " + elemNavBar.size());

            for (Element e : elemNavBar) {
                System.out.printf("  >%d %s\n", e.childrenSize(), e.html());

            }
        }

        // Faud t'il traiter les commentaire un a un ?
        boolean doComments = true;
        if (doComments) {
            String sSelect_A_id_comment = "a[id^=comment-]";
            Elements allElemAIdComment = doc.select(sSelect_A_id_comment);

            if (false) {
                System.out.println("Total (" + sSelect_A_id_comment + "): " + allElemAIdComment.size());
            }

            for (Element eACommentId : allElemAIdComment) {

                UneDef uneDef = new UneDef();

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
                        } else {
                            uneDef.setDateModification(sDateTime);
                        }
                        System.out.printf("  //  %s ( %s ) %s :: %s\n", d.attr("title"), d.text(), d.attr("datetime"), d.parent().text());

                    }
                    sTmpLastDateTime = sDateTime;
                }

                // le corps du commentaire
                Element commentContent = nextElementSibling.selectFirst("div[data-role=commentContent]");

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
                    System.out.printf("---\n%s\n---\n", HTML2Md.getTextContent(commentContent)); //HTML2Md.convertHtml(commentContent.html(), "UTF-8"));
                }

                uneDef.setCommentCorpHTML(commentContent.html());

                // voir le le coprs du commentaire contien un titre 2
                if (true && commentContent != null) {
                    Elements selectCommentH2 = commentContent.select("h2");
                    if (!selectCommentH2.isEmpty()) {
                        // le commentaire contien au moins un "titre 2"
                        for (Element c_elemH2 : selectCommentH2) {
                            if (c_elemH2.text().equalsIgnoreCase("Sommaire")) {
                                System.out.printf(" H2: \"%s\" comment id %s\n", c_elemH2.text(), eACommentId.attr("id").substring(8));
                                uneDef.setDefNom("0_" + c_elemH2.text());

                                // Pour réutiliser l'entete du sommaire, on supprime le sommaire ! ?
                                c_elemH2.nextElementSiblings().remove();
                                c_elemH2.remove();

                                if (false) {
                                    System.out.printf("%s\n", commentContent.html());
                                }
                                enteteSommaireToUse = commentContent.html();
                            } else {
                                System.out.printf(" H2: \"%s\"\n", c_elemH2.text());

                            }
                        }
                    }

                }

                // les elements en gras qui sont en puce ( spécifique au glossaire pour identifier le terme définie ... ) 
                if (true && commentContent != null) {
                    Elements selectCommentH2 = commentContent.select("ul li strong");
                    if (!selectCommentH2.isEmpty()) {
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

    //
    // 
    //
    private static void outDocumentHeadMetaProperty(Document docsub) {
        //og
        String sSelectMetaProp = "head > meta[property]";
        Elements docsubScriptMP = docsub.select(sSelectMetaProp);
        System.out.println("Total (" + sSelectMetaProp + "): " + docsubScriptMP.size());
        for (Element l : docsubScriptMP) {
            Attributes attributes = l.attributes();
            if (attributes.size() == 2 && attributes.hasKey("property") && attributes.hasKey("content")) {

                String aPropVal = l.attr("property");
                String aContVal = l.attr("content");
                System.out.printf("%s = %s\n", aPropVal, aContVal);
            } else {
                System.out.println(l.outerHtml());
            }
        }
        //
        System.out.println();
        System.out.flush();
    }

    //
    //
    //
    private static void urlHttpConnecteAndWriteToFile(URL url, File fCache) {
        //FileWriter fw = new FileWriter(fCache);

        // TODO prise en compte des redirection ( avoir la chaine de redirection ...)
        //HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection.setFollowRedirects(true);

        // TODO gestion des cookies ... 
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (false) {
                //TODO pour pouvoir etre authentifié ... on pique les cookies via un inspecter , network , ...  le curl que l'on sauve dans un fichier
                // il reste a parser la req curl pour faire les bon connection.setRequestProperty("","");
                try {

                    File file = new File("/home/q6/req_curl_PPAC.txt");
                    Scanner scr = null;
                    int count = 0;
                    try {
                        scr = new Scanner(file);
                        if (false) {
                            // SSI sous windows 
                            scr.useDelimiter("\r\n");
                        }
                        while (scr.hasNextLine()) {

                            count++;
                            String sTmpLine = scr.nextLine();
                            if (false) {
                                System.out.printf("line %4d [%4d]:  %s\n", count, sTmpLine.length(), sTmpLine);
                            }
                            if (sTmpLine.startsWith("  -H '")) {
                                int posSeparatorKeyValue = sTmpLine.indexOf(": ");
                                String k = sTmpLine.substring(6, posSeparatorKeyValue);
                                String val = sTmpLine.substring(posSeparatorKeyValue + 2, sTmpLine.length() - 3);

                                if (false) {
                                    System.out.printf(">%s=%s\n", k, val);
                                }
                                connection.setRequestProperty(k, val);
                            } else {
                                if (false) {
                                    System.out.printf("#%s\n", sTmpLine);
                                }
                            }

                        }
                        if (false) {
                            System.out.println(count);
                        }
                        scr.close();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(LesImprimantes3DForum.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (Exception e) {
                    //... TODO mais pas critique on ne sera ju
                    Logger.getLogger(LesImprimantes3DForum.class.getName()).log(Level.SEVERE, null, e);
                }

            }
            //connection.setRequestProperty ( "User-agent", "Opera/9.80 (Windows NT 5.1; U; fr) Presto/2.7.62 Version/11.01");
            //        connection.setRequestProperty("User-agent", "Nokia6230i/2.0 (03.25) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            Map<String, List<String>> requestProperties = connection.getRequestProperties();
            System.out.println(requestProperties.toString());

            String header = connection.getHeaderField(0);
            System.out.println(header);
            System.out.println("---Start of headers---");
            int i = 1;
            while ((header = connection.getHeaderField(i)) != null) {
                String key = connection.getHeaderFieldKey(i);
                System.out.println(((key == null) ? "" : key + ": ") + header);
                i++;
            }
            System.out.println("---End of headers---");

            if (debugPrintUrlHeaders || true) {
                System.out.printf("Url : %s\nUrlc: %s\nContent :: Encoding : %s Length : %s Type %s\n",
                        url,
                        connection.getURL(),
                        connection.getContentEncoding(),
                        numberFormat.format(connection.getContentLength()),
                        connection.getContentType());
                //            Map<String, List<String>> header = urlC.getHeaderFields();
                //            System.out.println(header.toString());
                System.out.println(connection.getHeaderFields().toString());
            }

            String cType = connection.getContentEncoding();
            System.out.printf(" ContentEncoding : %s\n", cType);

            long clen = connection.getContentLengthLong();
            System.out.printf(" ContentLength : %d\n", clen);

            InputStream is = connection.getInputStream();
            boolean haveToContinue = false;
            //            try{
            //                if ( dest.length()>0){
            //                is.skip(dest.length()); // todo et si on skip pas de la taille passé cf y a un reour ici qui n'est pas pris en compte ....
            //                haveToContinue=true;
            //                System.out.printf("Continuing %s from %d",dest.getName(), dest.length());
            //                }
            //            }
            //            catch (Exception e){
            //                haveToContinue=false;
            //                // ....
            //            }
            FileOutputStream fos = new FileOutputStream(fCache, haveToContinue);
            int bufferSize = 4096;
            byte[] creadBuffer = new byte[bufferSize];
            int len;
            long charcount = 0;
            while ((len = is.read(creadBuffer)) != -1) {
                fos.write(creadBuffer, 0, len);
                charcount += len;
            }
            fos.flush();
            fos.close();
            is.close();
            System.out.printf("-> %s %s\n", fCache.getAbsolutePath(), numberFormat.format(fCache.length()));
            if (charcount != fCache.length()) {
                System.out.printf("Erreur DL : %s %s\n", fCache.getAbsolutePath(), numberFormat.format(charcount));
            }

            //TODO ... lecture
        } catch (IOException e) {
            //System.out.println(e);
            Logger.getLogger(LesImprimantes3DForum.class.getName()).log(Level.SEVERE, null, e);

        }
    }

    private static boolean showDebugCacheDirBase = true;

    private static File getWwwCacheBaseDirAndCreateBaseDirs(URL url) {
        File fBaseCacheDir = new File(cacheBaseDir, url.getHost());
        if (fBaseCacheDir.exists() && fBaseCacheDir.isDirectory() && fBaseCacheDir.canRead() && fBaseCacheDir.canWrite()) {
            // ok
            if (showDebugCacheDirBase) {
                System.out.printf(" using cache dir : %s\n", fBaseCacheDir.getAbsolutePath());
            }
        } else {
            //todo
            if (!fBaseCacheDir.exists()) {
                boolean haveCreateDir = fBaseCacheDir.mkdirs();
                if (showDebugCacheDirBase) {
                    System.out.printf(" creating cache dir : %s\n", fBaseCacheDir.getAbsolutePath());
                }
            }
        }
        return fBaseCacheDir;
    }

    private static void debugUrl(URL url) throws URISyntaxException {
        System.out.printf(" url.toString                : %s\n", url.toString());
        System.out.printf(" url.toExternalForm          : %s\n", url.toExternalForm());
        System.out.printf(" url.toURI().toString()      : %s\n", url.toURI().toString());
        System.out.printf(" url.toURI().toASCIIString() : %s\n", url.toURI().toASCIIString());

        System.out.printf(" Protocol : %s\n", url.getProtocol());
        System.out.printf(" Host : %s\n", url.getHost());

        System.out.printf(" Port : %s\n", url.getPort());
        System.out.printf(" DefaultPort : %s\n", url.getDefaultPort());

        System.out.printf(" Path    : %s\n", url.getPath());
        System.out.printf(" getFile : %s\n", url.getFile());

        System.out.printf(" Query : %s\n", url.getQuery());// ? ... & ...
        System.out.printf(" Ref : %s\n", url.getRef());// #
        System.out.printf(" Authority : %s\n", url.getAuthority());

        System.out.printf(" UserInfo : %s\n", url.getUserInfo());
    }

    private static Document cacheAndParseUrl(String sUrlToParse, boolean doNextPage, boolean debugUrl, boolean debugParsed) {
        try {

            // Construct a URL object
            URL url = new URL(sUrlToParse);

            File fBaseCacheDir = getWwwCacheBaseDirAndCreateBaseDirs(url);

            if (debugUrl) {
                System.out.printf(" sUrlToParse                 : %s\n", sUrlToParse);
                debugUrl(url);
            }

            //A revoir nom du fichier de destination pour multiple pages mise en cache ...
            /*
            Avec gestion de l'url et de paramétres + coockies ?
            cf urlXYZ/, urlXYZ/# , urlXYZ/&t=?c=... (get), post, ... est relatif a entête et coockies (si
            
            Pour bien faire il faut un index au moin un basic
            pour archiver ... nom du fichier = currentTimeMillis de la requette ?
            fullURL -> filchier local
            ? mais donc pourune url on aura une liste eventuellement vide de fichier dont il faut savoir le/les quele utilisiser ou re faire la requette et le dl ...
            
             */
            //     URI tmpURI = url.toURI();
            // Si on fait en mode vrac ...
            File fDirDest = new File(fBaseCacheDir, url.getPath());
            if (showDebugCacheDirBase) {
                System.out.printf(" fDirDest : %s\n", fDirDest.getAbsoluteFile());
            }
            fDirDest.mkdirs();

            String outputcacheFile = "index.html";

            File fCache = new File(fDirDest, outputcacheFile);
            //todo ??? un system de nommage et de mise en cache
            /*
            mode simple et limité (pas d'archivage) recréation de l'orbo d'aprés le path de l'utl et d'un fichier content ? + header + ...
            
            + querry responce ?
            + time milli get
            + current milli debut req et fin req ?
            + current milli debut getContent er fin ?
            +...
            + contexte autre ?auth ? ....session .... cf via point d'acces / utilisation d'un proxi ? ...
            http req timestamp
            http resp timestamp
            http inputstrem
            ? user interaction ?
            
            un index avec notion de date de la requette !
            pour une url +header + session + ..  on a un map de contenus avec date ?
            // ? md5sum (hash) du content ?
            ...
            
            
            
             */
            // Faut t'il effacer le fichier de cache ?

            if (fCache.exists() && fCache.length() > 0) {
                // il y a un fichier 
                // faut t'il l'effacer ?
            } else {
            }
            // Si le fichier de cache n'existe pas où se trouve vide le créer / telecharger.
            if (fCache.exists() && fCache.length() > 0) {
                // il y a un fichier // faut t'il l'effacer ?
            } else {
                long tsub0 = System.currentTimeMillis();
                urlHttpConnecteAndWriteToFile(url, fCache);
                long tsub1 = System.currentTimeMillis();
                if (debugTimming) {
                    System.out.println(sUrlToParse + " ( cached in " + (tsub1 - tsub0) + " ms )");
                }
            }

            // Donc là on devrait avoir un fichier en "cache" a lire
            String sSelect;
            long tsub0 = System.currentTimeMillis();
            // TODO voir pour faire sans cache ...

            //Document doc = Jsoup.connect(sUrlToParse).get();
            Document doc = Jsoup.parse(fCache, null, sUrlToParse);

            long tsub1 = System.currentTimeMillis();
            if (debugTimming) {
                System.out.println(sUrlToParse + " ( jsoup get in " + (tsub1 - tsub0) + " ms )");
            }
            //
            return doc;

        } catch (MalformedURLException e) {
            System.out.println(e);
        } catch (IOException ex) {
            Logger.getLogger(LesImprimantes3DForum.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(LesImprimantes3DForum.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //
    //
    //
    // Pour ne pas trop "sur-charger le serveur" on va mettre des petit delais (aleatoire)    
    private static boolean noTempo = true;
    private static long maxTempo = 10000;
    private static long minTempo = 1000;
    private static Random rand = new Random();

    private static void sleepRandomTimes() {
        if (noTempo) {
            return;
        }
        try {
            long tmpRand = rand.nextLong(minTempo, maxTempo);
            //System.out.printf("sleep %20d + %d\n",System.currentTimeMillis(),tmpRand);
            Thread.sleep(tmpRand);
        } catch (InterruptedException ex) {
            Logger.getLogger(LesImprimantes3DForum.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //
    public static void mainDisplaySystemProperties(String[] args) {

        Properties properties = System.getProperties();
        // Java 8
//        properties.forEach((k, v) -> System.out.println(k + ":" + v));

        // Classic way to loop a map
        //for (Map.Entry<Object, Object> entry : properties.entrySet()) {
        //    System.out.println(entry.getKey() + " : " + entry.getValue());
        //}
        // No good, output is truncated, long lines end with ...
        //properties.list(System.out);
        // Thanks Java 8
        LinkedHashMap<String, String> collect = properties.entrySet().stream()
                .collect(Collectors.toMap(k -> (String) k.getKey(), e -> (String) e.getValue()))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        collect.forEach((k, v) -> System.out.println(k + ":" + v));
    }
}
