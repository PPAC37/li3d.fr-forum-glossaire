/*
 */
package com.ppac37.EText2022;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        this.sFile = fileToWrite;
        try {
            fwIndexComment = new FileWriter(sFile);
            File fTmp = new File(sFile);
            System.out.printf("open  (size=%10d) \"%s\"\n", fTmp.length(), fTmp.getAbsolutePath());
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

    public void close() {
        if (fwIndexComment != null) {
            try {
                fwIndexComment.close();
            } catch (IOException ex) {
                Logger.getLogger(UtilFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        File fTmp = new File(sFile);
        System.out.printf("close (size=%10d) \"%s\"\n", fTmp.length(), fTmp.getAbsolutePath());
    }

}
