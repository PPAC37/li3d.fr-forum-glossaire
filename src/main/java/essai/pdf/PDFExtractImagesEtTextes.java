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
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

class PDFExtractImagesEtTextes {

    public static void main(String[] args) throws Exception {
        String sPdfFile = "/home/q6/github/li3d.fr-forum-glossaire-definitions/autre_glossaire/Nom des pièeces - Impression 3D.pdf";

        String outFileBaseName = "./out/Nom des pièeces - Impression 3D.pdf";

        // Loaded using file io Existing PDF Document
        File newFile = new File(sPdfFile);
        try ( PDDocument document = Loader.loadPDF(newFile)) {

            // Inspecte and extract images object in the PDF
            if (true) {
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
                        BufferedImage bim = image.getImage();
                        File fileOutImg = new File(outFileBaseName + "-page"+compteurPages+"-" + (compteurImages++) + ".jpg");
                        // suffix in filename will be used as the file format
                        // Writing the extracted image to a new file
                        ImageIO.write(bim, "JPEG", fileOutImg);
                        System.out.println(
                                "Image extracted successfully: "+fileOutImg.getPath());
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
            // Closing the PDF document
            document.close();
        }

    }
}
