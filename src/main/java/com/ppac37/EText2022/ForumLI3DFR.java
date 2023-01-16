/*
 */
package com.ppac37.EText2022;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Entities.EscapeMode;

/**
 * Pour faire une copie local d'un message du forum lesimprimantes3d.fr.
 *
 * TODO extraction du message et vérification si contine ou non des liens de
 * telechargement de fichier ou des images
 * <p>
 * TODO : revoir l'utilisation de HTML2Md (pour les images .comment proposer une
 * sauvegarde en local et garder les lien originaux ? c-a-d sourcer les images )
 * A choisir modifier le document html pour avant l'export en .md avoir fait une
 * copie des images en local et avoir adapté les src="..." des images ? Ou faire
 * autrement ? ( dans HTML2Md encapsuler dans un commentaire ? et faire le dl de
 * limage et ajouter la nouvel image ? ) ? ??? et si j'a besion de limiter la
 * taille d'une image ? utiliser du code html ? ( pour rester avec du markdown
 * compatible avec github ! )
 *
 * <p>
 * Ajouter plus de bidouilles a HTML2Md pour garder les iframes ( revoie vers
 * sujet / message et video YouTube )? les liens des citation ?
 *
 * <br>
 * https://www.lesimprimantes3d.fr/forum/
 * https://www.lesimprimantes3d.fr/forum/69-entraide-questionsr%C3%A9ponses-sur-limpression-3d/
 * https://www.lesimprimantes3d.fr/forum/69-entraide-questionsr%C3%A9ponses-sur-limpression-3d/?sortby=posts&sortdirection=desc
 * https://www.lesimprimantes3d.fr/forum/69-entraide-questionsr%C3%A9ponses-sur-limpression-3d/?sortby=posts&sortdirection=asc
 * https://www.lesimprimantes3d.fr/forum/69-entraide-questionsr%C3%A9ponses-sur-limpression-3d/?sortby=forums_topics.last_post&sortdirection=desc
 * <ul>
 * <li>https://www.lesimprimantes3d.fr/forum/69-entraide-questionsr%C3%A9ponses-sur-limpression-3d/?filter=solved_topics
 * </li>
 * <li>https://www.lesimprimantes3d.fr/forum/69-entraide-questionsr%C3%A9ponses-sur-limpression-3d/?filter=unsolved_topics
 * </li>
 * <li>https://www.lesimprimantes3d.fr/forum/69-entraide-questionsr%C3%A9ponses-sur-limpression-3d/?filter=filter_hidden_topics
 * </li
 * ><li>https://www.lesimprimantes3d.fr/forum/69-entraide-questionsr%C3%A9ponses-sur-limpression-3d/?filter=filter_hidden_posts_in_topics
 * </li>
 * </ul>
 * <br>
 * https://www.lesimprimantes3d.fr/forum/online/
 * <br>
 * https://www.lesimprimantes3d.fr/forum/online/?filter=group_3
 * <br>
 * https://www.lesimprimantes3d.fr/forum/staff/
 * <br>
 * https://www.lesimprimantes3d.fr/forum/leaderboard/ Ce classement est réglé à
 * UTC/GMT+00:00
 * <br>
 * <br>
 * https://www.lesimprimantes3d.fr/forum/discover/
 * https://www.lesimprimantes3d.fr/forum/discover/all.xml/
 *
 *
 *
 * @author q6
 */
public class ForumLI3DFR {

    //import org.slf4j.Logger;
    //import org.slf4j.LoggerFactory;
    private static final Logger logger = LoggerFactory.getLogger(ForumLI3DFR.class);
    //logger.debug("version: {}", version);

    /**
     * Template String.format pour d'un id arriver a une url de page sur le
     * forum.      <code>
     * String idTopic = "45754" ; // 45754 c'est normalement l'id du topic du glossaire
     * String urlGenericVerTopic = String.format(li3dfrForumTopicTemplate, idTopic);
     * </code> Fonctionne uniquement cat le moteur du forum fait les
     * redirections lors des changemetn de titre ( TODO test unitaire pour le
     * formu )
     */
    public static String li3dfrForumTopicTemplate = "https://www.lesimprimantes3d.fr/forum/topic/%s-x/";
// Cela plante l'actuele generation de lien vers commentaire... :: + "?sortby=date#comments";

    /**
     * TODO a supprimer mais forcement c'est utilisé ailleur . a revoir
     */
    public static final String HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754
            = "https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/";

    static String[] urls = { //        HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754
        //
        //"https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/?do=editComment&comment=478082"
        //    "https://www.lesimprimantes3d.fr/forum/68-tutoriels/" // -> todo cache a revoir car actuellement KO https://www.lesimprimantes3d.fr/forum/68-tutoriels/?page=2
    };

    /**
     *
     */
    static SortedSet<ForumUneDef> lesDef = new TreeSet<>();
    static String enteteSommaireToUse = "";

    static String baseDirOutput = "."+File.separator+"out"+File.separator+"";
    
    static boolean doBrowse = false;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        logger.trace("debut: {}", ForumLI3DFR.class.getName());

        String idTopic = "";
        Properties propDeIdTopic = new Properties();
        String inDirPath = "in";

        String sForTitreH1 = " Sommaire ";
        String outTotauxSpan = "Total %d alias pour %d définitions.";
        String retour_h2_local = "(retour sommaire local)";

