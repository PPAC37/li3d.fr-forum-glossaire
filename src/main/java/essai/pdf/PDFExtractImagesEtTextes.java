package essai.pdf;

// Extracting Images from a PDF using java
import java.io.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import javax.imageio.ImageIO;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

//import org.apache.xmpbox.XMPMetadata;
//import org.apache.xmpbox.schema.AdobePDFSchema;
//import org.apache.xmpbox.schema.DublinCoreSchema;
//import org.apache.xmpbox.schema.XMPBasicSchema;
//import org.apache.xmpbox.type.BadFieldValueException;
//import org.apache.xmpbox.xml.DomXmpParser;
//import org.apache.xmpbox.xml.XmpParsingException;
class PDFExtractImagesEtTextes {

    public static void main(String[] args) throws Exception {
        String sPdfFile = "/home/q6/github/li3d.fr-forum-glossaire-definitions/autre_glossaire/pdf_GL/pdf.orig/Nom des pièeces - Impression 3D.pdf";
        
        sPdfFile ="/home/q6/li3d.fr TEST APM2/APM2_save_cle_USB_4GB_v2/Photon Mono 2"
                + "/"
                + "Photon Mono 2-FR-V0.0.3.pdf";
        
        sPdfFile = "/home/q6/0 -Test - Creality Ender-3 V3 SE/save_carte_SD_8GB_Ender-3 V3 SE/Ender-3 V3 SE _supplementary files_EN_V1.2/1.3D Printer User Manual"
                + "/"
                + "Ender-3 V3 SE-SM-001-User Manual (EN).pdf"; // KO
        
        sPdfFile = "/home/q6/Téléchargements"
                + "/"
                + "Ender-3 V3 SE.pdf";
        
        sPdfFile = "/home/q6/Téléchargements"
                + "/"
                + "Anycubic_App.pdf";
        /*
        Exception in thread "main" java.lang.ClassCastException: class org.apache.pdfbox.pdmodel.graphics.form.PDTransparencyGroup cannot be cast to class org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject (org.apache.pdfbox.pdmodel.graphics.form.PDTransparencyGroup and org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject are in unnamed module of loader 'app')
	at essai.pdf.PDFExtractImagesEtTextes.main(PDFExtractImagesEtTextes.java:165)
        */
        
        File inputPDFFile = new File(sPdfFile);
         String outFileBaseName= inputPDFFile.getAbsolutePath()+"_pdf_out/";
        
//        String outFileBaseName = "./out/pdf_GL/";
        
//        inputPDFFile = new File("/home/q6/Téléchargements/0 - TEST - Anycubic Photon mono X2 - From Anycubic.com/save_USB_4GB_SAVE_AnycubicPhotonMonoX2/Files_English_Photon Mono X2","Photon Mono X2-English-V0.0.2.pdf");
//        outFileBaseName = "./out/pdf_APMX2/";
//
//        inputPDFFile = new File("/home/q6/Téléchargements/0 - TEST - Anycubic Kobra Neo/AKN microSD 8GB/Files_English_Anycubic Kobra Neo/User Manual"
//                + "/"
//                + "Anycubic Kobra Neo_User Manual_220908_V0.0.3.pdf");
//        outFileBaseName = "./out/Kobra Neo_User Manual_220908_V0.0.3/";
//
//         inputPDFFile = new File("/home/q6/Téléchargements/0 - TEST - Anycubic Kobra Neo/AKN microSD 8GB/Files_English_Anycubic Kobra Neo/User Manual"
//                + "/"
//                + "Anycubic Kobra Neo Assembly Instruction-220906-C.pdf");
//        outFileBaseName = "./out/Anycubic Kobra Neo Assembly Instruction-220906-C/";

        
        String outImgFileBaseName = outFileBaseName + "img/";

        File destDir = new File(outImgFileBaseName);
        destDir.mkdirs();

        try ( FileWriter fw = new FileWriter(outFileBaseName + "README.md")) {
            // Loaded using file io Existing PDF Document
            File newFile = inputPDFFile;//new File(sPdfFile);
            try ( PDDocument document = Loader.loadPDF(newFile)) {

                fw.append("# " + inputPDFFile.getName() + "\n\n");
                fw.append("<!--\n");
                fw.append("pdf.src.url: " + "");
                fw.append("\n");
                fw.append("pdf.src.datetime: " + "");
                fw.append("\n");
                fw.append("pdf.file: " + inputPDFFile.getPath());
                fw.append("\n");
                fw.append("pdf.file.length: " + inputPDFFile.length());
                fw.append("\n");
                fw.append("pdf.file.md5sum: " + "");
                fw.append("\n");

                fw.append("pdf.DocumentId: " + document.getDocumentId());
                fw.append("\n");

                fw.append("pdf.NumberOfPages: " + document.getNumberOfPages());
                fw.append("\n");

                fw.append("pdf.Version: " + document.getVersion());
                fw.append("\n");

                fw.append("-->\n");

                if (true) {
                    // Pour bien identifier la source (local)

                    // Nom du fichier 
                    // Chemin du fichier
                    // Chemin complet du ichier
                    // Taille du fichier 
                    // Hash md5sum et hash git du fichier .pdf
                    // ? date modification du fihier sur le SF local 
                    // ? si fichier pris en ligne ? gdrive url ?
                    // Si il existe un fichier du même nom que le pdf mais avec l'extention ".src" regarder si il y a une ligne "url.source:..." ? ( demande de faire cela a chaque DL ...
                    System.out.println("getDocumentId: " + document.getDocumentId());
                    System.out.println("getNumberOfPages: " + document.getNumberOfPages());
                    System.out.println("getVersion: " + document.getVersion());
                }

                // Inspecte and extract images object in the PDF
                if (true) {
                    int compteurPages = 0;
                    int compteurImages = 0;
                    PDResources ressources;
                    Iterator iterateurCles;
                    PDImageXObject image;

                    //
                    AccessPermission ap = document.getCurrentAccessPermission();
                    if (!ap.canExtractContent()) {
                        throw new IOException("You do not have permission to extract text");
                    }

                    PDFTextStripper stripper = new PDFTextStripper();

                    stripper.setSortByPosition(false);
                    stripper.setShouldSeparateByBeads(true);
                    stripper.setAddMoreFormatting(true);

                    fw.append("<!--\n");
                    fw.append("pdfTextStripper.SortByPosition: " + stripper.getSortByPosition());
                    fw.append("\n");
                    fw.append("pdfTextStripper.ShouldSeparateByBeads: " + stripper.getSeparateByBeads());
                    fw.append("\n");
                    fw.append("pdfTextStripper.AddMoreFormatting: " + stripper.getAddMoreFormatting());
                    fw.append("\n");

                    fw.append("-->\n");

                    //parcourt les pages du PDF
                    for (PDPage page : document.getPages()) {
                        //recupere toutes les images de la page en cours
                        ressources = page.getResources();
                        Iterable<COSName> xObjectNames = ressources.getXObjectNames();
                        iterateurCles = xObjectNames.iterator();

                        fw.append("## page " + (compteurPages + 1) + "\n\n");

                        //extrait chaque image dans le dossier d'extraction
                        while (iterateurCles.hasNext()) {
                            System.out.println("...");
                            COSName cle = (COSName) iterateurCles.next();
                            image = (PDImageXObject) ressources.getXObject(cle);
                            if (true) {
                                System.out.println("img.getStructParent: " + image.getStructParent());
                                System.out.println("img.getSuffix: " + image.getSuffix());
                                System.out.println("img.getHeight: " + image.getHeight());
                                System.out.println("img.getWidth: " + image.getWidth());

                                PDMetadata metadata = image.getMetadata();
                                if (metadata != null) {
                                    System.out.println("img.getMetadata: " + metadata);
                                }
                                System.out.println("img.getInterpolate: " + image.getInterpolate());
                                System.out.println("img.getCOSObject.getLength: " + image.getCOSObject().getLength());

                                fw.append("<!--\n");
                                if(false){
                                fw.append("img.getStructParent: " + image.getStructParent());
                                fw.append("\n");
                                }
                                fw.append("img.getSuffix: " + image.getSuffix());
                                fw.append("\n");
                                fw.append("img.getHeight: " + image.getHeight());
                                fw.append("\n");
                                fw.append("img.getWidth: " + image.getWidth());
                                fw.append("\n");
                                if (metadata != null) {
                                    System.out.println("img.getMetadata: " + metadata);
                                    fw.append("img.getMetadata: " + metadata);
                                    fw.append("\n");
                                }
                                fw.append("img.getInterpolate: " + image.getInterpolate());
                                fw.append("\n");
                                fw.append("img.getCOSObject.getLength: " + image.getCOSObject().getLength());
                                fw.append("\n");
                                fw.append("-->\n");
                            } else {
                                System.out.printf("%s %dx%d\n",
                                        image.getSuffix(),
                                        image.getHeight(),
                                        image.getWidth());
                            }

                            //                            BufferedImage bim = image.getImage(); // Pb avec les image avec un fond transparent
                            //
                            BufferedImage bim = image.getOpaqueImage(); // ok mais fond noir sur les image avec un fond transparent
                            //?bim.getTransparency();

                            File fileOutImg = new File(outImgFileBaseName + "-ImgNum-" + (compteurImages)
                                    + "-" + "PageNum-" + compteurPages
                                    + //        ".jpg"
                                    "."+image.getSuffix()
                            );

                            fw.append(String.format("![%s-%dx%d](%s)\n", image.getSuffix(),
                                    image.getHeight(),
                                    image.getWidth(),
                                    "img/" + fileOutImg.getName()//TODO a revoir relatif aux path du fichier readme.md
                            ));

                            // suffix in filename will be used as the file format
                            // Writing the extracted image to a new file
                            //ImageIO.write(bim, "JPEG", fileOutImg);
                            ImageIO.write(bim, image.getSuffix(), fileOutImg);
                            System.out.println(
                                    "Image extracted successfully: " + fileOutImg.getPath());

                            fw.append("<!--\n");
                            fw.append("img.save.file: " + fileOutImg.getPath());
                            fw.append("\n");
                            fw.append("img.save.file.length: " + fileOutImg.length());
                            fw.append("\n");
                            fw.append("-->\n");

                            fw.append("\n");
                            compteurImages++;
                        }

                        compteurPages++;
                        // Set the page interval to extract. If you don't, then all pages would be extracted.
                        stripper.setStartPage(compteurPages);
                        stripper.setEndPage(compteurPages);
                        String text = stripper.getText(document);
                        // do some nice output with a header
                        String pageStr = String.format("page %d:", compteurPages);
                        System.out.println("page " + pageStr + ": \n" + text);
                        fw.append("\n\n" + text + "\n");

                    }

                    //ne pas oublier de fermer le PDF
                }

                if (false) {
                    AccessPermission ap = document.getCurrentAccessPermission();
                    if (!ap.canExtractContent()) {
                        throw new IOException("You do not have permission to extract text");
                    }

                    PDFTextStripper stripper = new PDFTextStripper();

                    // This example uses sorting, but in some cases it is more useful to switch it off,
                    // e.g. in some files with columns where the PDF content stream respects the
                    // column order.
                    //stripper.setSortByPosition(true);
                    stripper.setSortByPosition(false);

                    if (false) {
                        stripper.setAddMoreFormatting(true);
                        stripper.setShouldSeparateByBeads(true);
                    }

                    stripper.setParagraphStart("<p>\n");
                    stripper.setParagraphEnd("</p>\n");

                    if (false) {
                        // il ne semble pas y avoir d'article ?

                        stripper.setArticleStart("<article>");
                        stripper.setArticleEnd("</article>");
                    }

                    if (false) {
                        // il ne semble y avoir que le PageStart en sortie.
                        stripper.setPageStart("<page>");
                        stripper.setPageEnd("</page>");
                    }

                    for (int p = 1; p <= document.getNumberOfPages(); ++p) {
                        // Set the page interval to extract. If you don't, then all pages would be extracted.
                        stripper.setStartPage(p);
                        stripper.setEndPage(p);

                        // let the magic happen
                        String text = stripper.getText(document);

                        // do some nice output with a header
                        String pageStr = String.format("page %d:", p);
                        System.out.println(pageStr);
                        for (int i = 0; i < pageStr.length(); ++i) {
                            System.out.print("-");
                        }
                        System.out.println();
                        //System.out.println(text.trim());
                        System.out.println("[" + text + "]");
                        System.out.println();

                        fw.append("\n");
                        fw.append(pageStr);
                        fw.append("\n");
                        fw.append("\n");

                        fw.append(text);
                        //

                        // If the extracted text is empty or gibberish, please try extracting text
                        // with Adobe Reader first before asking for help. Also read the FAQ
                        // on the website: 
                        // https://pdfbox.apache.org/2.0/faq.html#text-extraction
                    }
                }

                // Faire un rendue de chaque page et enregistrer le rendue dans un fichier image.
                if (true) {
                    //https://www.geeksforgeeks.org/java-program-to-extract-a-image-from-a-pdf/?ref=rp
                    // PDFRenderer class to be Instantiated
                    // i.e. creating it's object
                    PDFRenderer pdfRenderer
                            = new PDFRenderer(document);

                    int pageCounter = 0;

                    if (false) {
                        // Rendering an image
                        // from the PDF document
                        // using BufferedImage class
                        BufferedImage img = pdfRenderer.renderImage(0);
                        // Writing the extracted
                        // image to a new file
                        ImageIO.write(
                                img, "JPEG",
                                new File("C:/Documents/GeeksforGeeks.png"));
                        System.out.println(
                                "Image has been extracted successfully");
                    }

                    for (PDPage page : document.getPages()) {
                        //....

                        // note that the page number parameter is zero based
                        BufferedImage bim = pdfRenderer.renderImageWithDPI(pageCounter, 
                                600,//300,
                                ImageType.RGB);
                        File file = new File(outFileBaseName + "vue/page-" + (++pageCounter) + ".jpg");
                        file.getParentFile().mkdirs();

                        // suffix in filename will be used as the file format
                        // Writing the extracted image to a new file
                        ImageIO.write(bim, "JPEG", file);
                        System.out.println(
                                "Image created: "+file.getPath());
                    }
                }

                if (true) {
                    // https://svn.apache.org/viewvc/pdfbox/trunk/examples/src/main/java/org/apache/pdfbox/examples/pdmodel/ExtractMetadata.java?view=markup

                    PDDocumentCatalog catalog = document.getDocumentCatalog();
                    PDMetadata meta = catalog.getMetadata();
                    if (meta != null) {
                        //                    DomXmpParser xmpParser = new DomXmpParser();
                        //                    try
                        //                    {
                        //                        XMPMetadata metadata = xmpParser.parse(meta.toByteArray());
                        //
                        //                        showDublinCoreSchema(metadata);
                        //                        showAdobePDFSchema(metadata);
                        //                        showXMPBasicSchema(metadata);
                        //                    }
                        //                    catch (XmpParsingException e)
                        //                    {
                        //                        System.err.println("An error occurred when parsing the metadata: "
                        //                                + e.getMessage());
                        //                    }
                    } else {
                        // The pdf doesn't contain any metadata, try to use the
                        // document information instead
                        PDDocumentInformation information = document.getDocumentInformation();
                        if (information != null) {
                            showDocumentInformation(information);
                        }
                    }

                }
                // Closing the PDF document
                document.close();
            }
            fw.flush();
            fw.close();
        }

    }

