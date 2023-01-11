/*
 */
package com.ppac37.EText2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author q6
 */
public class UrlCDownloderCache {

    static String cacheBaseDir = "./www_cache_w/"; //TODO a revoir car va changer dans la dist

    /**
     *
     */
    public static DateFormat dateFormat = DateFormat.getInstance();

    /**
     *
     */
    public static boolean debugPrintUrlHeaders = true;

    /**
     *
     */
    public static boolean debugTimming = false;

    /**
     *
     */
    public static NumberFormat numberFormat = NumberFormat.getInstance();
    private static Random rand = new Random();
    private static long minTempo = 1000;
    //
    static boolean showDebugCacheDirFileDest = false;
    private static boolean showDebugCacheDirBase = false;
    // Pour ne pas trop "sur-charger le serveur" on va mettre des petit delais (aleatoire)
    private static boolean noTempo = true;
    private static long maxTempo = 10000;

    private static void sleepRandomTimes() {
        if (noTempo) {
            return;
        }
        try {
            long tmpRand = rand.nextLong(minTempo, maxTempo);
            //System.out.printf("sleep %20d + %d\n",System.currentTimeMillis(),tmpRand);
            Thread.sleep(tmpRand);
        } catch (InterruptedException ex) {
            Logger.getLogger(ForumLI3DFR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static File getWwwCacheBaseDirAndCreateBaseDirs(URL url) {
        File fBaseCacheDir = new File(UrlCDownloderCache.cacheBaseDir, url.getHost());
        if (fBaseCacheDir.exists() && fBaseCacheDir.isDirectory() && fBaseCacheDir.canRead() && fBaseCacheDir.canWrite()) {
            // ok
            if (showDebugCacheDirBase) {
                System.out.printf("  using cache dir : %s\n", fBaseCacheDir.getAbsolutePath());
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

    static void debugUrl(URL url) throws URISyntaxException {
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
        System.out.printf(" Query : %s\n", url.getQuery()); // ? ... & ...
        System.out.printf(" Ref : %s\n", url.getRef()); // #
        System.out.printf(" Authority : %s\n", url.getAuthority());
        System.out.printf(" UserInfo : %s\n", url.getUserInfo());
    }

    //
    //
    //
    static void urlHttpConnecteAndWriteToFile(URL url, File fCache) {
        //FileWriter fw = new FileWriter(fCache);
        // TODO prise en compte des redirection ( avoir la chaine de redirection ...)
        //HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection.setFollowRedirects(true);
        // TODO une vrai gestion des cookies ...
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (true) {
                //pour etre authentifié ... on pique les cookies via un inspecter , network , ...  et l'on copie le curl que l'on met dans un fichier
                // parser la req curl pour faire les bon connection.setRequestProperty("","");
                try {
                    File file = new File("/home/q6/req_curl_PPAC.txt");
                    Scanner scr = null;
                    if ( file.exists()){
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
                        Logger.getLogger(ForumLI3DFR.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }else{
                        // TODO
                    }
                } catch (Exception e) {
                    //... TODO mais pas critique on ne sera ju
                    Logger.getLogger(ForumLI3DFR.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            //connection.setRequestProperty ( "User-agent", "Opera/9.80 (Windows NT 5.1; U; fr) Presto/2.7.62 Version/11.01");
            //        connection.setRequestProperty("User-agent", "Nokia6230i/2.0 (03.25) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            Map<String, List<String>> requestProperties = connection.getRequestProperties();
            if ( false ){
                System.out.println(requestProperties.toString());
            }
            String header = connection.getHeaderField(0);
            if (false) {
                System.out.println(header);
            }
            if (false) {
                System.out.println("---Start of headers---");
                int i = 1;
                while ((header = connection.getHeaderField(i)) != null) {
                    String key = connection.getHeaderFieldKey(i);
                    System.out.println(((key == null) ? "" : key + ": ") + header);
                    i++;
                }
                System.out.println("---End of headers---");
            }
            if (UrlCDownloderCache.debugPrintUrlHeaders) {
                System.out.printf("Url : %s\nUrlc: %s\nContent :: Encoding : %s Length : %s Type %s\n", url, connection.getURL(), connection.getContentEncoding(), UrlCDownloderCache.numberFormat.format(connection.getContentLength()), connection.getContentType());
                //            Map<String, List<String>> header = urlC.getHeaderFields();
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
            System.out.printf("-> %s %s\n", fCache.getAbsolutePath(), UrlCDownloderCache.numberFormat.format(fCache.length()));
            if (charcount != fCache.length()) {
                System.out.printf("Erreur DL : %s %s\n", fCache.getAbsolutePath(), UrlCDownloderCache.numberFormat.format(charcount));
            }
            //TODO ... lecture
        } catch (IOException e) {
            //System.out.println(e);
            Logger.getLogger(ForumLI3DFR.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    static Document cacheAndParseUrl(String sUrlToParse, boolean doNextPage, boolean debugUrl, boolean debugParsed) {
        try {
            // Construct a URL object
            URL url = new URL(sUrlToParse);
            File fBaseCacheDir = UrlCDownloderCache.getWwwCacheBaseDirAndCreateBaseDirs(url);
            if (debugUrl) {
                System.out.printf(" sUrlToParse                 : %s\n", sUrlToParse);
                UrlCDownloderCache.debugUrl(url);
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
            if (UrlCDownloderCache.showDebugCacheDirFileDest) {
                System.out.printf("  cache dir : %s\n", fDirDest.getAbsoluteFile());
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
                if (false) {
                    System.out.printf("  using cache file : %s\n", fDirDest.getAbsoluteFile());
                }
                // il y a un fichier // faut t'il l'effacer ?

            } else {
                System.out.printf("  save to cache file : %s\n", fDirDest.getAbsoluteFile());
                long tsub0 = System.currentTimeMillis();
                UrlCDownloderCache.urlHttpConnecteAndWriteToFile(url, fCache);
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
            Logger.getLogger(ForumLI3DFR.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(ForumLI3DFR.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
