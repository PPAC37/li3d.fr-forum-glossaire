/*
 */
package com.ppac37.EText2022;

import static com.ppac37.EText2022.ForumLI3DFR.loadMayByCachedDocumentFromUrl;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author q6
 */
public class UrlCParser {

    //
    static boolean debugTimming = false;

    String sUrl;
    Document doc;

    public final String homeq6req_curl_PPACtxt;
    /**
     *
     * @param sUrl
     */
    public UrlCParser(String sUrl) {
        this.sUrl = sUrl;
            homeq6req_curl_PPACtxt = "/home/q6/0_nodes_moderation_li3d.fr/req_curl_li3d.fr_PPAC.txt";
        System.out.printf(" %s\n", sUrl);
        try {
            this.doc = loadMayByCachedDocumentFromUrl(sUrl, homeq6req_curl_PPACtxt);
        } catch (IOException ex) {
            Logger.getLogger(ForumLI3DFR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //
    //
    //
    static void outDocumentHeadMetaProperty(Document docsub) {
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

}