    /*         

        private static void showXMPBasicSchema(XMPMetadata metadata)
        {
            XMPBasicSchema basic = metadata.getXMPBasicSchema();
            if (basic != null)
            {
                display("Create Date:", basic.getCreateDate());
                display("Modify Date:", basic.getModifyDate());
                display("Creator Tool:", basic.getCreatorTool());
            }
        }
        private static void showAdobePDFSchema(XMPMetadata metadata)
        {
            AdobePDFSchema pdf = metadata.getAdobePDFSchema();
            if (pdf != null)
            {
                display("Keywords:", pdf.getKeywords());
                display("PDF Version:", pdf.getPDFVersion());
                display("PDF Producer:", pdf.getProducer());
            }
        }

        private static void showDublinCoreSchema(XMPMetadata metadata) throws BadFieldValueException
        {
            DublinCoreSchema dc = metadata.getDublinCoreSchema();
            if (dc != null)
            {
                display("Title:", dc.getTitle());
                display("Description:", dc.getDescription());
                listString("Creators: ", dc.getCreators());
                listCalendar("Dates:", dc.getDates());
                listString("Subjects:", dc.getSubjects());
            }
        }
     */
    private static void showDocumentInformation(PDDocumentInformation information) {
        display("Title:", information.getTitle());
        display("Subject:", information.getSubject());
        display("Author:", information.getAuthor());
        display("Creator:", information.getCreator());
        display("Producer:", information.getProducer());
    }

    private static void listString(String title, List<String> list) {
        if (list == null) {
            return;
        }
        System.out.println(title);
        for (String string : list) {
            System.out.println("  " + string);
        }
    }

    private static void listCalendar(String title, List<Calendar> list) {
        if (list == null) {
            return;
        }
        System.out.println(title);
        for (Calendar calendar : list) {
            System.out.println("  " + format(calendar));
        }
    }

    private static String format(Object o) {
        if (o instanceof Calendar) {
            Calendar cal = (Calendar) o;
            return DateFormat.getDateInstance().format(cal.getTime());
        } else {
            return o.toString();
        }
    }

    private static void display(String title, Object value) {
        if (value != null) {
            System.out.println(title + " " + format(value));
        }
    }

    /**
     * This will print the usage for this program.
     */
    private static void usage() {
        //System.err.println("Usage: java " + ExtractMetadata.class.getName() + " <input-pdf>");
    }

}
