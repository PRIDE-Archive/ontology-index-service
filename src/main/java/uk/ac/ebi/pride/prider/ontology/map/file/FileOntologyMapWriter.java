package uk.ac.ebi.pride.prider.ontology.map.file;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import uk.ac.ebi.pride.prider.ontology.map.OntologyMapWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class FileOntologyMapWriter implements OntologyMapWriter {

    private final File mappingsFile;
    private HSSFWorkbook workBook;

    public FileOntologyMapWriter(File mappingsFile) throws IOException {
        this.mappingsFile = mappingsFile;
        this.workBook = new HSSFWorkbook();
//        HSSFSheet sheet = this.workBook.createSheet();
        writeToFile();
    }

    @Override
    public void setAccession(int page, int index, String accession) throws IOException {

        while (this.workBook.getNumberOfSheets()<= page)
            this.workBook.createSheet();

        HSSFSheet sheet = this.workBook.getSheetAt(page);

        HSSFRow row = sheet.getRow(index);
        if (row == null) {
            row = sheet.createRow(index);
        }
        HSSFCell cell = row.getCell(FileColumnIndex.ACCESSION_COLUMN_INDEX.getIndex());
        if (cell == null) {
            cell = row.createCell(FileColumnIndex.ACCESSION_COLUMN_INDEX.getIndex());
        }
        cell.setCellValue(accession);
        writeToFile();
    }

    private void writeToFile() throws IOException {
        FileOutputStream fileOut = new FileOutputStream(mappingsFile);
        workBook.write(fileOut);
        fileOut.close();
    }

    @Override
    public void setName(int page, int index, String name) throws IOException {
        while (this.workBook.getNumberOfSheets()<= page)
            this.workBook.createSheet();


        HSSFSheet sheet = this.workBook.getSheetAt(page);
        HSSFRow row = sheet.getRow(index);
        if (row == null) {
            row = sheet.createRow(index);
        }
        HSSFCell cell = row.getCell(FileColumnIndex.NAME_COLUMN_INDEX.getIndex());
        if (cell == null) {
            cell = row.createCell(FileColumnIndex.NAME_COLUMN_INDEX.getIndex());
        }
        cell.setCellValue(name);
        writeToFile();
    }

    @Override
    public void setAscendants(int page, int index, Set<String> ascendants) throws IOException {
        while (this.workBook.getNumberOfSheets()<= page)
            this.workBook.createSheet();

        HSSFSheet sheet = this.workBook.getSheetAt(page);
        HSSFRow row = sheet.getRow(index);
        if (row == null) {
            row = sheet.createRow(index);
        }
        int i=2;
        for (String ascendant: ascendants) {
            HSSFCell cell = row.getCell(i);
            if (cell == null) {
                cell = row.createCell(i);
            }
            cell.setCellValue(ascendant);
            i++;
        }
        writeToFile();
    }
}
