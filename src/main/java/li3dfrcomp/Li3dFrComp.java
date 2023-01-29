/*

 */
package li3dfrcomp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import static stl.index.ThingiverseThing.getIconeUrlAsBase64EncodedString;

/**
 * Pour "aspirer" le comparateur de lesimpriantes3d.fr et pouvoir faire du
 * suivie de mise a jours. teste de non régession. archivage. Aussi avoir un
 * moyen de completer (ajouter / corriger des champs) ? une version
 * collaborative ( a la wiki ? mais aussi pour les champs ?
 *
 * Liens de vente et validité (date d'ajout , date de dernié contrl , date de
 * non disponibilité ?
 *
 * Lien vers les ressources ( ex github / kickstarter / site du constructeur /
 * forum ... ? les composant détails ? ? les problèmes récurent ? détail et
 * solutution eventuel ?
 *
 * ! une sauvagarde de version des sites officiel et forum / documents officiel
 * ou non / images de référence.
 *
 * ! une date de publication / modification / auteur de la modif ... (historique
 * et diff ... cf wiki ) ? un genre de commentaires / blog page / forum / page
 * wiki communotaire ?
 *
 *
 * Une imprimante peut avoir plusieur nom ( ex K8200 ou 3DRag ) Une imprimante
 * peut avoir un constructeur mais pas forcement ( un créateur ou une origine )
 *
 * // todo sgbd sqlite pour la mise en cache ? // todo model de class pour les
 * constructeur / imprimantes ... //mise en cache de la page si n'existe pas //
 * todo rep de mise en cache // todo map de page mise en cahce
 *
 * @author q6
 */
public class Li3dFrComp {

    static String cacheBaseDir = "./www_cache_/";//TODO a revoir car va changer dans la dist 

    public static NumberFormat numberFormat = NumberFormat.getInstance();
    public static DateFormat dateFormat = DateFormat.getInstance();

    public static boolean debugPrintUrlHeaders = true; 
    public static boolean debugTimming = true;
    
    
    // Pour ne pas trop "sur-charger le serveur" on va mettre des petit delais (aleatoire)    
    private static boolean noTempo = true;
    private static long maxTempo = 10000;
    private static long minTempo = 1000;    
        private static  Random rand = new Random();
    
        

    public static void main(String[] args) {

        //https://www.lesimprimantes3d.fr/forum/topic/44035-genius-ai-je-flingu%C3%A9-la-carte-m%C3%A8re/?tab=comments#comment-463048
        //https://www.lesimprimantes3d.fr/forum/topic/44035-genius-ai-je-flingu%C3%A9-la-carte-m%C3%A8re/?do=findComment&comment=461022
        //  static String urlli3dfrComp = "https://www.lesimprimantes3d.fr/comparateur/imprimante3d/page/45/";//index.html?tutu=titi&t=d#111
        boolean doFull = false;
        if (doFull) {
            String sUrlToParse = "https://www.lesimprimantes3d.fr/comparateur/imprimante3d/";//urlli3dfrComp;//args[0]);
            cacheAndParseUrl(sUrlToParse, true, false, false);
        }

        boolean essaiProduitPage = false;
        if (essaiProduitPage) {
            //
            String sUrlToParse = "https://www.lesimprimantes3d.fr/comparateur/imprimante3d/elegoo/jupiter/";
            cacheAndParseUrl(sUrlToParse, false, false, false);
        }
        
        boolean essaiIndexBase = true;
        if (essaiIndexBase) {
            //gdpc-product-type
            String sUrlToParse = "https://www.lesimprimantes3d.fr/comparateur";
            cacheAndParseUrl(sUrlToParse, false, false, false);
        }
        
        // todo les images ... pas avec la même méthode sauf si :
        // todo metre une protection pour eviter de tenter de parser un fichier non text/html 
        // todo revoir le nomage et chemin du repertoire d ecache ... là c'est pas bon
        // là les images local ne serons plus avec le bon chemin ... donc ... puf 
        //https://www.lesimprimantes3d.fr/wp-content/uploads/2014/02/comparateur-imprimante-3d-259x259.png
        
        // todo détection de modification / mise a jours
        // là c'est délicat car il y a pliens de sous parties continuellement mise a jours 
        // le entête de requettes http pour une page complexe ?
        // 
        // donc il faut bien comparez les block/parites qui nous intéresse
        // donc une comparaison sur plusieur niveau plutot que sur le granul le plus fin de parse ...
        // ? quelle niveau ?
        // la page = KO
        // le bloc de selection retenus ? eventuellement ( full compare du outerHtml ? ou des hash ...
        // 
        // 
        
        // TODO telechargement d'un sujet du forum ( l'ensemble des message de la pages et des pages suivant
        // ? nouveau message 
        // ? info changemet titre / autre admin ...
        // ? fichier joint ( ssi identifié )
        // ? todo identification ( cf avec gestions des coockies ?
        // TODO telechargement d'un message
        // mise en forme (les sources des url si le texte n'ai pas la source)
        // les url des image
        // les url des video
        // ? les lien vers (autre message)
        // les entête ( a l'instant t du post ? avatard , nb post, nb like ...) et signature de l'utilisateur
        // ? delta entete et signature
        // date de post, date d'édition
        // ? le réaction
        // ... si effecé / déplaceé / masqué / edité / fusioné  ... ???
        // ? mise en forme wiki / markdown ? pdf ...
        //https://www.lesimprimantes3d.fr/forum/topic/19909-tuto-modifier-un-stl-avec-blender-en-5-minutes/?do=findComment&comment=249843
        //https://www.lesimprimantes3d.fr/forum/topic/19909-tuto-modifier-un-stl-avec-blender-en-5-minutes/?tab=comments#comment-463359
        
        
        
        //jeu d'essai tempo
//        for (int i = 0; i<10; i++){
//            sleepRandomTimes();
//        }
    }