        String c_datemodif_by_s_s = "M %s %s";
        String c_datec_by_s_s = "C %s %s";
        String nb_Img_d_hors_citation___d__dans_citation = "Nb Img %d (hors citation)  + %d ( dans citation )";

        File fInDir = new File(inDirPath);
        if (fInDir.exists()) {
            for (File f : fInDir.listFiles()) {

                if (f.isFile()) {
                    if (f.getName().endsWith(".properties")) {
                        System.out.printf("load in dir loading: %s\n", f.getPath());
                        Properties prop
                                = UtilPropertiesFile.loadPropertiesLocal(f.getPath(), false);
                        String sDadeDebut = prop.getProperty("concours.date.debut");
                        String sDadeLimit = prop.getProperty("concours.date.limite-inscriptions");
                        String sDadeFin = prop.getProperty("concours.date.fin-votes");

                        sForTitreH1 = prop.getProperty("out.titre.h1");
                        outTotauxSpan = prop.getProperty("out.totaux.span");
                        retour_h2_local = prop.getProperty("out.retour.text");
                        c_datemodif_by_s_s = prop.getProperty("out.c.d.M.format");
                        c_datec_by_s_s = prop.getProperty("out.c.d.C.format");
                        nb_Img_d_hors_citation___d__dans_citation = prop.getProperty("out.c.img.text");

                        String sIdTopic = prop.getProperty("topic.id");

                        idTopic = sIdTopic;
                        propDeIdTopic = prop;
                        int cpt = 0;
                        for (Object o : prop.keySet()) {
                            String k = (String) o;
                            if (false) {
                                System.out.printf(" \"%s\" = \"%s\"\n", k, prop.get(k));
                            }
                            if (k.startsWith("comment")) {
                                cpt++;
                                if (false) {
                                    System.out.printf(" \"%s\" = \"%s\"\n", k, prop.get(k));
                                }
                            } else {
                                System.out.printf(" \"%s\" = \"%s\"\n", k, prop.get(k));
                            }

                        }
                        System.out.printf(" \"%s\" cpt = %d\n", "comment.*", cpt);
                    } else {
                        // TODO autre type de fichiers   
                        logger.trace("load in dir error: not a properties file: {}", f.getPath());
                    }
                } else {
                    // TODO
                    logger.trace("load in dir error: not a file: {}", f.getPath());
                }

            }

        }

        boolean asSujetGlossaireOuConcours=true;
        String lienVersCommentaireBase = HTTPSWWWLESIMPRIMANTES3DFRFORUMTOPIC45754 + "?do=findComment&comment=";
        if (!asSujetGlossaireOuConcours) {
            for (String sUrl : urls) {
                //lienVersCommentaireBase = sUrl + "?do=findComment&comment="; // TODO a revoir c'est pas top .
                UrlCParserForum urlCParser = new UrlCParserForum(sUrl, true);
            }
        } else {

            if (idTopic.isBlank()) {
                idTopic = "45754"; // normalement le sujet du glossaire

            }
            if (!idTopic.isBlank()) {
                String sUrlVersTopic = String.format(ForumSujet.li3dfrForumTopicTemplate, idTopic.strip());
                UrlCParserForum urlCParser = new UrlCParserForum(sUrlVersTopic, true);
                lienVersCommentaireBase = sUrlVersTopic + "?do=findComment&comment=";
            }

        }
        //
        System.out.printf("Nb comment with Def = %d\n", lesDef.size());

        // création si n'existe pas du repertoire pour les fichiers de sortie.
        File destDir = new File(baseDirOutput);
        if (destDir.exists()) {
            //TODO ? supprimer des truc ?
        } else {
            destDir.mkdirs();
        }
        // 
        System.out.println("Using as output dir : " + destDir.getAbsolutePath());

        UtilFileWriter fwIndexCommentMd = new UtilFileWriter(baseDirOutput + File.separator + "indexComment.md");
        fwIndexCommentMd.append("# Lexique\n");
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

            // Pour une issue avec des coche 
            if (false) {
                fwIndexCommentMd.append(d.toStringMD_Coche(lienVersCommentaireBase + d.commentId) + "\n");
            }

            if (true) {
                // Pour faire comme le lexique de SuperSlicer traduit par 5Axe
                fwIndexCommentMd.append(d.toStringMD_Index(lienVersCommentaireBase + d.commentId) + "\n");
            }

