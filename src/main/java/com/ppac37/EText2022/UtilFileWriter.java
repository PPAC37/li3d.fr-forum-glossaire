/*
 */
package com.ppac37.EText2022;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author q6
 */
public class UtilFileWriter {
    
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UtilFileWriter.class);
    //logger.trace("version: {}", version);

    private FileWriter fwIndexComment = null;

    /**
     *
     */
    protected String sFile = "tmp.txt";

    static boolean debugOpen = false;
    /**
     *
     * @param fileToWrite
     */
    public UtilFileWriter(String fileToWrite) {
        this.sFile = fileToWrite;
        try {
            fwIndexComment = new FileWriter(sFile);
            File fTmp = new File(sFile);
            if ( debugOpen ) System.out.printf("open  (size=%10d) \"%s\"\n", fTmp.length(), fTmp.getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(UtilFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param csq
     */
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

    /**
     *
     */
    public void flush() {
        if (fwIndexComment != null) {
            try {
                fwIndexComment.flush();
            } catch (IOException ex) {
                Logger.getLogger(UtilFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     */
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
        //logger.trace("close: {} {}", fTmp.length(), fTmp.getAbsolutePath());
    }

    public String getsFile() {
        return sFile;
    }

    
}
