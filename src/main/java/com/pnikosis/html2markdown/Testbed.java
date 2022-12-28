package com.pnikosis.html2markdown;

import static com.pnikosis.html2markdown.HTML2Md.localPathToSaveImg;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Testbed {

    public static void main(String[] args) {
        URL url;
        try {
            //url = new URL("http://jsoup.org/");
            //url = new URL("");
            //url = new URL("https://phrozen3d.com/pages/mini-8k-preorder");
            url = new URL("https://www.lesimprimantes3d.fr/comparateur/imprimante3d/elegoo/jupiter/");
            //url = new URL("https://www.lesimprimantes3d.fr/forum/topic/43711-elegoo-jupiter/");
            //url = new URL("https://www.elegoo.com/products/elegoo-jupiter-12-8-6k-mono-msla-3d-printer?variant=39777858093104");
            // TODO 
            // w_`date`
            //   src_html\ (.html + .log + .cookies(attetion privé ! )
            //   export_version.md
            //   images\
            // créer un repertoire de travail/de destination si il n'existe pas 
            //faire une sauvegarde en local du contenue de la page ( avec date et log de la requete HTTP (header req, cookies ,...)
            // et memo des redirection et réécriture de l'url.
            // Puis lecture de cette sauvegarde (avec passage de l'url (modifié/ des redirections) pour jsoup
            // revoir el nomage du répertoire et/ou du fichier .md ...
            // et pour le fichier de destination .md un repertoire spécifique (?daté) avec le cas echéant un sous répertoire pour les images rapatrié
            // bien revoir la methode de sauvagarde des images ( ne pas ecraser des image de même nom mais de source distinte) et ne pas recharger une même image.
            // faire un fichier qui contien les url des sources de image et le renomage eventuel ... + log des telechargement des images
            // ? 001.png ...
            
            // Un repertoire general où l'on va faire l'ensemble des exports 
            String sLocalBaseDir = "md_export";
            File fLocalBaseDir = new File(sLocalBaseDir);
            fLocalBaseDir.mkdirs();

            
            // Pour avoir le nom de de la page ( du fichier de la page ... )
            
            Path p = Path.of(url.toURI().getPath()); 
            String sPageName = p.getFileName().toString();
            // TODO a revoir ... plutot partire de l'url complete que l'on normalise pour le nom du repertoire 
            sPageName = url.toString().replaceAll("/", "-").replaceAll("[^0-9a-zA-Z-.]", "_");
            // Un sous repertoire pour l'export de cette page
            File fLocalDestDir = new File(fLocalBaseDir,sPageName);
            fLocalDestDir.mkdirs();
            
            // Un sous/sous repertoire "image" pour l'export des images de cette page
            File fImageDestDir = new File(fLocalDestDir, "images");
            fImageDestDir.mkdirs();
            
            // Là c'est pas du propre car attention si on fait du multi thread cela va bugger
            // Pour bien specifier lors de l'export le chemin de sauvagarde des images
            HTML2Md.localPathToSaveImg = fImageDestDir.getPath();
            // Le chemin du fichier .md pour l'export
            String sMarkDownFileDest = sPageName + ".test.md";
            final File file = new File(fLocalDestDir, sMarkDownFileDest);
            // Là encore pas du propre mais on a besoins plus tard d'avoir les chemins relatif des images par rapport au chemin d'export du fichier .md
            HTML2Md.mdFilePath = fLocalDestDir.toPath();
            // Et enfin on fait la lecture du contenue de l'url et l'export au format markdown
            String parsedText = HTML2Md.convert(url, 30000);
            System.out.println(parsedText);
            // Ecriture de lexport au format markdown dans un fichier 
            FileWriter fw = new FileWriter(file);
            // Pour avoir une trace de l'url source dans le fichier d'export
            fw.write(String.format("<!--\n%s\n%s\n-->\n", url.toExternalForm(), new Date().toString()));
            fw.write(parsedText);
            fw.close();

            // test parse local html file
//            String pathFile = "test.html";
//            File f = new File(pathFile);
//            String parsedFileText = HTML2Md.convertFile(f, "gbk");
//            System.out.println(parsedFileText);
            System.out.println("done");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException ex) {
            Logger.getLogger(Testbed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
