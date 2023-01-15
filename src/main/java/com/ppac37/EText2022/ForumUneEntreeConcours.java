/*
 */
package com.ppac37.EText2022;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author q6
 */
public class ForumUneEntreeConcours extends ForumUneDef implements Comparable<Object> {

    boolean isRejetedEntry = false;
    boolean isVerifiedValideEntry = false;

    public ForumUneEntreeConcours(ForumUneDef d) {
        super(d);
    }

    /**
     * Pour permetre de comparer et trier ...dans l'order du 1er gagnats(le plus de reactions) au dernier suivie des entrèes rejetés.
     * 
     * @param o
     * @return 
     */
    @Override
    public int compareTo(Object o) {        
        if (o == null) {
            return -1;
        } else {
            if (o instanceof ForumUneEntreeConcours) {
                ForumUneEntreeConcours d = (ForumUneEntreeConcours) o;
                if (d.isRejetedEntry != this.isRejetedEntry) {
                    // une entré rejeté face a une entré non rejeté, ce met en fin .
                    if (d.isRejetedEntry) {
                        return +1;
                    } else {
                        return -1;
                    }
                } else {
                    // On trie par nombre de réaction et par date de création du commentaire en cas d'égalité.
                    if (d.getReactionsTotals() == this.getReactionsTotals()) {
                        return d.getDateCreation().compareTo(this.getDateCreation());
                    } else {
                        return d.getReactionsTotals() - this.getReactionsTotals();
                    }
                }
            } else if (o instanceof ForumUneDef) {
                ForumUneDef d = (ForumUneDef) o;
                // On trie par nombre de réaction et par date de création du commentaire en cas d'égalité.
                if (d.getReactionsTotals() == this.getReactionsTotals()) {
                    return d.getDateCreation().compareTo(this.getDateCreation());
                } else {
                    return d.getReactionsTotals() - this.getReactionsTotals();
                }
            } else {
                // TODO
                System.err.printf("TODO UnDef comparaTo class %s\n", o.getClass().getSimpleName());
                return -1;
            }
        }
    }


    /**
     * TODO a revoir trop de trucs codé en dur.( le style pas exemple ... ) 
 Pour ne citer que les images (non emojie) contenus dans le Coprs Du CommentaireBrut, avec modification du style de ses image pour les avoir en vignettes.
     * @param imgStyleToUse une valeur de style css, du genre de "width:auto;height:200px;" ou de "max-width:150px;max-height:150px;width: auto;height: auto;".
     * @return 
     */
    public String createThumbsCitation(String imgStyleToUse) {

        String citationMsg = this.alImgsUrl.size() + " image(s) du commentaire de " + this.commentAuteurNom ;
        Document docRes = Jsoup.parse("");
        Element bodyDocRes = docRes.body();        
        Element resDivQuote = createElementAsCitation(bodyDocRes, citationMsg);

        Document docOrig = Jsoup.parse(getCommentCorpHTMLBrut());
        if (resDivQuote != null) {
            //selectFirst.appendChildren(docOrig.select("a.ipsAttachLink_image"));
            {
                Elements selectOriImgs = docOrig.select("img"
                // les image collé depuis un lien externe n'on pas se qui suit ! ?
                //        + ".ipsImage_thumbnailed"
                );
                if (selectOriImgs != null) {
                    for (Element e : selectOriImgs) {

                        Element imgParent = e.parent();

                        if (e.classNames().contains("ipsEmoji")) {
                            // Les caractères unicode emoji sont transformé en image par l'éditeur ... 
                            // il faut donc ne pas afficher ses images car se sont les emojis
                        } else {
                            // donc voila une image
                            //bidouille pour avoir des vignettes ( mais implique que le client soit avec un navigateur compatible html5 ? )
                            e.attr("style", imgStyleToUse );
                            // TODO a revoir pour éviter de reprendre un lien malvélliant
                            // donc uniquement si le lien cible le forum !
                            if (imgParent.is("a")
                                    && imgParent.attr("href").startsWith("https://www.lesimprimantes3d.fr/")) {

                                resDivQuote.appendChild(imgParent);
                            } else {
                                // TODO pas un lien vers le forum alors mettre une bordure en rouge ou un logo de "warnig external link" ...

                                resDivQuote.appendChild(e);
                            }
                            boolean warnExternalImg = true;
                            if (!e.attr("src").startsWith("https://www.lesimprimantes3d.fr/") && warnExternalImg) {
                                resDivQuote.appendText("( ! img src externe " + e.attr("src")
                                        + " ) ");
                            } else {
                                resDivQuote.appendText(" ");
                            }
                        }
                    }
                }
            }
            Elements imgs = resDivQuote.select("img");
            if (imgs != null) {
                for (Element e : imgs) {
                    e.attr("style", imgStyleToUse);
                }
            }
        }
        return docRes.body().html();
    }

