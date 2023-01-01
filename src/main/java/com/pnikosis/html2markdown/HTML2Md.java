package com.pnikosis.html2markdown;

import com.pnikosis.html2markdown.MDLine.MDLineType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;

/**
 * Convert Html to MarkDown
 */
public class HTML2Md {

    /**
     *
     */
    public static boolean localSaveImg = true;

    /**
     *
     */
    public static String localPathToSaveImg = "./img_save/";

    /**
     *
     */
    public static Path mdFilePath = new File(".").toPath();

    private static int indentation = -1;
    private static boolean orderedList = false;

    /**
     *
     * @param theHTML
     * @param baseURL
     * @return
     */
    public static String convert(String theHTML, String baseURL) {
        Document doc = Jsoup.parse(theHTML, baseURL);

        return parseDocument(doc);
    }

    /**
     *
     * @param url
     * @param timeoutMillis
     * @return
     * @throws IOException
     */
    public static String convert(URL url, int timeoutMillis) throws IOException {
        Document doc = Jsoup.parse(url, timeoutMillis);

        return parseDocument(doc);
    }

    /**
     *
     * @param html
     * @param charset
     * @return
     * @throws IOException
     */
    public static String convertHtml(String html, String charset) throws IOException {
        Document doc = Jsoup.parse(html, charset);

        return parseDocument(doc);
    }

    /**
     *
     * @param file
     * @param charset
     * @return
     * @throws IOException
     */
    public static String convertFile(File file, String charset) throws IOException {
        Document doc = Jsoup.parse(file, charset);

        return parseDocument(doc);
    }

