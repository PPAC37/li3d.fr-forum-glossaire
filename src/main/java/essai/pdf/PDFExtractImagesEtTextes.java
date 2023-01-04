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
        String sPdfFile = "/home/q6/github/li3d.fr-forum-glossaire-definitions/autre_glossaire/Nom des pièeces - Impression 3D.pdf";

        String outFileBaseName = "./out/Nom des pièeces - Impression 3D.pdf";

        // Loaded using file io Existing PDF Document
        File newFile = new File(sPdfFile);
        try ( PDDocument document = Loader.loadPDF(newFile)) {

            // Inspecte and extract images object in the PDF
            if (false) {
                int compteurPages = 0;
                int compteurImages = 0;
                PDResources ressources;
                Iterator iterateurCles;
                PDImageXObject image;
                //parcourt les pages du PDF
                for (PDPage page : document.getPages()) {
                    //recupere toutes les images de la page en cours
                    ressources = page.getResources();
                    Iterable<COSName> xObjectNames = ressources.getXObjectNames();
                    iterateurCles = xObjectNames.iterator();
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
                        }
                        BufferedImage bim = image.getImage();
                        
                        File fileOutImg = new File(outFileBaseName + "-ImgNum-" + (compteurImages++)
                                + "-" + "PageNum-" + compteurPages + ".jpg");
                        // suffix in filename will be used as the file format
                        // Writing the extracted image to a new file
                        ImageIO.write(bim, "JPEG", fileOutImg);
                        System.out.println(
                                "Image extracted successfully: " + fileOutImg.getPath());
                    }
                    compteurPages++;
                }

                //ne pas oublier de fermer le PDF
            }

            if (true) {
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
                stripper.setAddMoreFormatting(true);
                
                stripper.setParagraphStart("\n<p>");
                stripper.setParagraphEnd("</p>");
                
                stripper.setArticleStart("<article>");
                stripper.setArticleEnd("</article>");
                
                stripper.setPageStart("<page>");
                stripper.setPageEnd("</page>");
                
                
                
                for (int p = 1; p <= document.getNumberOfPages(); ++p) {
                    // Set the page interval to extract. If you don't, then all pages would be extracted.
                    stripper.setStartPage(p);
                    stripper.setEndPage(p);

                    stripper.setShouldSeparateByBeads(true);

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

                    // If the extracted text is empty or gibberish, please try extracting text
                    // with Adobe Reader first before asking for help. Also read the FAQ
                    // on the website: 
                    // https://pdfbox.apache.org/2.0/faq.html#text-extraction
                }
            }

            // Faire un rendue de chaque page et enregistrer le rendue dans un fichier image.
            if (false) {
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
                    BufferedImage bim = pdfRenderer.renderImageWithDPI(pageCounter, 300, ImageType.RGB);

                    // suffix in filename will be used as the file format
                    // Writing the extracted image to a new file
                    ImageIO.write(
                            bim, "JPEG",
                            new File(outFileBaseName + "-" + (pageCounter++) + ".jpg"));
                    System.out.println(
                            "Image has been extracted successfully");
                }
            }
            
            
            if ( true ){
                // https://svn.apache.org/viewvc/pdfbox/trunk/examples/src/main/java/org/apache/pdfbox/examples/pdmodel/ExtractMetadata.java?view=markup
                
            
                PDDocumentCatalog catalog = document.getDocumentCatalog();
                PDMetadata meta = catalog.getMetadata();
                if (meta != null)
                {
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
                }
                else
                {
                    // The pdf doesn't contain any metadata, try to use the
                    // document information instead
                    PDDocumentInformation information = document.getDocumentInformation();
                    if (information != null)
                    {
                        showDocumentInformation(information);
                    }
                }
                
                
                
            }
            // Closing the PDF document
            document.close();
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

    private static void showDocumentInformation(PDDocumentInformation information)
    {
        display("Title:", information.getTitle());
        display("Subject:", information.getSubject());
        display("Author:", information.getAuthor());
        display("Creator:", information.getCreator());
        display("Producer:", information.getProducer());
    }

    private static void listString(String title, List<String> list)
    {
        if (list == null)
        {
            return;
        }
        System.out.println(title);
        for (String string : list)
        {
            System.out.println("  " + string);
        }
    }

    private static void listCalendar(String title, List<Calendar> list)
    {
        if (list == null)
        {
            return;
        }
        System.out.println(title);
        for (Calendar calendar : list)
        {
            System.out.println("  " + format(calendar));
        }
    }

    private static String format(Object o)
    {
        if (o instanceof Calendar)
        {
            Calendar cal = (Calendar) o;
            return DateFormat.getDateInstance().format(cal.getTime());
        }
        else
        {
            return o.toString();
        }
    }

    private static void display(String title, Object value)
    {
        if (value != null)
        {
            System.out.println(title + " " + format(value));
        }
    }

    /**
     * This will print the usage for this program.
     */
    private static void usage()
    {
        //System.err.println("Usage: java " + ExtractMetadata.class.getName() + " <input-pdf>");
    }
            
}