    /**
     * Append to an element the structure of the Forum citation ...
     * @param elementToAppendTheCitationElementsCreated an element in a Jsoup Document wherre to append all the "bordel" ...
     * @param citationMsg seems to be errased when the forum editor recive it mais du genre "     Le 30/11/2022 at 19:47, PPAC a dit" et y a un "&nbsp;:\n" ...
     * @return the Element where to append the contente of the citation
     */
    public Element createElementAsCitation(Element elementToAppendTheCitationElementsCreated, String citationMsg) {
        /*
        
        <span data-cke-copybin-start="1">​</span>
        <div tabindex="-1" contenteditable="false" data-cke-widget-wrapper="1" data-cke-filter="off" class="cke_widget_wrapper cke_widget_block cke_widget_ipsquote cke_widget_wrapper_ipsQuote cke_widget_focused cke_widget_selected" data-cke-display-name="blockquote" data-cke-widget-id="1" role="region" aria-label="Élément blockquote">
        <blockquote class="ipsQuote cke_widget_element" data-ipsquote="" data-gramm="false" data-ipsquote-timestamp="1669834058" data-ipsquote-userid="33940" data-ipsquote-username="PPAC" data-ipsquote-contentapp="forums" data-ipsquote-contenttype="forums" data-ipsquote-contentclass="forums_Topic" data-ipsquote-contentid="50419" data-ipsquote-contentcommentid="522096" data-cke-widget-keep-attr="0" data-widget="ipsquote" data-cke-widget-data="%7B%22classes%22%3A%7B%22ipsQuote%22%3A1%7D%7D">
        <div class="ipsQuote_citation">
        Le 30/11/2022 at 19:47, PPAC a dit&nbsp;:
        </div>
        <div class="ipsQuote_contents ipsClearfix cke_widget_editable" data-gramm="false" contenteditable="true" data-cke-widget-editable="content" data-cke-enter-mode="1">
        <p><br></p>
        <p><a class="ipsAttachLink ipsAttachLink_image" data-fileext="jpeg"
        data-fileid="153534" data-cke-saved-href="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.jpeg.f9da9cf270a773ee80fc05af8f0d4063.jpeg"
        href="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.jpeg.f9da9cf270a773ee80fc05af8f0d4063.jpeg"
        rel=""
        data-fullurl="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.jpeg.f9da9cf270a773ee80fc05af8f0d4063.jpeg">
        <img alt="image.thumb.jpeg.4c61ae5faf0a5cc547bc77f4c034ea20.jpeg"
        class="ipsImage ipsImage_thumbnailed" data-fileid="153534"
        data-ratio="75.00" style="width:400px;height:auto;" width="1000"
        data-cke-saved-src="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.thumb.jpeg.4c61ae5faf0a5cc547bc77f4c034ea20.jpeg" src="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.thumb.jpeg.4c61ae5faf0a5cc547bc77f4c034ea20.jpeg"></a>&nbsp;<a class="ipsAttachLink ipsAttachLink_image" data-fileext="jpeg" data-fileid="153535" data-cke-saved-href="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.jpeg.7dc02592a14247a15fceac98d1b1d63f.jpeg" href="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.jpeg.7dc02592a14247a15fceac98d1b1d63f.jpeg" rel="" data-fullurl="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.jpeg.7dc02592a14247a15fceac98d1b1d63f.jpeg"><img alt="image.thumb.jpeg.0aed4ee6ba3c1308b7c5735a15a8e2eb.jpeg" class="ipsImage ipsImage_thumbnailed" data-fileid="153535" data-ratio="75.00" style="width:400px;height:auto;" width="1000" data-cke-saved-src="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.thumb.jpeg.0aed4ee6ba3c1308b7c5735a15a8e2eb.jpeg" src="https://www.lesimprimantes3d.fr/forum/uploads/monthly_2022_11/image.thumb.jpeg.0aed4ee6ba3c1308b7c5735a15a8e2eb.jpeg"></a>&nbsp;</p>
        </div>
        </blockquote><span class="cke_reset cke_widget_drag_handler_container" style="background: url(&quot;//www.lesimprimantes3d.fr/forum/applications/core/interface/ckeditor/ckeditor/plugins/widget/images/handle.png&quot;) rgba(220, 220, 220, 0.5); top: -15px; left: 0px;"><img class="cke_reset cke_widget_drag_handler" data-cke-widget-drag-handler="1" src="data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==" width="15" title="Cliquer et glisser pour déplacer" height="15" role="presentation"></span>
        </div><span data-cke-copybin-end="1">​</span>
        
        */
        //TODO a revoir pour generaliser
        Element append = elementToAppendTheCitationElementsCreated.append(
                "<span data-cke-copybin-start=\"1\">​</span>"
                        + "<div "
                        + "tabindex=\"-1\" "
                        + "contenteditable=\"false\" "
                        + "data-cke-widget-wrapper=\"1\" "
                        + "data-cke-filter=\"off\" "
                        + "class=\"cke_widget_wrapper cke_widget_block cke_widget_ipsquote cke_widget_wrapper_ipsQuote cke_widget_focused cke_widget_selected\" "
                        + "data-cke-display-name=\"blockquote\" "
                        + "data-cke-widget-id=\"1\" "
                        + "role=\"region\" "
                        + "aria-label=\"Élément blockquote\""
                        + ">\n"
                        + "   <blockquote "
                        + "class=\"ipsQuote cke_widget_element\" "
                        + "data-ipsquote=\"\" "
                        + "data-gramm=\"false\" "
                        + "data-ipsquote-timestamp=\""
                        //+ "1669834058"
                        + getForumTimeStampFromForumDateTime(this.getCommentDateCreation())
                        + "\" "
                                + "data-ipsquote-userid=\""
                        //+ "33940"
                        + this.commentAuteurId
                        + "\" "
                                + "data-ipsquote-username=\""
                        //                + "PPAC"
                        + this.commentAuteurNom // TODO surment a encodé ? ou les '"' a virer ou "proteger" car valeur d'un attribut d'un tag html ... ?
                        + "\" "
                                + "data-ipsquote-contentapp=\"forums\" "
                                + "data-ipsquote-contenttype=\"forums\" "
                                + "data-ipsquote-contentclass=\"forums_Topic\" "
                                + "data-ipsquote-contentid=\""
                        //                + "50419"
                        + this.sujetId
                        + "\" "
                                + "data-ipsquote-contentcommentid=\""
                        //+ "522096"
                        + this.commentId
                        + "\" "
                                + "data-cke-widget-keep-attr=\"0\" "
                                + "data-widget=\"ipsquote\" "
                                + "data-cke-widget-data=\"%7B%22classes%22%3A%7B%22ipsQuote%22%3A1%7D%7D\">\n"
                                + "    <div class=\"ipsQuote_citation\">\n"
                        // + "     Le 30/11/2022 at 19:47, PPAC a dit&nbsp;:\n"
                        +citationMsg+ "&nbsp;:\n"
                                + "    </div>\n"
                                + "    <div "
                                + "class=\"ipsQuote_contents ipsClearfix cke_widget_editable\" "
                                + "data-gramm=\"false\" contenteditable=\"true\" "
                                + "data-cke-widget-editable=\"content\" "
                                + "data-cke-enter-mode=\"1\">\n"
                                + "    </div>\n"
                                + "   </blockquote>"
                                + "<span "
                                + "class=\"cke_reset cke_widget_drag_handler_container\" "
                                + "style=\"background: url(&quot;//www.lesimprimantes3d.fr/forum/applications/core/interface/ckeditor/ckeditor/plugins/widget/images/handle.png&quot;) rgba(220, 220, 220, 0.5); top: -15px; left: 0px;\">"
                                + "<img class=\"cke_reset cke_widget_drag_handler\" "
                                + "data-cke-widget-drag-handler=\"1\" "
                                + "src=\"data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==\" "
                                + "width=\"15\" title=\"Cliquer et glisser pour déplacer\" height=\"15\" "
                                + "role=\"presentation\">"
                                + "</span>\n"
                                + "  </div>" + "<span data-cke-copybin-end=\"1\">​</span>");
        Element resDivQuote = append.selectFirst("div.ipsQuote_contents");
        return resDivQuote;
    }

    
    static String pattern_yyyyMMddTHHmmssZ = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * 
     * @param sForumDatetime
     * @return a long to use as attrib for an attrib data-ipsquote-timestamp
     */
    private long getForumTimeStampFromForumDateTime(String sForumDatetime) {
        long lForumTimestamp = 0;//
        SimpleDateFormat sdf = new SimpleDateFormat(pattern_yyyyMMddTHHmmssZ);
        String parsableDateTime = sForumDatetime;
        parsableDateTime = parsableDateTime.substring(0, parsableDateTime.length() - 1) + "GMT";
        try {

            lForumTimestamp = sdf.parse(parsableDateTime).getTime() / 1000;
        } catch (ParseException e) {
            System.err.printf("err: parsing forum datetime (%s) to forum timestamp with %s pattern\n",
                    parsableDateTime,
                    pattern_yyyyMMddTHHmmssZ);
            //e.printStackTrace();
        }
        return lForumTimestamp;
    }

}