    /**
     *
     * @param htmlPath
     * @param mdPath
     * @param charset
     */
    public static void htmlToJekyllMd(String htmlPath, String mdPath, String charset) {
        try {
            List<File> fileList = FilesUtil.getAllFiles(htmlPath, "html");
            for (File file : fileList) {
                String mdName = file.getAbsolutePath().replace(htmlPath, mdPath).replace("html", "md");
                String hmPath = mdName.substring(0, mdName.lastIndexOf("/")) + "/";
                String separator = System.getProperty("line.separator");
                String head = "---" + separator
                        + "layout: post" + separator
                        + "title: \"" + file.getName() + "\"" + separator
                        + "description: \"" + file.getName() + "\"" + separator
                        + "category: pages\"" + separator
                        + "tags: [blog]\"" + separator
                        + "--- " + separator
                        + "{% include JB/setup %}" + separator
                        + separator;
                FilesUtil.isExist(hmPath);
                String parsedText = convertFile(file, charset);
                Calendar calendar = Calendar.getInstance();
                String dateName = DateUtil.dateToShortString(calendar.getTime());
                String newName = dateName + "-" + hmPath.replace(mdPath, "").replace("/", "-") + "-" + file.getName();
                String mmName = (hmPath + newName.replace("html", "md")).replaceAll("\\s*", "");
                FilesUtil.newFile(mmName, head + parsedText, charset);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param htmlPath
     * @param mdPath
     * @param charset
     */
    public static void htmlToHexoMd(String htmlPath, String mdPath, String charset) {
        try {
            List<File> fileList = FilesUtil.getAllFiles(htmlPath, "html");
            for (File file : fileList) {
                String mdName = file.getAbsolutePath().replace(htmlPath, mdPath).replace("html", "md");
                String hmPath = mdName.substring(0, mdName.lastIndexOf("/")) + "/";
                String separator = System.getProperty("line.separator");
                String[] strings = hmPath.replace(mdPath, "").split("/");
                Calendar calendar = Calendar.getInstance();
                String dateName = DateUtil.dateToShortString(calendar.getTime());
                String dateString = DateUtil.dateToLongString(calendar.getTime());
                StringBuilder blog = new StringBuilder();
                StringBuilder categories = new StringBuilder();
                Map<String, String> stringMap = new TreeMap<String, String>();
                for (String value : strings) {
                    stringMap.put(value, value);
                }
                for (String tag : stringMap.keySet()) {
                    blog.append(" - ").append(tag).append(separator);
                }
                categories.append(strings[0]);
                String head = "---" + separator
                        + "layout: post" + separator
                        + "title: \"" + file.getName().replace(".html", "").split("-")[0] + "\"" + separator
                        + "date: " + dateString + separator
                        + "categories: " + categories + separator
                        + "tags: " + separator
                        + blog.toString()
                        + "--- " + separator
                        + separator;
                FilesUtil.isExist(hmPath);
                String parsedText = HTML2Md.convertFile(file, "utf-8");
                String newName = dateName + "-" + hmPath.replace(mdPath, "").replace("/", "-") + "-" + file.getName();
                String mmName = (hmPath + newName.replace("html", "md")).replaceAll("\\s*", "");
                FilesUtil.newFile(mmName, head + parsedText, charset);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean doDocWhiteList = false;

    private static String parseDocument(Document dirtyDoc) {
        indentation = -1;

        String title = dirtyDoc.title();

        Document doc = dirtyDoc;
        if (doDocWhiteList) {
            Safelist whitelist = Safelist.relaxed();
            Cleaner cleaner = new Cleaner(whitelist);

            //Document 
            doc = cleaner.clean(dirtyDoc);
        }
        doc.outputSettings().escapeMode(EscapeMode.xhtml);

        if (!title.trim().equals("")) {
            return "# " + title + "\n\n" + getTextContent(doc);
        } else {
            return getTextContent(doc);
        }
    }

    //private

    /**
     *
     * @param element
     * @return
     */
    public static String getTextContent(Element element) {
        ArrayList<MDLine> lines = new ArrayList<MDLine>();

        List<Node> children = element.childNodes();
        for (Node child : children) {
            if (child instanceof TextNode) {
                TextNode textNode = (TextNode) child;
                MDLine line = getLastLine(lines);
                if (line.getContent().equals("")) {
                    if (!textNode.isBlank()) {
                        line.append(textNode.text().replaceAll("#", "/#").replaceAll("\\*", "/\\*"));// ???
                    }
                } else {
                    line.append(textNode.text().replaceAll("#", "/#").replaceAll("\\*", "/\\*"));//???
                }

            } else if (child instanceof Element) {
                Element childElement = (Element) child;
                processElement(childElement, lines);
            } else {
                System.out.println();
            }
        }

        int blankLines = 0;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).toString().trim();
            if (line.equals("")) {
                blankLines++;
            } else {
                blankLines = 0;
            }
            if (blankLines < 2) {
                result.append(line);
                if (i < lines.size() - 1) {
                    result.append("\n");
                }
            }
        }

        return result.toString();
    }

    /**
     * Un ajout pour une gestion des tags blockquote / block de citation (qui
     * peuvent etre inbriqué ).
     *
     * TODO revoir pour ne pas avoir une ligne vide au debut et a la fin du
     * blcok de citation cf: "> " ...
     *
     * <p>
     * TODO vérifier que j'ai pas un effet de bord ... car là c'est fait sans
     * trop d'analyse du focntionnement de cette lib.
     *
     * @param element
     * @return
     */
    public static String getTextContentQuote(Element element) {
        ArrayList<MDLine> lines = new ArrayList<MDLine>();

        List<Node> children = element.childNodes();
        for (Node child : children) {
            if (child instanceof TextNode) {
                TextNode textNode = (TextNode) child;
                MDLine line = getLastLine(lines);
                if (line.getContent().equals("")) {
                    if (!textNode.isBlank()) {
                        //line.append(textNode.text().replaceAll("#", "/#").replaceAll("\\*", "/\\*"));
                        line.append(textNode.text().replaceAll("#", "\\#")
                        //       .replaceAll("\\*", "/\\*")
                        );
                    }
                } else {
                    //line.append(textNode.text().replaceAll("#", "/#").replaceAll("\\*", "/\\*"));
                    line.append(textNode.text().replaceAll("#", "\\#")
                    //.replaceAll("\\*", "/\\*")
                    );
                }

            } else if (child instanceof Element) {
                Element childElement = (Element) child;
                processElement(childElement, lines);
            } else {
                System.out.println();
            }
        }

        int blankLines = 0;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).toString().trim();
            if (line.equals("")) {
                blankLines++;
            } else {
                blankLines = 0;
            }
            if (blankLines < 2) {
                result.append("> " + line.replaceAll("\n", "\n> "));
                if (i < lines.size() - 1) {
                    result.append("\n");
                }
            }
        }

        return result.toString();
    }

    /**
     * Alors ... oui ?
     * <p>
     * TODO voir pour garder les tags html des tables et les styles quand il y a
     * des mise en couleur du texte ...
     * <p>
     * TODO voir aussi pour les images quand il y a des taille de définie ( pour
     * garder les taille et donc le tag img plutot que de passer a du format
     * md.)
     *
     * @param element
     * @param lines
     */
    private static void processElement(Element element, ArrayList<MDLine> lines) {
        Tag tag = element.tag();

        String tagName = tag.getName();
        if (tagName.equals("div")) {
            div(element, lines);
        } else if (tagName.equals("p")) {
            p(element, lines);
        } else if (tagName.equals("br")) {
            br(lines);
        } else if (tagName.matches("^h[0-9]+$")) {
            h(element, lines);
        } else if (tagName.equals("strong") || tagName.equals("b")) {
            strong(element, lines);
        } else if (tagName.equals("em")) {
            em(element, lines);
        } else if (tagName.equals("hr")) {
            hr(lines);
        } else if (tagName.equals("a")) {
            a(element, lines);
        } else if (tagName.equals("img")) {
            img(element, lines);
        } else if (tagName.equals("table")) { // ajout PPAC
            lines.add(new MDLine(MDLineType.None, 0, ""));
            MDLine line = getLastLine(lines);
            line.append(element.outerHtml());
            lines.add(new MDLine(MDLineType.None, 0, ""));
            lines.add(new MDLine(MDLineType.None, 0, ""));
        } else if (tagName.equals("iframe")) { // ajout PPAC
            iframe(element, lines);
        } else if (tagName.equals("blockquote")) { // ajout PPAC
            lines.add(new MDLine(MDLineType.None, 0, ""));
            MDLine line = getLastLine(lines);
            line.append(getTextContentQuote(element));
            lines.add(new MDLine(MDLineType.None, 0, ""));
            lines.add(new MDLine(MDLineType.None, 0, ""));
        } else if (tagName.equals("code")) { // ? 
            code(element, lines);
        } else if (tagName.equals("ul")) {
            ul(element, lines);
        } else if (tagName.equals("ol")) {
            ol(element, lines);
        } else if (tagName.equals("li")) {
            li(element, lines);
        } else {
            MDLine line = getLastLine(lines);
            line.append(getTextContent(element));
        }
    }

    private static MDLine getLastLine(ArrayList<MDLine> lines) {
        MDLine line;
        if (lines.size() > 0) {
            line = lines.get(lines.size() - 1);
        } else {
            line = new MDLine(MDLineType.None, 0, "");
            lines.add(line);
        }

        return line;
    }

    private static void div(Element element, ArrayList<MDLine> lines) {
        MDLine line = getLastLine(lines);
        String content = getTextContent(element);
        if (!content.equals("")) {
            if (!line.getContent().trim().equals("")) {
                lines.add(new MDLine(MDLineType.None, 0, ""));
                lines.add(new MDLine(MDLineType.None, 0, content));
                lines.add(new MDLine(MDLineType.None, 0, ""));
            } else {
                if (!content.trim().equals("")) {
//            line.append(content); // PPAC a l'origine il n'y avais que cette ligne !
//            //modif PPAC ? TODO voir comment rendre cela actif ... car là cela ne fonctionne pas !
                    line.append("  ");//permt dans le rendue HTML du .md un retour a la ligne sans casser le block ...
                    lines.add(new MDLine(MDLineType.None, 0, ""));//ppac
                    line = getLastLine(lines);//ppac
                    line.append(content);//deja
                    //lines.add(new MDLine(MDLineType.None, 0, ""));//ppac

                }
            }
        }
    }

    private static void p(Element element, ArrayList<MDLine> lines) {
        MDLine line = getLastLine(lines);
        if (!line.getContent().trim().equals("")) {
            lines.add(new MDLine(MDLineType.None, 0, ""));
        }
        lines.add(new MDLine(MDLineType.None, 0, ""));
        lines.add(new MDLine(MDLineType.None, 0, getTextContent(element)));
        lines.add(new MDLine(MDLineType.None, 0, ""));
        if (!line.getContent().trim().equals("")) {
            lines.add(new MDLine(MDLineType.None, 0, ""));
        }
    }

    private static void br(ArrayList<MDLine> lines) {
        MDLine line = getLastLine(lines);
        if (!line.getContent().trim().equals("")) {
            lines.add(new MDLine(MDLineType.None, 0, ""));
        }
    }

    private static void h(Element element, ArrayList<MDLine> lines) {
        MDLine line = getLastLine(lines);
        if (!line.getContent().trim().equals("")) {
            lines.add(new MDLine(MDLineType.None, 0, ""));
        }

        int level = Integer.valueOf(element.tagName().substring(1));
        switch (level) {
            case 1:
                lines.add(new MDLine(MDLineType.Head1, 0, getTextContent(element)));
                break;
            case 2:
                lines.add(new MDLine(MDLineType.Head2, 0, getTextContent(element)));
                break;
            default:
                lines.add(new MDLine(MDLineType.Head3, 0, getTextContent(element)));
                break;
        }

        lines.add(new MDLine(MDLineType.None, 0, ""));
        lines.add(new MDLine(MDLineType.None, 0, ""));
    }

    private static void strong(Element element, ArrayList<MDLine> lines) {
        MDLine line = getLastLine(lines);
        line.append("**");
        line.append(getTextContent(element));
        line.append("**");
    }

    private static void em(Element element, ArrayList<MDLine> lines) {
        MDLine line = getLastLine(lines);
        line.append("*");
        line.append(getTextContent(element));
        line.append("*");
    }

    private static void hr(ArrayList<MDLine> lines) {
        lines.add(new MDLine(MDLineType.None, 0, ""));
        lines.add(new MDLine(MDLineType.HR, 0, ""));
        lines.add(new MDLine(MDLineType.None, 0, ""));
    }

    private static void a(Element element, ArrayList<MDLine> lines) {
        MDLine line = getLastLine(lines);
        line.append("[");
        line.append(getTextContent(element).replaceFirst("^\n", "").replaceAll("\n\n", "  \n"));
        line.append("]");
        line.append("(");
        //String url = element.attr("href");
        String url = element.attr("abs:href");
        line.append(url);
        String title = element.attr("title");
        if (!title.equals("")) {
            line.append(" \"");
            line.append(title);
            line.append("\"");
        }
        line.append(")");
    }

    private static void img(Element element, ArrayList<MDLine> lines) {
        MDLine line = getLastLine(lines);

        // En fait c'est mis par un script chargé dans la page donc c'est mort ... en static il n'y a que data-src
        //data-srcset
        //srcset
        String imgSrcSet = element.attr("srcset");
        if (imgSrcSet != null && imgSrcSet.length() > 0) {
            String[] srcs = imgSrcSet.split(", *");
            for (String src : srcs) {
                String[] p = src.split(" ");
                line.append("\n![");
                String alt = element.attr("alt");
                line.append(alt);
                line.append(" ");
                if (p.length > 1) {
                    line.append(p[1]);
                } else {
                    line.append(p[0]);
                }//
                line.append("]");
                line.append("(");
                String url = p[0];
                line.append(url);
                String title = element.attr("title");
                if (!title.equals("")) {
                    line.append(" \"");
                    line.append(title);
                    line.append("\"");
                }
                line.append(")");
            }
        }

        if (localSaveImg) {
            String url = element.attr("abs:src");
            String newLocalPath = "";
            if (url.length() == 0) {
                System.err.println(" url : " + element.attr("abs:src"));
                System.err.println(" url from : " + element.outerHtml());
            } else
            try {
                URL imgsrc = new URL(url);
                BufferedImage read = ImageIO.read(imgsrc);

                Path p = Path.of(imgsrc.toURI().getPath());
                File dirDest = new File(localPathToSaveImg);
                dirDest.mkdirs();
                String imageFileName = p.getFileName().toString();
                File dest = new File(localPathToSaveImg, imageFileName);
                //newLocalPath = dest.getPath();
                newLocalPath = mdFilePath.relativize(dest.toPath()).toString();
                String imageFormat = "png";
                imageFormat = imageFileName.substring(imageFileName.lastIndexOf(".") + 1, imageFileName.length());
                // TODO le cas de format d'image svg, webp et si codé en base 64 
                ImageIO.write(read, imageFormat, dest);// TODO revoir pour obtenir le format de ?? une requette a l'url ?? 
            } catch (MalformedURLException ex) {
                System.err.println(" url : " + element.attr("abs:src"));
                Logger.getLogger(HTML2Md.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(HTML2Md.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(HTML2Md.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                System.err.println(" url : " + element.attr("abs:src"));

                System.err.println(" url from : " + element.outerHtml());
                //Logger.getLogger(HTML2Md.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (newLocalPath.length() > 0) {
                line.append("![");
                String alt = element.attr("alt");
                line.append(alt);
                line.append("]");
                line.append("(");
                //String url = element.attr("src");
                line.append(newLocalPath);
                String title = element.attr("title");
                if (!title.equals("")) {
                    line.append(" \"");
                    line.append(title);
                    line.append("\"");
                }
                line.append(")");
            }
        }
        if (!localSaveImg) {
            // A revoir car un commentaire HTML fait bug si dans un lien ...
            if (localSaveImg) {
                // TODO a revoir car si dans un lien cela plante completement avec un interpréteur basic de markdown ...
                // donc plutot faire un fichier avec les urls d'origine ?
                // ou en fin d'export un listing 
                line.append("<!-- ");
            }
            line.append("![");
            String alt = element.attr("alt");
            line.append(alt);
            line.append("]");
            line.append("(");
            String url = element.attr("abs:src");
            line.append(url);
            String title = element.attr("title");
            if (!title.equals("")) {
                line.append(" \"");
                line.append(title);
                line.append("\"");
            }
            line.append(")");
            if (localSaveImg) {
                line.append(" -->");
            }
        }
    }

    //ajout PPAC
    private static void iframe(Element element, ArrayList<MDLine> lines) {

        lines.add(new MDLine(MDLineType.None, 0, ""));
        //lines.add(new MDLine(MDLineType.None, 0, ""));    
        //lines.add(new MDLine(MDLineType.None, 0, element.attr("src")));

        MDLine line = getLastLine(lines);

        line.append("[");
//    String alt = element.attr("alt");
//    line.append(alt);
        String url = element.attr("abs:src");
        if (url.length() == 0) {
            url = element.attr("data-embed-src");
        }
        line.append(url);
        line.append("]");
        line.append("(");
//    String url = element.attr("src");
        line.append(url);
        String title = element.attr("title");
        if (!title.equals("")) {
            line.append(" \"");
            line.append(title);
            line.append("\"");
        }
        line.append(")");
        line.append("<!--");
        line.append(element.outerHtml());
        line.append("-->");
        lines.add(new MDLine(MDLineType.None, 0, ""));
        lines.add(new MDLine(MDLineType.None, 0, ""));
    }

    private static void code(Element element, ArrayList<MDLine> lines) {
        lines.add(new MDLine(MDLineType.None, 0, ""));
        MDLine line = new MDLine(MDLineType.None, 0, "    ");
        line.append(getTextContent(element).replace("\n", "    "));
        lines.add(line);
        lines.add(new MDLine(MDLineType.None, 0, ""));
    }

    private static void ul(Element element, ArrayList<MDLine> lines) {
        lines.add(new MDLine(MDLineType.None, 0, ""));
        indentation++;
        orderedList = false;
        MDLine line = new MDLine(MDLineType.None, 0, "");
        line.append(getTextContent(element));
        lines.add(line);
        indentation--;
        lines.add(new MDLine(MDLineType.None, 0, ""));
    }

    private static void ol(Element element, ArrayList<MDLine> lines) {
        lines.add(new MDLine(MDLineType.None, 0, ""));
        indentation++;
        orderedList = true;
        MDLine line = new MDLine(MDLineType.None, 0, "");
        line.append(getTextContent(element));
        lines.add(line);
        indentation--;
        lines.add(new MDLine(MDLineType.None, 0, ""));
    }

    private static void li(Element element, ArrayList<MDLine> lines) {
        MDLine line;
        if (orderedList) {
            line = new MDLine(MDLineType.Ordered, indentation,
                    getTextContent(element));
        } else {
            line = new MDLine(MDLineType.Unordered, indentation,
                    getTextContent(element));
        }
        lines.add(line);
    }
}