            cptTotalAlias += d.defNomAlias.size();
            for (String a : d.defNomAlias) {
                aliasToId.put(new ForumUneDefAlias(a), d.commentId);
            }
        }
        fwIndexCommentMd.flush();
        fwIndexCommentMd.close();

        //
        boolean isConcours = true;

        Document docSiConcours = Jsoup.parse("");
        Element divResum = docSiConcours.body().appendElement("div");

        boolean doNotAddElemInDivResum = true;
        Element divClassement = divResum.appendElement("div");

        Element divRejet = divResum.appendElement("div");
        divResum.appendElement("br");

        if (isConcours) {
            Properties propSortie = new Properties();
            // essai de trie pour gagants concours .
            // un seul lot par gagnat -> regrouper par les post par utilisateur
            // Avoir la liste des lots ( 1er message ) 
            // Avoir la vérification des validitées des entrées ( une photo d'un print avec papier li3d.fr et ( et pas une incrustation sur la photo)
            // cf un validateur vérife et notes si ok ou non ok supprime l'entrée .
            // si egalité ?
            // avoir la version a la date heure de fin de prise en compte des likes ?
            // ignorer les entrée qui on dépacé la date de fin "01/01/2023 23h59"

            SortedSet<ForumUneEntreeConcours> listeDesEntreesValideOuNon = new TreeSet<>();

            int cptRejet = 0;
            int cptAccept = 0;
            Map<String, ArrayList<ForumUneEntreeConcours>> mapUserToArrayListEntree = new HashMap<>();
            for (ForumUneDef d : lesDef) {
                String sApprobation = propDeIdTopic.getProperty("comment." + d.commentId);
                boolean isEntreeInValideFromApprobation = true;
                if (sApprobation != null && sApprobation.startsWith("no ")) {
                    isEntreeInValideFromApprobation = false;
                    // TODO rapatier cette raison de rejet pour le fichier de prop de sortie des raison de rejet
                    propSortie.setProperty("comment." + d.commentId, sApprobation);
                    cptRejet++;

                } else {
                    //TODO compléter un fichier prop de sortie ou l'on donne la raison du rejet ...
                    if (d.alImgsUrl.isEmpty()) {
                        propSortie.setProperty("comment." + d.commentId, "no img");
                        cptRejet++;
                    }

                }
                if (!d.alImgsUrl.isEmpty()
                        //                        && !d.commentAuteurNom.equals("PPAC")
                        //                        && !d.commentAuteurNom.equals("LesImprimantes3D.fr")
                        //                        && !d.commentAuteurNom.equals("Motard Geek")

                        && isEntreeInValideFromApprobation) {
                    cptAccept++;
                    ForumUneEntreeConcours asC = new ForumUneEntreeConcours(d);
                    ArrayList<ForumUneEntreeConcours> get = mapUserToArrayListEntree.get(asC.getCommentAuteurNom());

                    if (get != null) {
                        get.add(asC);
                    } else {
                        ArrayList<ForumUneEntreeConcours> a = new ArrayList<>();
                        a.add(asC);
                        mapUserToArrayListEntree.put(asC.getCommentAuteurNom(), a);
                    }
                    // trop tot //  d.reloadReactionHitory();
                } else {

                    // TODO Annoter pour avoir une raison de rejet ( n'a pas d'image )
                    // TODO Annoter pour les autre cas de rejet ( et invalidé d'apréé le fichier d'approbation )
                }

                //
                if (!d.alImgsUrl.isEmpty()) {
                    ForumUneEntreeConcours asC = new ForumUneEntreeConcours(d);
                    asC.isRejetedEntry = isEntreeInValideFromApprobation;
                    listeDesEntreesValideOuNon.add(asC);
                }
            }
            System.out.println("Rejeté cpt = " + cptRejet);
            System.out.println("Accept cpt = " + cptAccept);
            System.out.println("R+A    cpt = " + (cptAccept + cptRejet));
            System.out.println("Total commentaire avec image(s) entré valide ou non = " + listeDesEntreesValideOuNon.size());
            //
            try {
                int nbInpropSortie = propSortie.size();
                //lesDef.size();
                System.out.println("n prop cpt = " + nbInpropSortie);
                System.out.println("control isCohérent = " + (nbInpropSortie == (cptAccept + cptRejet) ? "OK" : "TRUC LOUCHE !!!"));
                //
                //
                File fPropDest = new File(baseDirOutput + File.separator + "" + idTopic + "-NEW.properties");
                System.out.printf("writed %d in %s\n", fPropDest.length(), fPropDest.getPath());
                FileWriter fwP = new FileWriter(fPropDest);
                propSortie.store(fwP, "En date de ...");
                fwP.flush();
                fwP.close();
                System.out.printf("writed %d in %s\n", fPropDest.length(), fPropDest.getPath());
            } catch (Exception e) {
                e.printStackTrace();

            }
            // la plus liké de ses entré valide ( une image photo d'une impression avec li3d.fr sur post it ou incrusté dans desinge )
            SortedSet<ForumUneEntreeConcours> sSetRes = new TreeSet<>();
            Map<Integer, List<ForumUneEntreeConcours>> parGroupeNbLikeToEntreeList = new TreeMap<>();
            System.out.printf("### Res concours A Trier\n");
            int totalnbe = 0;
            for (String k : mapUserToArrayListEntree.keySet()) {
                ArrayList<ForumUneEntreeConcours> a = mapUserToArrayListEntree.get(k);

                SortedSet<ForumUneEntreeConcours> sSet = new TreeSet<>();
                for (ForumUneEntreeConcours e : a) {
                    sSet.add(e);
                }

                System.out.printf("%s\t%d\t%d\t%d\t"
                        + "https://www.lesimprimantes3d.fr/forum/topic/50575-qqchose/?do=findComment&comment=%s\n",
                        k, sSet.size(), a.size(),
                        sSet.first().getReactionsTotals(),
                        sSet.first().getCommentId()
                );

                if (!doNotAddElemInDivResum) {
                    Element append = divResum.appendElement("div");
                }
//                append.append("Par ");
//                append.append(k);
//                append.append(String.format(" %d entrée(s)", sSet.size()));
                Iterator<ForumUneEntreeConcours> iterator = sSet.iterator();
                while (iterator.hasNext()) {
                    totalnbe++;

                    ForumUneEntreeConcours eC = iterator.next();
                    if (!doNotAddElemInDivResum) {
                        Element appends = divResum.appendElement("div");

                        appends.attr("id", eC.getCommentId());

                        if (false) {
                            appends.append(
                                    String.format(
                                            "&nbsp;&nbsp; %d likes sur \t"
                                            + "https://www.lesimprimantes3d.fr/forum/topic/50575-qqchose/?do=findComment&comment=%s\n",
                                            eC.getReactionsTotals(),
                                            eC.getCommentId()
                                    ));
                        } else {
                            if (false) {
                                // refait en mieux ailleur et avec des id en conflic avec autre bloc de detaille des commentaire
                                // Nom d'utilisateur, liens vers forum, lien retour 
                                appends.appendText(k);
                                Element tmpUrlToComment = appends.appendElement("a").attr("href",
                                        "https://www.lesimprimantes3d.fr/forum/topic/"
                                        + idTopic + "-qqchose/?do=findComment&comment=" + eC.getCommentId()
                                );
                                tmpUrlToComment.attr("target", "_blank");
                                tmpUrlToComment.append(" allez au commentaire sur le forum.");
                                appends.appendChild(tmpUrlToComment);
                                appends.appendElement("a").attr("href", "#top." + eC.getCommentAuteurId()).appendText(" | retour");
                            }

                            Element appendsImgs = divResum.appendElement("div");
                            //appendsImgs.attr("height", "100px");
                            for (String sImgUrl : eC.getAlImgsUrl()) {
                                Element appendElementImg = appendsImgs.appendElement("img");
                                appendElementImg.attr("src", sImgUrl);
                                appendElementImg.attr("class", "vignettes");
                                appendElementImg.attr("style", "max-width:150px;max-height:150px;width: auto;height: auto;");
                            }
                        }
                    }

                }

                sSetRes.add(sSet.first());
                int maxRCUser = sSet.first().getReactionsTotals();
                List<ForumUneEntreeConcours> l = parGroupeNbLikeToEntreeList.get(maxRCUser);
                if (l == null) {
                    List<ForumUneEntreeConcours> tmp = new ArrayList();
                    parGroupeNbLikeToEntreeList.put(maxRCUser, tmp);
                    tmp.add(sSet.first());
                    //sSet.first()    
                } else {
                    l.add(sSet.first());
                }
            }
            System.out.printf("###\n");
            for (Integer i : parGroupeNbLikeToEntreeList.keySet()) {
                List<ForumUneEntreeConcours> tmp = parGroupeNbLikeToEntreeList.get(i);
                if (tmp.size() > 1) {
                    String listUserEnConflic = "";
                    for (ForumUneEntreeConcours e : tmp) {

                        listUserEnConflic += "\"" + e.commentAuteurNom + "\"(" + e.commentAuteurId + ") ";
                        if (i == 21) {
                            e.setSujetId(idTopic);
                            //
                            e.reloadReactionHitory();
                        }
                    }
                    System.out.printf("En conflic %d reaction %s\n", i, listUserEnConflic);
                }
            }
            if (!doNotAddElemInDivResum) {
                divResum.appendElement("div").append("entrées " + totalnbe);

                divResum.appendElement("div").append("utilisateurs avec au moins une etrées : " + mapUserToArrayListEntree.size());

            }
            System.out.println("Nb Utilisateur ayant un post avec aux moins une images " + mapUserToArrayListEntree.size());

            System.out.println("Nb entrées r :" + totalnbe);
            System.out.println("");

            if (false) {
                // ajouter un table d'un un classement sur les likes mais qui filtre les multiple commentaire d'un même utilisateur ...
                // pas adpaté si pas un concours

                int pos = 1;
                Element tableResOrdo = divClassement.appendElement("table");
                Element tableResOrdoHeader = tableResOrdo.appendElement("tr");
                tableResOrdoHeader.appendElement("th").appendText("Pos");
                boolean addColUserName = false;
                if (addColUserName) {
                    tableResOrdoHeader.appendElement("th").appendText("Utilisateur");
                }
                tableResOrdoHeader.appendElement("th").appendText("Réactions");
                boolean addColLienToComment = true;// a revoir car si a false il manque actuellement un element avec un id pour faciliter la navigation interne a la page
                if (addColLienToComment) {
                    tableResOrdoHeader.appendElement("th").appendText("Commentaire");
                }
                boolean addColDateC = false;
                if (addColDateC) {
                    tableResOrdoHeader.appendElement("th").appendText("DateC");
                }
                tableResOrdoHeader.appendElement("th").appendText("Thumbs");

                Element lastElementPost = null; // pour marquer qd il y a un conflict ( même nb de réaction ) 
                int lastNbReaction = -1;
                boolean lastPosHaveBeenMark = false;
                boolean nextlastPosIsToMark = false;
                for (ForumUneEntreeConcours e : sSetRes) {

                    if (e.getReactionsTotals() != lastNbReaction) {

                    } else {
                        if (lastElementPost != null) {
                            lastElementPost.appendText("*");
                            lastPosHaveBeenMark = true;
                            nextlastPosIsToMark = true;
                        }
                    }

                    Element appendElement = tableResOrdo.appendElement("tr");
                    lastElementPost = appendElement.appendElement("td").appendText("" + pos);
                    lastNbReaction = e.getReactionsTotals();
                    pos++;
                    if (nextlastPosIsToMark == true) {
                        lastElementPost.appendText("*");
                        nextlastPosIsToMark = false;
                    } else {

                    }
                    if (addColUserName) {
                        appendElement.appendElement("td").appendText(e.commentAuteurNom);
                    }
                    appendElement.appendElement("td").appendText("" + e.getReactionsTotals());
                    if (addColLienToComment) {
                        Element tmpUrlToComment = appendElement.appendElement("a").attr("href",
                                "#" + e.getCommentId()
                        //                        "https://www.lesimprimantes3d.fr/forum/topic/"
                        //                        + idTopic + "-qqchose/?do=findComment&comment=" + e.getCommentId()
                        ).attr("id", "top." + e.getCommentAuteurId());
                        tmpUrlToComment.append("->");
                        appendElement.appendElement("td").appendChild(tmpUrlToComment);
                    }

                    System.out.printf("%40s\t%d\t%d\thttps://www.lesimprimantes3d.fr/forum/topic/50575-qqchose/?do=findComment&comment=%s\n",
                            e.commentAuteurNom, e.alImgsUrl.size(),
                            e.getReactionsTotals(),
                            e.getCommentId()
                    );

                    if (addColDateC) {
                        appendElement.appendElement("td").appendText("" + e.getDateCreation());
                    }
                    appendElement.appendElement("td").append(e.createThumbsCitation("max-width:150px;max-height:150px;width: auto;height: auto;"));

                }
            }

            if (true) {

                
                UtilFileWriter fwIndexCommentaireAvecDesImages = new UtilFileWriter(baseDirOutput + File.separator + "indexC.html");
                fwIndexCommentaireAvecDesImages.append("<!DOCTYPE html>\n");
                fwIndexCommentaireAvecDesImages.append("<html lang=\"fr\">\n");
                fwIndexCommentaireAvecDesImages.append("<head>\n");
                fwIndexCommentaireAvecDesImages.append("<title>");
                //fwIndexCommentaireAvecDesImages.append("Glossaire");
                //fwIndexCommentaireAvecDesImages.append(sForTitreH1); // TODO toujours se problème d'encodage a toute la pas si un accent ici ...
                fwIndexCommentaireAvecDesImages.append(URLEncoder.encode(sForTitreH1));
                fwIndexCommentaireAvecDesImages.append("</title>\n");
                fwIndexCommentaireAvecDesImages.append("</head>\n");
                fwIndexCommentaireAvecDesImages.append("<body>\n");
                fwIndexCommentaireAvecDesImages.append(String.format("<h2 style=\"text-align:center;\" >%s</h2>\n", 
                        "Images Thumbs Top réactions"
                ));

                Document createShell = Jsoup.parse("");

                boolean addColUserName = false;
                boolean addColLienToComment = false;
                boolean asLocalLink = false;
                boolean addColDateC = false;
                Element tableResOrdo = createShell.body().appendElement("table");
                Element tableResOrdoHeader = tableResOrdo.appendElement("tr");
                //tableResOrdoHeader.appendElement("th").appendText("Pos");
                if (addColUserName) {
                    tableResOrdoHeader.appendElement("th").appendText("Utilisateur");
                }
                tableResOrdoHeader.appendElement("th").appendText("Réactions");
                if (addColLienToComment) {
                    tableResOrdoHeader.appendElement("th").appendText("Lien");
                }
                if (addColDateC) {
                    tableResOrdoHeader.appendElement("th").appendText("DateC");
                }
                tableResOrdoHeader.appendElement("th").appendText("Image(s)");

                for (ForumUneEntreeConcours e : listeDesEntreesValideOuNon) {
                   
                    Element appendElement = tableResOrdo.appendElement("tr");
                    
                    if (addColUserName) {
                        appendElement.appendElement("td").appendText(e.commentAuteurNom);
                    }
                    appendElement.appendElement("td").appendText("" + e.getReactionsTotals());
                    if (addColLienToComment) {
                        Element tmpUrlToComment = appendElement.appendElement("a").attr("href",
                               asLocalLink
                                       ? 
                                       "#" + e.getCommentId()
                                       :
                                                "https://www.lesimprimantes3d.fr/forum/topic/"
                                                + idTopic + "-L/?do=findComment&comment=" + e.getCommentId()
                                       
                        ).attr("id", "top." + e.getCommentAuteurId()).attr("target", "_blank");
                        tmpUrlToComment.append("->"+
                               ( asLocalLink
                                       ? "(local)" : "(forum)" )
                                );
                        appendElement.appendElement("td").appendChild(tmpUrlToComment);
                    }

                    System.out.printf("%40s\t%d\t%d\thttps://www.lesimprimantes3d.fr/forum/topic/50575-qqchose/?do=findComment&comment=%s\n",
                            e.commentAuteurNom, e.alImgsUrl.size(),
                            e.getReactionsTotals(),
                            e.getCommentId()
                    );

                    if (addColDateC) {
                        appendElement.appendElement("td").appendText("" + e.getDateCreation());
                    }
                    appendElement.appendElement("td").append(e.createThumbsCitation("max-width:150px;max-height:150px;width: auto;height: auto;"));
                }
                fwIndexCommentaireAvecDesImages.append(createShell.body().html());
                fwIndexCommentaireAvecDesImages.append("</body>\n");
                fwIndexCommentaireAvecDesImages.append("</html>\n");
                fwIndexCommentaireAvecDesImages.flush();
                fwIndexCommentaireAvecDesImages.close();

            }
        }
        //

        Document doc = Jsoup.parse("");
        doc.appendElement("html");

        boolean useSommaireEnteteHardCoded = false;
        String sIndexFilePathAndName = baseDirOutput + File.separator + "index.html";
        File fIndexFilePathAndName = new File(sIndexFilePathAndName);
        // la pour un sommaire en html
        UtilFileWriter fwIndexOnlySommaireHtml = new UtilFileWriter(fIndexFilePathAndName.getAbsolutePath());
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
            //se qui se trouvé avant le sommaire dans se commentaire a etait mis dans enteteSommaireToUse .

            fwIndexOnlySommaireHtml.append(enteteSommaireToUse);
        }
        fwIndexOnlySommaireHtml.append(String.format("<h2 style=\"text-align:center;\" >%s</h2>\n", sForTitreH1));

        // la pour un sommaire en html avec les definition 
        // Bon il y a un truc qui m'echape avec JSoup pour créer un document ... cela bug les accents si il y en a un dans le head.title
        Document dout = Jsoup.parse("<html lang=\"fr\">", "UTF-8");
        Charset charset = dout.charset();
        dout.attr("lang", "fr");
        dout.attr("encoding", charset.toString());
        //
        dout.body().append(String.format("<h2 style=\"text-align:center;\" >%s</h2>\n", sForTitreH1));
        dout.body().appendText(charset.toString());
        // TODO BUG encode si il y a un accent la dedant ... dout.head().appendElement("title").appendText(sForTitreH1);        //Concour noël 2022 participations
        UtilFileWriter essai = new UtilFileWriter(baseDirOutput + File.separator + "index2.1.html");
        essai.append("<!DOCTYPE html>\n"); // Là c'est naze si c'est pas posible autrement ... ? Vraiment faire du JAVA DOM ?
        dout.outputSettings().escapeMode(EscapeMode.xhtml);
        dout.outputSettings().charset("utf-8");
        essai.append(dout.outerHtml());
        essai.flush();
        essai.close();
        //

        UtilFileWriter fwIndexSommaireEtCommentHtml = new UtilFileWriter(baseDirOutput + File.separator + "index2.html");
        fwIndexSommaireEtCommentHtml.append("<!DOCTYPE html>\n");
        fwIndexSommaireEtCommentHtml.append("<html lang=\"fr\">\n");
        fwIndexSommaireEtCommentHtml.append("<head>\n");
        fwIndexSommaireEtCommentHtml.append("<title>");
        //fwIndexSommaireEtCommentHtml.append("Glossaire");
        //fwIndexSommaireEtCommentHtml.append(sForTitreH1);
        fwIndexSommaireEtCommentHtml.append("</title>\n");
        fwIndexSommaireEtCommentHtml.append("</head>\n");
        fwIndexSommaireEtCommentHtml.append("<body>\n");
        fwIndexSommaireEtCommentHtml.append(String.format("<h2 style=\"text-align:center;\" >%s</h2>\n", sForTitreH1));

        // la pour une autre version en html mais avec plus de lien pour navigation local ( TODO gestion de tous les liens est de elements externe ( images, iframe video, iframe vers sujet ou commentaire. ) 
        UtilFileWriter fwIndexHtml_avec_lien_et_id_pour_navigation_embarque = new UtilFileWriter(baseDirOutput + File.separator + "index3.html");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("<!DOCTYPE html>\n");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("<html lang=\"fr\">\n");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("<head>\n");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("<title>");
        //fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("Glossaire nav interne");
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("* " + sForTitreH1);
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("</title>\n");
        //TODO style css <link rel="stylesheet" type="text/css" href="impression.css" media="print">
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append("""
                                            <style>
                                               <!--
                                               
                                                  @media screen {
                                                       .warn { background-color :yellow;}             
                                                        img.vignettes {
                                                          display: inline;
                                                          max-width:250px;
                                                          max-height:200px;
                                                          width: auto;
                                                          height: auto;
                                                        }
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
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(
                String.format("<h2 style=\"text-align:center;\" id=\"debut\">%s</h2>\n", sForTitreH1));
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(
                divResum.toString());
       

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
                    //TODO généraliser ce fix
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
                    fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(
                            String.format("<h3 id=\"%s\">%s</h3>\n", tmpFisrtChar, tmpFisrtChar));
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
                        .append(String.format("<a href=\"%s%s\"  target=\"_blank\">%s</a>\n",
                                lienVersCommentaireBase, aliasToId.get(k), k.a));
            } else {
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<a href=\"#%s\" >%s</a>\n", aliasToId.get(k), k.a));
            }
            fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                    .append(String.format("<br>\n"));
        }

        fwIndexOnlySommaireHtml
                .append(String.format("<br><p>"
                        + outTotauxSpan // "Total %d alias pour %d définitions."//out.totaux.span
                        + "</p>\n", cptTotalAlias, lesDef.size() - 1));// -1 pour le commentaire qui contien le sommaire qui n'a pas d'alias

        fwIndexSommaireEtCommentHtml
                .append(String.format("<br><p>"
                        + outTotauxSpan
                        + "</p>\n", cptTotalAlias, lesDef.size() - 1));// -1 pour le commentaire qui contien le sommaire qui n'a pas d'alias
        fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                .append(String.format("<br><p>"
                        + outTotauxSpan
                        + "</p>\n", cptTotalAlias, lesDef.size() - 1));// -1 pour le commentaire qui contien le sommaire qui n'a pas d'alias

        // Boucle sur l'ensemble des "Définitions" ( qui en fait sont actuellement l'ensemble des commentaire ... A REVOIR)
        // Les définitions
        //
        for (ForumUneDef d : lesDef) {

            if (false) {
                System.out.println(d.toString());
            }

            if (false) {
                System.out.println(d.toStringFTab());
            }

            if (d.defNomAlias.isEmpty()) {

                fwIndexSommaireEtCommentHtml
                        .append(String.format("<hr>\n<a href=\"%s%s\" target=\"_blank\" >comment-id %s :: %s</a>\n", lienVersCommentaireBase, d.commentId, d.commentId, d.defNomAlias));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<hr><a id=\"%s\" target=\"_blank\" href=\"%s%s\">comment-id %s :: %s</a> "
                                + "// <a href=\"#debut\" style=\"text-align:center;\" >"
                                + retour_h2_local
                                + "</a>\n",
                                d.commentId, lienVersCommentaireBase, d.commentId, d.commentId, d.defNomAlias));
                //.append(String.format("<div>%s</div>\n<br>\n", ));
                fwIndexSommaireEtCommentHtml
                        .append(String.format("<div>\n"
                                + "<details><summary>\n"
                                + "...\n"
                                + "</summary>\n%s\n</details>\n"
                                + "</div>\n", d.commentCorpHTMLBrut));

                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<div><code>"
                        + c_datec_by_s_s
                        + "</code></div>\n", d.getDateCreation(), d.commentAuteurNom));
                if (d.commentModifDate != null) {
                    fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<div><code>"
                            + c_datemodif_by_s_s
                            + "</code></div>\n", d.commentModifDate, d.commentModifParNom));
                }
                //TODO a revoir plus générique pour d'autre site ou forum car là dommaine en dur et fonctionne seulement si le moteur du forum gére une redirection pour quand un titre d'un sujet a changé.
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(
                        String.format(""
                                + "<div><a href=\"https://www.lesimprimantes3d.fr/forum/topic/%s-qqchose/"
                                + "?do=showReactionsComment&comment=%s&changed=1&reaction=all\""
                                + " target=\"_blank\">R %d</a></div>\n",
                                d.getSujetId(), d.commentId, d.getReactionsTotals()));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<div><code>"
                        + nb_Img_d_hors_citation___d__dans_citation
                        + "</code>\n<div>\n", d.alImgsUrl.size(), d.alImgsUrlDansCitation.size()));
                for (String urlImg : d.alImgsUrl) {
                    fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(
                            String.format("<img class=\"vignettes\" src=\"%s\"/> ", urlImg));
                }
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("</div></div>\n"));

                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<div><details><summary>"
                                + "..."
                                + "</summary>%s</details></div>\n", d.commentCorpHTML));

            } else {
                //d.saveInHistoryDir(d.defNomAlias.toString());
                d.saveInHistoryDirPrety(d.defNomAlias.toString());
                // ? sauver un fichier avec juste le coprs html du commentaire

                fwIndexSommaireEtCommentHtml
                        .append(String.format(
                                "<hr>\n<a href=\"%s%s\" target=\"_blank\" >comment-id %s :: %s</a>\n<hr>\n",
                                lienVersCommentaireBase, d.commentId, d.commentId, d.defNomAlias));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<hr><a id=\"%s\" href=\"%s%s\" target=\"_blank\">"
                                + "comment-id %s :: %s</a> // "
                                + "<a href=\"#debut\" style=\"text-align:center;\" >"
                                + retour_h2_local
                                + "</a>\n", d.commentId, lienVersCommentaireBase, d.commentId, d.commentId, d.defNomAlias));
                //.append(String.format("<div>%s</div>\n<br>\n", ));
                fwIndexSommaireEtCommentHtml
                        .append(String.format("<div>\n%s\n</div>\n", d.commentCorpHTMLBrut));

                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<div><code>"
                        + c_datec_by_s_s
                        + "</code></div>\n", d.getDateCreation(), d.commentAuteurNom));
                if (d.commentModifDate != null) {
                    fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.append(String.format("<div><code>"
                            + c_datemodif_by_s_s
                            + "</code></div>\n", d.commentModifDate, d.commentModifParNom));
                }
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque
                        .append(String.format("<div>%s</div>\n", d.commentCorpHTML));
            }

            if (false) {
                fwIndexSommaireEtCommentHtml.
                        append(
                                String.format(
                                        " comment-id %s\t%-35s\t%d\t%s\n",
                                        d.commentId, d.defNom, d.defNomAlias.size(), d.defNomAlias.toString()));
                fwIndexHtml_avec_lien_et_id_pour_navigation_embarque.
                        append(
                                String.format(
                                        " comment-id %s\t%-35s\t%d\t%s\n",
                                        d.commentId, d.defNom, d.defNomAlias.size(), d.defNomAlias.toString()));
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
        
        boolean openInBrowser = doBrowse;
        if ( openInBrowser){
            DesktopBrowse(fIndexFilePathAndName);
            
        }
        

        System.out.printf("FIN : %s\n", ForumLI3DFR.class.getName());
        System.out.flush();
    }

    public static void DesktopBrowse(File fIndexFilePathAndName) {
        if ( Desktop.isDesktopSupported() ) {
            Desktop d = Desktop.getDesktop();
            if (d.isSupported(Desktop.Action.BROWSE)){
                System.out.printf("Trying toURI on \"%s\"\n",fIndexFilePathAndName);
                try {
                    URI toURI = fIndexFilePathAndName.toURI();
                    System.out.printf("Trying to browse %s\n",toURI);
                    d.browse(toURI);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(ForumLI3DFR.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                //
                System.out.printf("Faild to browse %s\n",fIndexFilePathAndName);
            }
        } else{
            // TODO msg : Desktop not supported ignoring Desktop.browse ...
            System.out.printf("Desktop not supported ignoring: browse %s\n",fIndexFilePathAndName);
        }
    }

    /**
     * TODO a revoir - Séparer les niveau d'abstraction ( Parse vs Reformatage
     * par creation de multiple class d'heriage . bien décomposer en méthodes )
     *
     * @param sUrl
     * @return
     * @throws IOException
     */
    public static Document loadMayByCachedDocumentFromUrl(String sUrl, String homeq6req_curl_PPACtxt) throws IOException {

        HashMap<String, String> mapUrlElem = new HashMap<>();

        //Connection connect = Jsoup.connect(sUrlSectionPetg);
        //TODO si cache ex: Document doc = Jsoup.parse(bookmarkHtmlFile, "UTF-8");
        Document doc = UrlCDownloderCache.cacheAndParseUrl(sUrl, false, UrlCDownloderCache.debugPrintUrlHeaders, false, homeq6req_curl_PPACtxt);//connect.get();
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);

        // todo a refaire avec un Matcheur ?
        String idSujet = "";
        int posPreIdTopic = doc.baseUri().indexOf("/topic/");
        if (posPreIdTopic > -1) {
            int posPosIdTopic = doc.baseUri().indexOf("-", posPreIdTopic + 7);
            if (posPosIdTopic > posPreIdTopic) {
                idSujet = doc.baseUri().substring(posPreIdTopic + 7, posPosIdTopic);
                System.out.println(" id sujet = " + idSujet);
            }
        }

        //
        boolean outputPageTitles = false;
        if (outputPageTitles) {
            outTitles(doc);
        }

        if (false) {
            UrlCParser.outDocumentHeadMetaProperty(doc);
        }

        outFilDarian_Breadcrum_top(doc);

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
                // TODO revoir Héritage qui fait quoi pour faire les 3 lignes suivate en une.
                ForumUneDef uneDef = new ForumUneDef();
                uneDef.setSujetId(idSujet);
                uneDef.parseComment(eACommentId);
            }
        }

        // ssi page des likes "/?do=showReactionsComment&comment=524461&reaction=all"
        /*
        😡 grr ?https://emojipedia.org/pouting-face/
        ---
        Grrr
        Triste...
        Confus
        Wow
        Haha
        +1
        Merci !
        J'aime
        ---
        
         */
        if (true) {
            String docBaseUri = doc.baseUri();

            int posT1 = docBaseUri.lastIndexOf("/?do=showReactionsComment&comment=");
            System.out.println("? page historique like : " + (posT1 > 0));
            int posT2 = docBaseUri.lastIndexOf("&reaction=all"); // ? ssi en 1er page des reaction
            System.out.println("? page historique ALL like : " + (posT1 > 0 && posT2 > posT1));
            Elements select = doc.select("ol.ipsGrid li.ipsGrid_span6");
            if (select != null) {
                System.out.println("? li des likes ? cpt = " + select.size());
                for (Element l : select) {
                    String uA = l.selectFirst("a[href~=https://www.lesimprimantes3d.fr/forum/profile/]").attr("href").substring(46);
                    String dL = l.selectFirst("time").attr("datetime");
                    String uIR = l.selectFirst("img[src~=https://www.lesimprimantes3d.fr/forum/uploads/reactions/]").attr("src").substring(56);
                    System.out.printf("%s %s \t \t %s\n", dL, uA, uIR);
                }

            } else {
                System.out.println("? li des likes ? NONE ");
            }

        }

        System.out.flush();

        return doc;
    }

    public static void outTitles(Document doc) {
        Elements titles = doc.select("title");
        //print all titles in main page
        for (Element e : titles) {
            boolean outputTitelsElemetnsHtml = true;

            if (outputTitelsElemetnsHtml) {
                System.out.println("  title : " + e.html());
            } else {
                System.out.println("  title : " + e.text());
            }
        }
    }

    public static void outFilDarian_Breadcrum_top(Document doc) {
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
    }

}
