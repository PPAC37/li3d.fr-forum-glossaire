package essai.pdf;

// Extracting Images from a PDF using java
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

class GFG {

    public static void main(String[] args) throws Exception {
        String sPdfFile = "yourfile.pdf";
        String pdfFilename = sPdfFile;

        sPdfFile = "/home/q6/github/li3d.fr-forum-glossaire-definitions/autre_glossaire/Nom des pièeces - Impression 3D.pdf";

        // Existing PDF Document
        // to be Loaded using file io
        File newFile = new File(sPdfFile);

        try ( PDDocument document = Loader.loadPDF(newFile)) {

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
                    System.out.println("["+text+"]");
                    System.out.println();

                    // If the extracted text is empty or gibberish, please try extracting text
                    // with Adobe Reader first before asking for help. Also read the FAQ
                    // on the website: 
                    // https://pdfbox.apache.org/2.0/faq.html#text-extraction
                }
            }

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
                    //ImageIOUtil.writeImage(bim, pdfFilename + "-" + (pageCounter++) + ".png", 300);
                    // Writing the extracted
                    // image to a new file
                    ImageIO.write(
                            bim, "JPEG",
                            new File(pdfFilename + "-" + (pageCounter++) + ".jpg"));
                    System.out.println(
                            "Image has been extracted successfully");
                }
            }
            // Closing the PDF document
            document.close();
        }

    }
}
