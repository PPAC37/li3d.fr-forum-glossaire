/*
 */
package com.ppac37.EText2022;

import static com.ppac37.EText2022.ForumLI3DFR.loadMayByCachedDocumentFromUrl;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Outil pour "parser" une page du forum.
 * TODO a renomer ForumUrl ? ForumPageUrl ?
 * @author q6
 */
public class UrlCParserForum extends UrlCParser {

    String sIdSujet = "";//TODO
    String sPageTitle = "";//TODO

    int iPageNumActive = -1;
    int iPageNumLastPage = -1;
    String sUrlNextPage = "";

    boolean debugNums = false;
    boolean debugToNextPage = true;

    /**
     *
     * @param sUrl
     * @param doToLastPage
     */
    public UrlCParserForum(String sUrl, boolean doToLastPage) {
        super(sUrl);

        try {
            parseForms();
            iPageNumActive = parsePageNumActive();
            parseUrlNextPage();
            parsePageNumLastPage();
            if (doToLastPage) {
                if (iPageNumActive < iPageNumLastPage) {
                    for (int numPage = (iPageNumActive + 1); numPage <= iPageNumLastPage; numPage++) {
                        String sUrlOtherPageToDo = sUrlNextPage.replaceFirst("/page/.*/", "/page/" + numPage + "/");
                        sUrlOtherPageToDo = sUrlOtherPageToDo.replaceFirst("\\?page=[0-9]*", "?page=" + numPage + "");
                        sUrlOtherPageToDo = sUrlOtherPageToDo.replaceFirst("\\&page=[0-9]*", "&page=" + numPage + "");
                        if (debugNums || debugToNextPage) {
                            System.out.printf(" to page %d/%d\n %s\n", numPage, iPageNumLastPage, sUrlOtherPageToDo);
                        }
                        loadMayByCachedDocumentFromUrl(sUrlOtherPageToDo, this.homeq6req_curl_PPACtxt);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ForumLI3DFR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //
    // TODO a déplacer vers ForumPage ?
    //
    
    private void parsePageNumLastPage() {
        Element pLast = doc.selectFirst("li.ipsPagination_last a");
        if (pLast != null) {
            String sNumPage = pLast.attr("data-page");
            if (debugNums) {
                System.out.printf("Last Page %s\n", sNumPage);
            }
            try {
                iPageNumLastPage = Integer.parseInt(sNumPage);
            } catch (NumberFormatException e) {
                iPageNumLastPage = -1;
            }
        }
    }

    private void parseUrlNextPage() {
        Element pNext = doc.selectFirst("li.ipsPagination_next a");
        if (pNext != null) {
            String sNumPage = pNext.attr("data-page");
            if (debugNums) {
                System.out.printf("Page Next %s\n", sNumPage);
            }
            sUrlNextPage = pNext.attr("abs:href");
        }
    }

    private int parsePageNumActive() {
        Integer res = -1;
        Element pActive = doc.selectFirst("li.ipsPagination_active a");
        if (pActive != null) {
            String sNumPage = pActive.attr("data-page");
            if (debugNums) {
                System.out.printf("Page %s\n", sNumPage);
            }
            try {
                res = Integer.parseInt(sNumPage);
            } catch (NumberFormatException e) {
                res = -1;
            }
        }
        return res;
    }

    private int parseForms() {
        Integer res = -1;
        /*
         enctype = multipart/form-data
        */
        Elements forms = doc.select("form[enctype=\"multipart/form-data\"]");
        if ( forms != null ){
            int fNum = 0;
            for ( Element e :forms){
                fNum++;
                
                Elements inputs = e.select("input, button, textarea");
                System.out.printf("# form num %d have %s inputs\n",fNum,inputs.size());
                
                Attributes attributes = e.attributes();
                for ( Attribute attrib : attributes){
                    System.out.printf(" * %s = %s\n",attrib.getKey(),attrib.getValue());                    
                }
                
                for ( Element i : inputs){
                    System.out.printf(" - %s\n",i.outerHtml());                    
                }
                
            }
        }
        
        Elements actionsLink = doc.select("ul.ipsMenu li.ipsMenu_item a[data-action=\"editComment\"]");
        System.out.printf("# Edit link .size=%d\n",actionsLink.size());
        for ( Element al : actionsLink){
                   System.out.printf(" ? %s\n",al.attr("href"));                    
        }
        
        
        String sSelect = "ol.cForumTopicTable li.ipsDataItem";
        Elements elems = doc.select(sSelect);
        System.out.printf("# select(\"%s\").size(): %d\n",sSelect, elems.size());
        
        for ( Element e : elems){
             System.out.printf(" attrib data-rowid: %s\n",e.attr("data-rowid"));   
             
             Element urlSujet = e.selectFirst("h4.ipsDataItem_title span.ipsContained a");
             if ( urlSujet != null){
                 System.out.printf("  [%s](%s)\n",urlSujet.text(),urlSujet.attr("href"));   
             }
        }
        
        
        
        
        return res;
    }

}
