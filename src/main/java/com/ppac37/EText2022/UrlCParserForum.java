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
 * Outil pour "parser" une page du forum. TODO a renomer ForumUrl ? ForumPageUrl
 * ?
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
    boolean debugToNextPage = true;

    /**
     *
     * @param sUrl
     * @param doToLastPage
     */
    public UrlCParserForum(String sUrl, boolean doToLastPage) {
        super(sUrl);
        String httpswwwlesimprimantes3dfr = "https://www.lesimprimantes3d.fr";

        // TODO Selon le contexte d'utilisation action ou non selon type d'url        
        if (sUrl.matches(httpswwwlesimprimantes3dfr + "/.*")) {
            //Bien une page sur li3d.fr donc peut etre du forum ou du wordpress
            // Enfin sauf si un fichier joint ou une image
            /*
            https://www.lesimprimantes3d.fr/forum/uploads/monthly_2023_01/image.png.b332a9683b390d9cf6339d6303668563.png
            */
            if (sUrl.matches(httpswwwlesimprimantes3dfr + "/forum/[0-9]*-.*")) {
                // ex : "https://www.lesimprimantes3d.fr/forum/138-tutoriels/"
                // Un forum , donc y chercher une liste de sujet ?
                /*
                <ul class="ipsMenu ipsMenu_auto ipsMenu_withStem ipsMenu_selectable ipsHide ipsMenu_bottomCenter" id="elSortByMenu_82ee45fe59033407fcca2aeb12c7d039_menu" style="left: 437.992px; top: 583.094px; position: absolute; z-index: 5300; opacity: 1; display: block;" animating="false">
						
						
							<li class="ipsMenu_item" data-ipsmenuvalue="last_post" data-sortdirection="desc"><a href="https://www.lesimprimantes3d.fr/forum/138-tutoriels/?sortby=last_post&amp;sortdirection=desc" rel="nofollow">Récemment mis à jour</a></li>
						
							<li class="ipsMenu_item ipsMenu_itemChecked" data-ipsmenuvalue="title" data-sortdirection="asc" id="ips_uid_5715_3"><a href="https://www.lesimprimantes3d.fr/forum/138-tutoriels/?sortby=title&amp;sortdirection=asc" rel="nofollow">Titre</a></li>
						
							<li class="ipsMenu_item " data-ipsmenuvalue="start_date" data-sortdirection="desc"><a href="https://www.lesimprimantes3d.fr/forum/138-tutoriels/?sortby=start_date&amp;sortdirection=desc" rel="nofollow">Date de création</a></li>
						
							<li class="ipsMenu_item " data-ipsmenuvalue="views" data-sortdirection="desc"><a href="https://www.lesimprimantes3d.fr/forum/138-tutoriels/?sortby=views&amp;sortdirection=desc" rel="nofollow">Nombre de visualisations</a></li>
						
							<li class="ipsMenu_item " data-ipsmenuvalue="posts" data-sortdirection="desc"><a href="https://www.lesimprimantes3d.fr/forum/138-tutoriels/?sortby=posts&amp;sortdirection=desc" rel="nofollow">Nombre de réponses</a></li>
						
						
							<li class="ipsMenu_item " data-noselect="true">
								<a href="https://www.lesimprimantes3d.fr/forum/138-tutoriels/?advancedSearchForm=1&amp;sortby=forums_topics.last_post&amp;sortdirection=DESC" rel="nofollow" data-ipsdialog="" data-ipsdialog-title="Tri personnalisé">Personnaliser</a>
							</li>
						
					</ul>
                */
                ///?sortby=title&sortdirection=asc
            }
            if (sUrl.matches(httpswwwlesimprimantes3dfr + "/forum/topic/[0-9]*-.*")) {
                // ex : "https://www.lesimprimantes3d.fr/forum/topic/19909-tuto-modifier-un-stl-avec-blender-en-5-minutes/"
                // Un sujet , donc y chercher des commentaires ?
                // sauf si sujet en édition ex "https://www.lesimprimantes3d.fr/forum/topic/19909-tuto-modifier-un-stl-avec-blender-en-5-minutes/?do=edit"
                // ou sauf si commentaire en édition ex "https://www.lesimprimantes3d.fr/forum/topic/19909-tuto-modifier-un-stl-avec-blender-en-5-minutes/?do=editComment&comment=249843"
                // ou sauf si nb de like d'un commentaire ex "https://www.lesimprimantes3d.fr/forum/topic/19909-tuto-modifier-un-stl-avec-blender-en-5-minutes/?do=showReactionsComment&comment=249890&reaction=2"
                
                //
                
            }

        } else {
            // 
        }

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
        if (forms != null) {
            int fNum = 0;
            for (Element e : forms) {
                fNum++;

                Elements inputs = e.select("input, button, textarea");
                System.out.printf("# form num %d have %s inputs\n", fNum, inputs.size());

                Attributes attributes = e.attributes();
                for (Attribute attrib : attributes) {
                    System.out.printf(" * %s = %s\n", attrib.getKey(), attrib.getValue());
                }

                for (Element i : inputs) {
                    System.out.printf(" - %s\n", i.outerHtml());
                }

            }
        }

        Elements actionsLink = doc.select("ul.ipsMenu li.ipsMenu_item a[data-action=\"editComment\"]");
        System.out.printf("# Edit link .size=%d\n", actionsLink.size());
        for (Element al : actionsLink) {
            System.out.printf(" ? %s\n", al.attr("href"));
        }

        String sSelect = "ol.cForumTopicTable li.ipsDataItem";
        Elements elems = doc.select(sSelect);
        System.out.printf("# select(\"%s\").size(): %d\n", sSelect, elems.size());

        for (Element e : elems) {
            System.out.printf(" attrib data-rowid: %s\n", e.attr("data-rowid"));

            Element urlSujet = e.selectFirst("h4.ipsDataItem_title span.ipsContained a");
            if (urlSujet != null) {
                System.out.printf("  [%s](%s)\n", urlSujet.text(), urlSujet.attr("href"));
            }
        }

        return res;
    }

}
