/*
 */
package com.ppac37.EText2022;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author q6
 */
public class UtilFileWriter {
    
    private FileWriter fwIndexComment = null;
    protected String sFile = "tmp.txt";

    public UtilFileWriter(String fileToWrite) {
        this.sFile=fileToWrite;
                
                try {
            //FileWriter 
            fwIndexComment = new FileWriter(sFile);
        } catch (IOException ex) {
            Logger.getLogger(UtilFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void append(CharSequence csq) {
        //return fwIndexComment.append(csq);
         if (fwIndexComment != null) {
                try {
                    fwIndexComment.append(csq);
                } catch (IOException ex) {
                    Logger.getLogger(UtilFileWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }

    public void flush() {
         if (fwIndexComment != null) {
            try {
                fwIndexComment.flush();
            } catch (IOException ex) {
                Logger.getLogger(UtilFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void close()  {
         if (fwIndexComment != null) {
            try {
                fwIndexComment.close();
            } catch (IOException ex) {
                Logger.getLogger(UtilFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
}
