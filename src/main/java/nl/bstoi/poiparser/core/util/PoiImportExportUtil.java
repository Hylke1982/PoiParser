package nl.bstoi.poiparser.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Usefull class for reading certain excel properties and values
 * </p>
 *
 * @author Hylke Stapersma
 */
public class PoiImportExportUtil {

    private static final Log log = LogFactory.getLog(PoiImportExportUtil.class);

    /**
     * <p>
     * Get sheet name from excel file
     * </p>
     *
     * @param excelFile excel file to read
     * @return a list of sheet names
     */
    public static List<String> getSheetNames(final File excelFile) {
        try {
            log.debug("Loading sheet names from excel file:"
                    + excelFile.getName());
            return getSheetNames(new FileInputStream(excelFile));
        } catch (FileNotFoundException e) {
            log.error("File: " + excelFile.getName() + " cannot be found.");
        }
        return null;
    }


    public static List<String> getSheetNames(final InputStream inputStream) {
        POIFSFileSystem poifs = null;
        try {
            poifs = new POIFSFileSystem(inputStream);
            HSSFWorkbook workbook = new HSSFWorkbook(poifs);
            List<String> sheetList = new ArrayList<String>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                log.trace("Adding sheet name: " + workbook.getSheetName(i));
                sheetList.add(workbook.getSheetName(i));
            }
            inputStream.close();
            return sheetList;
        } catch (IOException e) {
            log.error("Error while reading excel file");
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Error while closing input stream");
                }
            }
        }
        return null;
    }

    /**
     * <p>
     * Get excel files from a certain path
     * </p>
     *
     * @param path list excel files on specific path
     * @return list of file names on a path
     */
    public static List<String> readExcelFiles(String path) {
        log.debug("Listing excel files from path: " + path);
        File directoryPath = new File(path);
        List<String> fileList = new ArrayList<String>();
        if (directoryPath.exists() && directoryPath.isDirectory()) {
            for (File file : directoryPath.listFiles()) {
                fileList.add(file.getName());
            }
        }
        return fileList;
    }

    /**
     * <p>
     * Get the file hash for a excel file
     * </p>
     *
     * @param excelFile the excel to get a hash from
     * @return the hash
     */
    public static String getFileHash(final File excelFile) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            FileInputStream fis = new FileInputStream(excelFile);

            byte[] dataBytes = new byte[1024];

            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            ;
            byte[] mdbytes = md.digest();

            // convert the byte to hex format method 2
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
                hexString.append(String.format("%02x", mdbytes[i]));
            }

            log.debug("Hash for file [" + excelFile.getName() + "] is: " + hexString.toString());
            fis.close();
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("Hashing algorithm cannot be found.");
        } catch (IOException e) {
            log.error("Error while reading file.");
        }
        return null;
    }

    public static String getByteArrayHash(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dataBytes = new byte[1024];

            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

            int nread = 0;
            while ((nread = bis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            ;

            byte[] mdbytes = md.digest();

            // convert the byte to hex format method 2
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
                hexString.append(String.format("%02x", mdbytes[i]));
            }

            bis.close();
            return hexString.toString();


        } catch (NoSuchAlgorithmException e) {
            log.error("Hashing algorithm cannot be found.");
        } catch (IOException e) {
            log.error("Error while reading file.");
        }
        return null;
    }

}
