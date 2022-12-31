/*
 */
package com.ppac37.EText2022;

import static com.ppac37.EText2022.ForumLI3DFR.loadMayByCachedDocumentFromUrl;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Element;

/**
 *
 * @author q6
 */
public class UrlCParserForum extends UrlCParser {

    String sIdSujet = "";//TODO
    String sPageTitle = "";//TODO

    int iPageNumActive = -1;
    int iPageNumLastPage = -1;
    String sUrlNextPage = "";

    boolean debugNums = false;

    public UrlCParserForum(String sUrl, boolean doToLastPage) {
        super(sUrl);

        try {
            iPageNumActive = parsePageNumActive();
            parseUrlNextPage();
            parsePageNumLastPage();
            if (doToLastPage) {
                if (iPageNumActive < iPageNumLastPage) {
                    for (int numPage = (iPageNumActive + 1); numPage <= iPageNumLastPage; numPage++) {
                        String sUrlOtherPageToDo = sUrlNextPage.replaceFirst("/page/.*/", "/page/" + numPage + "/");
                        if (debugNums) {
                            System.out.printf("WillDo Next Url %s\n", sUrlOtherPageToDo);
                        }
                        loadMayByCachedDocumentFromUrl(sUrlOtherPageToDo);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ForumLI3DFR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

}
