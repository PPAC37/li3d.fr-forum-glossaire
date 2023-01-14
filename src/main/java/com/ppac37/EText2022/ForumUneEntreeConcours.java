/*
 */
package com.ppac37.EText2022;

import java.text.SimpleDateFormat;
import java.util.Locale;
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

    @Override
    public int compareTo(Object o) {
        // pour trier dans l'order des gagnats ?
        if (o == null) {
            return -1;
        } else {
            if (o instanceof ForumUneEntreeConcours) {
                ForumUneEntreeConcours d = (ForumUneEntreeConcours) o;
                if (d.isRejetedEntry != this.isRejetedEntry) {
                    // une entré rejeté 
                    if (d.isRejetedEntry) {
                        return +1;
                    }else {
                        return -1;
                    }
                } else {

                    if (d.getReactionsTotals() == this.getReactionsTotals()) {
                        // ? 
                        return d.getDateCreation().compareTo(this.getDateCreation());
                    } else {
                        return d.getReactionsTotals() - this.getReactionsTotals();
                    }
                }
            } else if (o instanceof ForumUneDef) {
                ForumUneDef d = (ForumUneDef) o;

                // TODO affinier avec la date de creation et de modification pour comparaison d'historique de commentaire 
                if (d.getReactionsTotals() == this.getReactionsTotals()) {
                    // ? 
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
    public String createThumbsCitation() {
        Document docRes = Jsoup.parse("");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT);
        long timestamp = 0;
        try {
            //TODO revoir j'ai un décalage d'une heure ... cf GMT+0 
            timestamp = sdf.parse(this.getCommentDateCreation()).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        docRes.body().append(
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
                + timestamp
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
                + //"     Le 30/11/2022 at 19:47, PPAC a dit&nbsp;:\n" 
                +this.alImgsUrl.size() + " image(s) du commentaire de " + this.commentAuteurNom + "&nbsp;:\n"
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

        Document docOrig = Jsoup.parse(getCommentCorpHTMLBrut());

        Element resDivQuote = docRes.selectFirst("div.ipsQuote_contents");
        if (resDivQuote != null) {
            //selectFirst.appendChildren(docOrig.select("a.ipsAttachLink_image"));
            if (false) {
                // étrange il arrive que le lien ne soit pas de cette class ...
                // donc on ne fait plus cela
                Elements selectOriImgs = docOrig.select("a.ipsAttachLink_image");
                if (selectOriImgs != null) {
                    for (Element e : selectOriImgs) {
                        e.attr("style",
                                //        "width:auto;height:200px;"
                                "max-width:150px;max-height:150px;width: auto;height: auto;"
                        );
                        resDivQuote.appendChild(e);
                        resDivQuote.appendText(" ");
                    }
                }
            } else {
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
                            e.attr("style",
                                    //        "width:auto;height:200px;"
                                    "max-width:200px;max-height:150px;width:auto;height:auto;"
                            );
                            // TODO a revoir pour éviter de reprendre un lien malvélliant
                            // donc uniquement si le lien cible le forum !
                            if (imgParent.is("a")
                                    && imgParent.attr("href").startsWith("https://www.lesimprimantes3d.fr/")) {

                                resDivQuote.appendChild(imgParent);
                            } else {
                                // TODO ? là vérifier si image hébergé par le forum ou non                                                        
                                // et si pas une image du forum alors mettre une bordure en rouge ou un logo de "warnig external img" ...

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
                    //e.attr("style", "style=\"width:auto;height:200px;\"");
                    e.attr("style", "max-width:150px;max-height:150px;width: auto;height: auto;");
                }
            }
            // docOrig.select("a.ipsAttachLink_image");
        }

        return docRes.body().html();
    }

}
