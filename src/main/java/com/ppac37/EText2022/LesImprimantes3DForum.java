/*
 */
package com.ppac37.EText2022;

import com.pnikosis.html2markdown.HTML2Md;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public static void main(String[] args) {

        System.out.printf("DEBUT : %s\n", LesImprimantes3DForum.class.getName());

        if (modeDev) {
            // Jeux d'essai pour le dev.
            String[] urls = {
                "https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/?sortby=date#comments",
                "https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/page/2/?sortby=date#comments",
                "https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/page/3/?sortby=date#comments"
            //             , ""    
            };

            for (String sUrl : urls) {
                System.out.printf(" %s\n", sUrl);
                try {
                    // TODO analyse de l'url (si ou non une parties de parametres)
                    getSpecificMessage(sUrl);
                } catch (IOException ex) {
                    Logger.getLogger(LesImprimantes3DForum.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            // TODO
        }

        System.out.printf("FIN : %s\n", LesImprimantes3DForum.class.getName());
        System.out.flush();
    }

    public static void getSpecificMessage(String sUrl) throws IOException {

        HashMap<String, String> mapUrlElem = new HashMap<>();

        long t0 = System.currentTimeMillis();
        //Connection connect = Jsoup.connect(sUrlSectionPetg);

        Document doc = cacheAndParseUrl(sUrl, false, debugPrintUrlHeaders, false);//connect.get();
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);

        long t1 = System.currentTimeMillis();
        System.out.println(sUrl + " ( in " + (t1 - t0) + " ms.)");
        //TODO si cache ex: Document doc = Jsoup.parse(bookmarkHtmlFile, "UTF-8");

        Elements titles = doc.select("title");

        //print all titles in main page
        for (Element e : titles) {
            System.out.println("text: " + e.text());
            if (false) {
                System.out.println("html: " + e.html());
            }
        }
        long t3 = System.currentTimeMillis();
        if (false) {
            System.out.println("in " + (t3 - t1) + " ms.");
        }

        if (true) {
            outDocumentHeadMetaProperty(doc);
        }

        //nav.ipsBreadcrumb  > <ul data-role="breadcrumbList"> li ...
        if (true) {
            String sSelectNavBar = "nav.ipsBreadcrumb_top  > ul[data-role=breadcrumbList] > li";
            Elements elemNavBar = doc.select(sSelectNavBar);
            System.out.println("Total (" + sSelectNavBar + "): " + elemNavBar.size());

            for (Element e : elemNavBar) {
                System.out.printf("  >%d %s\n", e.childrenSize(), e.html());

            }
        }

        //<div class="ipsPageHeader ipsResponsive_pull ipsBox ipsPadding sm:ipsPadding:half ipsMargin_bottom">
        // <h1 class="ipsType_pageTitle ipsContained_container">
        //<div class="ipsPageHeader__meta ipsFlex ipsFlex-jc:between ipsFlex-ai:center ipsFlex-fw:wrap ipsGap:3">flex
        //todo
        //
        boolean doComment = true;
        if (doComment) {
            String sSelectNavBar = "a[id^=comment-]";
            Elements elemNavBar = doc.select(sSelectNavBar);
            System.out.println("Total (" + sSelectNavBar + "): " + elemNavBar.size());

            for (Element eACommentId : elemNavBar) {

                Element nextElementSibling = eACommentId.nextElementSibling();
                if (false) {
                    System.out.printf(" \"%s\"\n", eACommentId.attr("id"));
                }

                String tmpCommentId = "";
                if (eACommentId.attr("id").startsWith("comment-")) {
                    tmpCommentId = eACommentId.attr("id").substring(8);
                    System.out.printf("https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/?do=findComment&comment=%s\n", tmpCommentId);
                    System.out.printf(" comment id \"%s\"\n", tmpCommentId);
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

                // heure 
                Elements dateMsg = nextElementSibling.select("time"); //"div.ipsType_reset");
                String sTmpLastDateTime = "";
                for (Element d : dateMsg) {
                    if (false) {
                        System.out.printf("  dateMsg :  %s ( %s ) %s :: %s\n", d.attr("title"), d.text(), d.attr("datetime"), d.parent().text());
                    }
                    String sDateTime = d.attr("datetime");
                    if (sDateTime.equals(sTmpLastDateTime)) {
                        // on ignore car on a deja sortie cette date précédament
                    } else {
                        System.out.printf("  //  %s ( %s ) %s :: %s\n", d.attr("title"), d.text(), d.attr("datetime"), d.parent().text());
                    }
                    sTmpLastDateTime = sDateTime;
                }

                // le message
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
                    System.out.printf("---\n%s\n---\n", commentContent.html());
                    System.out.printf("---\n%s\n---\n", HTML2Md.getTextContent(commentContent)); //HTML2Md.convertHtml(commentContent.html(), "UTF-8"));
                }
                if (true && commentContent != null) {

                    Elements selectCommentH2 = commentContent.select("h2");
                    if (!selectCommentH2.isEmpty()) {
                        // le commentaire contien au moins un "titre 2"
                        for (Element c_elemH2 : selectCommentH2) {
                            if (c_elemH2.text().equalsIgnoreCase("Sommaire")) {
                                System.out.printf(" H2: \"%s\" comment id %s\n", c_elemH2.text(), eACommentId.attr("id").substring(8));
                            } else {
                                System.out.printf(" H2: \"%s\"\n", c_elemH2.text());

                            }
                        }
                    }

                }
                if (true && commentContent != null) {

                    Elements selectCommentH2 = commentContent.select("ul li strong");
                    if (!selectCommentH2.isEmpty()) {
                        // le commentaire contien au moins un "titre 2"
                        for (Element c_elemH2 : selectCommentH2) {

                            System.out.printf(" * \"%s\"\n", c_elemH2.text());
                        }
                    }

                }
                System.out.printf("---\n%s\n---\n", HTML2Md.getTextContent(commentContent)); //HTML2Md.convertHtml(commentContent.html(), "UTF-8"));

                System.out.println();
            }

            System.out.println();
        }

        System.out.flush();
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

    private static boolean showDebugCacheDirBase = false;

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

}