    private static void sleepRandomTimes() {
        if ( noTempo ){
            return;
        }
        try {
            long tmpRand = rand.nextLong(minTempo,maxTempo);
            //System.out.printf("sleep %20d + %d\n",System.currentTimeMillis(),tmpRand);
            Thread.sleep(tmpRand);
        } catch (InterruptedException ex) {
            Logger.getLogger(Li3dFrComp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void cacheAndParseUrl(String sUrlToParse, boolean doNextPage, boolean debugUrl, boolean debugParsed) {
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
            //            URI tmpURI = url.toURI();
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
                if ( debugTimming ){
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
            if ( debugTimming ){
                System.out.println(sUrlToParse + " ( jsoup get in " + (tsub1 - tsub0) + " ms )");
            }
            //
            // merci JSoup ... on commence l'extraction de ce qui nous plais ...
            // La le(s) titre(s) de la page dans l'entete html
            boolean debugTitles = true;
            if (debugTitles) {
                sSelect = "head > title";
                Elements titles = doc.select(sSelect);
                // TODO if debug ... //System.out.println("Total (" + sSelect + "): " + titles.size());
                //print all titles in main page

                for (Element e : titles) {
                    System.out.println("text: " + e.text());
                    System.out.println("html: " + e.html());
                }
            }
            //
            //
            /*  */
            boolean debugIcons = false;
            if (debugIcons) {
                sSelect = "head > link[rel=icon][sizes=\"16x16\"]";//"head > link[rel*=icon]";
                Elements eResSelectLinkRelIcon = doc.select(sSelect);
                System.out.println("Total (" + sSelect + "): " + eResSelectLinkRelIcon.size());
                for (Element l : eResSelectLinkRelIcon) {
                    System.out.printf("%s\n", l.outerHtml());
                    String lhref = l.attr("abs:href");
                    URL urlIcon = new URL(lhref);
                    if (false) {
//                        getIconeUrlAsBase64EncodedString(urlIcon);
                    }

                }
            }

            /*
             */
            boolean debugMetaOg = true;
            if (debugMetaOg) {
                sSelect = "head > meta[property^=og:]";//, title
                Elements eResSelect = doc.select(sSelect);
                System.out.println("Total (" + sSelect + "): " + eResSelect.size());
                for (Element l : eResSelect) {
                    System.out.printf("%s\n", l.outerHtml());
                    //
                    //l.attributes();

                }
            }
            //
            //
            //
            sSelect = "div[class^=gdpc-simple-grid-product]";
            Elements eResSelect = doc.select(sSelect);
            System.out.println("Total (" + sSelect + "): " + eResSelect.size());
            for (Element l : eResSelect) {
                boolean showGridProductDetail = false;
                //System.out.printf("%s\n", l.outerHtml());
                Elements selectProductTitre = l.select(".gdpc-product-title");
                for (Element tL : selectProductTitre) {
                    if (showGridProductDetail) {
                        System.out.printf("# %s\n", tL.text());
                    }
                    System.out.printf("  %s\n", tL.selectFirst("a").absUrl("href"));
                    
                    sleepRandomTimes();
                    cacheAndParseUrl(tL.selectFirst("a").absUrl("href"), doNextPage, debugUrl, debugParsed);
                }
                if (showGridProductDetail) {
                    selectProductTitre = l.select(".gdrts-rating-text");
                    for (Element tL : selectProductTitre) {
                        System.out.printf("  %s\n", tL.text());
                    }

                    selectProductTitre = l.select(".gdpc-product-cmp");
                    for (Element tL : selectProductTitre) {
                        System.out.printf("  %s\n", tL.text());
                    }

                    selectProductTitre = l.select(".main-image>a>img");
                    for (Element tL : selectProductTitre) {
                        System.out.printf("  %s\n", tL.absUrl("src"));
                    }
                }

            }
            // navigation des pages ... 
            // ? todo current vs last 
            // on suppose que l'on n'a deja fait le page avant la page en cours ...
            // TODO ? donner le choix du sens de parcours 
            // TODO vérifier que l'on a toute les pages ?

//            sSelect = ".gdpc-pages-nav";
//            /*Total (.gdpc-pages-nav): 1
//            <div class="gdpc-pages-nav gdpc-navigate-advanced">
//            <div class="gdr2-nav-pager"> <span class="pages">Page 1 sur 45</span><span class="current">1</span><a href="https://www.lesimprimantes3d.fr/comparateur/imprimante3d/page/2/" class="page" title="2">2</a><a href="https://www.lesimprimantes3d.fr/comparateur/imprimante3d/page/3/" class="page" title="3">3</a><a href="https://www.lesimprimantes3d.fr/comparateur/imprimante3d/page/4/" class="page" title="4">4</a><a href="https://www.lesimprimantes3d.fr/comparateur/imprimante3d/page/5/" class="page" title="5">5</a><a href="https://www.lesimprimantes3d.fr/comparateur/imprimante3d/page/10/" class="page" title="10">10</a><a href="https://www.lesimprimantes3d.fr/comparateur/imprimante3d/page/20/" class="page" title="20">20</a><a href="https://www.lesimprimantes3d.fr/comparateur/imprimante3d/page/30/" class="page" title="30">30</a><span class="extend">...</span><a href="https://www.lesimprimantes3d.fr/comparateur/imprimante3d/page/45/" class="last" title="Dernière »">Dernière »</a>
//            </div>
//            </div>*/
//            sSelect = ".last";/*
//            Total (.last): 1
//            <a href="https://www.lesimprimantes3d.fr/comparateur/imprimante3d/page/45/" class="last" title="Dernière »">Dernière »</a>*/
//            //
            sSelect = ".gdpc-pages-nav > .gdr2-nav-pager > .current";
            //Elements 
            eResSelect = doc.select(sSelect);
            boolean showDebugPageNav = false;
            if (showDebugPageNav) {
                System.out.println("Total (" + sSelect + "): " + eResSelect.size());
            }
            for (Element l : eResSelect) {
                if (showDebugPageNav) {
                    System.out.printf("%s\n", l.outerHtml());
                }
                Element nextElementSibling = l.nextElementSibling();
                if (nextElementSibling != null) {
                    String absUrlNextPage = nextElementSibling.absUrl("href");
                    if (showDebugPageNav) {
                        System.out.printf("# TODO next Page href : %s\n", absUrlNextPage);
                    }
                    // todo une liste vs appel directe et tempo ?
                    if (doNextPage) {
                        sleepRandomTimes();
                        cacheAndParseUrl(absUrlNextPage, doNextPage, debugUrl, debugParsed);
                    }
                }

            }

            // Pour un produit             
            // Peut etre mieux de faire une autre methode ... pour bien cloisoner 
            //
            //
            // TODO
            //
            // sSelect = "div.gdpc-container-product";
            sSelect = "div.gdpc-single-media-li3d, div.gdpc-single-main-info-li3d";
            //            Elements
            eResSelect = doc.select(sSelect);
            System.out.println("Total (" + sSelect + "): " + eResSelect.size());
            for (Element l : eResSelect) {
                boolean showGridProductDetail = true;
                //                System.out.printf("%s\n", l.outerHtml());
                Elements selectProductTitre;

                //                selectProductTitre  = l.select(".gdpc-product-title");
                //                for (Element tL : selectProductTitre) {
                //                    if (showGridProductDetail){
                //                        System.out.printf("# %s\n", tL.text());
                //                    }
                //                    System.out.printf("  %s\n", tL.selectFirst("a").absUrl("href"));
                //                }
                selectProductTitre = l.select("span[itemprop=name]");
                for (Element tL : selectProductTitre) {
                    if (true) {
                        System.out.printf("# %s\n", tL.text());
                    }
                    //System.out.printf("  %s\n", tL.selectFirst("a").absUrl("href"));
                }
                //ok
                if (showGridProductDetail) {
                    selectProductTitre = l.select(".gdrts-rating-text");//ok
                    for (Element tL : selectProductTitre) {
                        System.out.printf("  %s\n", tL.text());
                    }

                    //                selectProductTitre = l.select(".gdpc-product-cmp");
                    //                for (Element tL : selectProductTitre) {
                    //                    System.out.printf("  %s\n", tL.text());
                    //                }
                    selectProductTitre = l.select("a[class^=gdpc-company-]");
                    for (Element tL : selectProductTitre) {
                        System.out.printf("  %s (%s %s)\n", tL.text(), tL.attr("class"), tL.attr("abs:href"));
                    }
                    //todo l'image ( ? versifier le title et le alt 
                    //selectProductTitre = l.select(".main-image>a>img");
                    selectProductTitre = l.select(".gdpc-single-image-li3d>a>img");
                    for (Element tL : selectProductTitre) {
                        System.out.printf("  %s title:%s alt:%s\n", tL.absUrl("src"), tL.attr("title"), tL.attr("alt"));
                        System.out.printf("  %s\n", tL.parent().absUrl("href"));
                        
                    }
                    //
                    //
                    //
                    selectProductTitre = l.select(".gdpc-property");//ok enfin ...
                    for (Element tL : selectProductTitre) {
                        System.out.printf("  %s\n", tL.text());
                    }
                }

            }
            // 
            //
            //
            //
            //
            
            sSelect = ".gdpc-product-type-info";
            //Elements 
            eResSelect = doc.select(sSelect);
            boolean showDebugProdTypeInfo = true;
            if (showDebugProdTypeInfo) {
                System.out.println("Total (" + sSelect + "): " + eResSelect.size());
            }
            for (Element l : eResSelect) {
                if (showDebugProdTypeInfo) {
               //     System.out.printf("%s\n", l.outerHtml());
                }
                // a href > img src alt=title  sibiling h3 sibiling p text n produits
                Element selectFirstLink = l.selectFirst("a");
                System.out.printf(" %s\n", selectFirstLink.absUrl("href"));  
                
                Element selectFirstLinkH3 = l.selectFirst("h3>a");
                System.out.printf(" %s\n", selectFirstLinkH3.absUrl("href"));   
                System.out.printf(" %s\n", selectFirstLinkH3.text());
                
                Element selectFirstImg = selectFirstLink.selectFirst("img");
                System.out.printf(" %s\n", selectFirstImg.absUrl("src"));
                System.out.printf(" %s\n", selectFirstImg.attr("title"));
                System.out.printf(" %s\n", selectFirstImg.attr("alt"));
                
                Element selectFirstParag = l.selectFirst("p");                    
                System.out.printf(" %s\n", selectFirstParag.text());
                String textNbProduits =  selectFirstParag.text();
                int posMotProduit = textNbProduits.indexOf(" produits");
                try{
                    if ( posMotProduit > 0){
                        int parseInt = Integer.parseInt(textNbProduits.substring(0, posMotProduit));
                        System.out.printf(" %d\n", parseInt);
                                }
                }catch (NumberFormatException n){
                    
                }
                // todo match et parse integer?
                
                    
//                Element nextElementSibling = l.nextElementSibling();
//                if (nextElementSibling != null) {
//                    String absUrlNextPage = nextElementSibling.absUrl("href");
//                    if (showDebugProdTypeInfo) {
//                        System.out.printf("# TODO next Page href : %s\n", absUrlNextPage);
//                    }
//                    // todo une liste vs appel directe et tempo ?
//                    if (doNextPage) {
//                        sleepRandomTimes();
//                        cacheAndParseUrl(absUrlNextPage, doNextPage, debugUrl, debugParsed);
//                    }
//                }

            }

        } catch (MalformedURLException e) {
            System.out.println(e);
        } catch (IOException ex) {
            Logger.getLogger(Li3dFrComp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Li3dFrComp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void urlHttpConnecteAndWriteToFile(URL url, File fCache) {
        //FileWriter fw = new FileWriter(fCache);

        HttpURLConnection.setFollowRedirects(false);

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestProperty ( "User-agent", "Opera/9.80 (Windows NT 5.1; U; fr) Presto/2.7.62 Version/11.01");
            //        connection.setRequestProperty("User-agent", "Nokia6230i/2.0 (03.25) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            Map<String, List<String>> requestProperties = connection.getRequestProperties();
            System.out.println(requestProperties.toString());
            /**
             * The User-Agent header of the Nokia 6230i cell phone:
             * Nokia6230i/2.0 (03.25) Profile/MIDP-2.0 Configuration/CLDC-1.1
             */

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

            if (debugPrintUrlHeaders) {
                System.out.printf("Url : %s\nContent :: Encoding : %s Length : %s Type %s\n",
                        url,
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
            System.out.println(e);
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

}
