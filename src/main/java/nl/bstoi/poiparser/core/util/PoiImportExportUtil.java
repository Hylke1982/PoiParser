package nl.bstoi.poiparser.core.util;

import org.apache.commons.io.IOUtils;
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
            IOUtils.closeQuietly(inputStream);
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

}
