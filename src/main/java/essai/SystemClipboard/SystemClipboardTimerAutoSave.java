/*
 */
package essai.SystemClipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.text.SimpleDateFormat;
import java.util.Locale;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author q6
 */
public class SystemClipboardTimerAutoSave {

    public static void main(String[] args) {
        boolean doSaveAsFile = true;
        
        // TODO une seul instance pour ne pas faire plusieur foix le même taf
        // ? la nouvel demande a l'ancienne de stopper son timer 
        //? vs si la nouvel identifie une autre instance elle alors on ne lance pas la nouvel instance ?
        
        // Pour bien limiter a une instance dans une JRE
        // patterne singleton ?
        // Class final ?
        
        // Pour bien limiter a une instance dans un system / OS
        // Fichier de lock ?
        // Socket et pattern Client Serveur ?
        
        
        String sOutputDir = "./save_SystemClipboard";
        File fOuputDir = new File(sOutputDir);
        System.out.println("# SystemClipboard Watcher - Start " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        
        if ( !fOuputDir.exists()){
            fOuputDir.mkdirs();
            System.out.println("Creating output dir: "+fOuputDir.getPath());
            System.out.println("Creating output dir (abs) : "+fOuputDir.getAbsolutePath());
        }else{
            System.out.println("Using output dir: "+fOuputDir.getPath());
            System.out.println("Using output dir (abs) : "+fOuputDir.getAbsolutePath());
        }
        
        Toolkit defaultToolkit = java.awt.Toolkit.getDefaultToolkit();
        Clipboard systemClipboard = defaultToolkit.getSystemClipboard();
        DataFlavor dfTextHtml = DataFlavor.fragmentHtmlFlavor;
        DataFlavor dfText = DataFlavor.stringFlavor;

        int timerDelay = 1000;
        Timer timer = new Timer(timerDelay, new ActionListener() {
            String lastData = "";

            @Override
            public void actionPerformed(ActionEvent e) {
                

                boolean haveBeenReadAsFlavorTextHtml = false;
                boolean haveBeenReadAsFlavorTextOnly = false;
                if (systemClipboard.isDataFlavorAvailable(dfTextHtml)) {
                    try {
                        Object data = systemClipboard.getData(dfTextHtml);
                        haveBeenReadAsFlavorTextHtml = true;
                        String sData = (String) data;

                        if (lastData.equals(sData)) {
                            //Same
                        } else {
                            //new html
                            lastData = sData;
                            // TODO 
                            
                            if ( false ) {
                                System.out.printf("###\n~~~\n%s\n~~~\n###\n",sData);
                            }
                            
                            // Pour reformater
                            Document parse = Jsoup.parse((String) data);                            
                            parse.outputSettings().prettyPrint(true);
                            parse.outputSettings().outline(true);
                            
                            if ( doSaveAsFile ){
                                String sTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
                                String sTmpFileName = sTimeStamp+" - .html";
                                File tmpFileDest = new File(sOutputDir,sTmpFileName);
                                try(FileWriter fw = new FileWriter(tmpFileDest)){
                                    fw.append(parse.toString());
                                    fw.flush();
                                    fw.close();
                                }
                                System.out.printf("\n~~~\n%s\n~~~\n>%s\n",parse.body().html(),tmpFileDest.getAbsolutePath());
                            }else{
                                System.out.printf("~~~\n%s\n~~~\n",parse.body().toString());
                                // .html ne reformate pas ?
                            }
                         
                            

                        }
                    } catch (UnsupportedFlavorException ex) {
                        System.err.println("err UFE html cb:"+ex.getMessage());
                        //Logger.getLogger(EssaiSystemClipboardTimerAutoSave.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        System.err.println("err IOE html cb:"+ex.getMessage());
                        //Logger.getLogger(EssaiSystemClipboardTimerAutoSave.class.getName()).log(Level.SEVERE, null, ex);
                    }catch (Exception ex) {
                        System.err.println("err E html cb:"+ex.getMessage());
                        //Logger.getLogger(EssaiSystemClipboardTimerAutoSave.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                
                if (!haveBeenReadAsFlavorTextHtml && systemClipboard.isDataFlavorAvailable(dfText) ) {
                    // ficher de sauvegarde .txt ?
                    //TODO 
                    
                    
                    try {
                        Object data = systemClipboard.getData(dfText);
                        haveBeenReadAsFlavorTextOnly = true;
                        String sData = (String) data;
                        haveBeenReadAsFlavorTextOnly = true;

                        if (lastData.equals(sData)) {
                            //Same
                        } else {
                            //new html
                            lastData = sData;
                            // TODO 
                            
                            if ( false ) {
                                System.out.printf("###\n~~~\n%s\n~~~\n###\n",sData);
                            }
                                                        
                            if ( doSaveAsFile ){
                                String sTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
                                String sTmpFileName = sTimeStamp+" - .txt";
                                File tmpFileDest = new File(sOutputDir,sTmpFileName);
                                try(FileWriter fw = new FileWriter(tmpFileDest)){
                                    fw.append(sData);
                                    fw.flush();
                                    fw.close();
                                }
                                System.out.printf("\n~~~\n%s\n~~~\n>%s\n",sData,tmpFileDest.getAbsolutePath());
                            }else{
                                System.out.printf("~~~\n%s\n~~~\n",sData);
                                // .html ne reformate pas ?
                            }
                         
                            

                        }
                    } catch (UnsupportedFlavorException ex) {
                        System.err.println("err UFE txt cb:"+ex.getMessage());
                        //Logger.getLogger(EssaiSystemClipboardTimerAutoSave.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        System.err.println("err IOE txt cb:"+ex.getMessage());
                        //Logger.getLogger(EssaiSystemClipboardTimerAutoSave.class.getName()).log(Level.SEVERE, null, ex);
                    }catch (Exception ex) {
                        System.err.println("err E txt cb:"+ex.getMessage());
                        //Logger.getLogger(EssaiSystemClipboardTimerAutoSave.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } else{
                    // 
                }
            }

        }
        );
        timer.setRepeats(true);
        timer.start();

        boolean keepRunning = true;
        long mainSleppTimeBeforTerminating = 1000000;                
        while (keepRunning) {
            try {
                sleep(mainSleppTimeBeforTerminating);
            } catch (InterruptedException ex) {
                Logger.getLogger(SystemClipboardTimerAutoSave.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
