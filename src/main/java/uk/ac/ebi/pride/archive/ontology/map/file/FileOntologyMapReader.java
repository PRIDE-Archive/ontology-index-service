package uk.ac.ebi.pride.archive.ontology.map.file;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import uk.ac.ebi.pride.archive.ontology.map.OntologyMapReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class FileOntologyMapReader implements OntologyMapReader {

    private HSSFWorkbook workBook;

    public FileOntologyMapReader(File mappingsFile) throws IOException {
        this.workBook = new HSSFWorkbook(new FileInputStream(mappingsFile));
    }


    @Override
    public int numTerms(int page) {
        return this.workBook.getSheetAt(page).getPhysicalNumberOfRows();
    }

    @Override
    public String getAccession(int page, int index) {
        HSSFSheet sheet = this.workBook.getSheetAt(page);
        HSSFRow row = sheet.getRow(index);
        HSSFCell cell = row.getCell(FileColumnIndex.ACCESSION_COLUMN_INDEX.getIndex());

        return getStringCellValue(cell);
    }

    @Override
    public String getName(int page, int index) {
        HSSFSheet sheet = this.workBook.getSheetAt(page);
        HSSFRow row = sheet.getRow(index);
        HSSFCell cell = row.getCell(FileColumnIndex.NAME_COLUMN_INDEX.getIndex());

        return cell.getStringCellValue();
    }

    @Override
    public Set<String> getAscendants(int page, int index) {
        HSSFSheet sheet = this.workBook.getSheetAt(page);
        HSSFRow row = sheet.getRow(index);
        int cells = row.getPhysicalNumberOfCells();
        Set<String> res = new TreeSet<String>();
        for (int i = 2; i < cells; i++) {
            res.add(getStringCellValue(row.getCell(i)));
        }
        return res;
    }

    private String getStringCellValue(HSSFCell cell) {
        if (cell.getCellType() == 0) // it's a number
            return ""+((int)cell.getNumericCellValue());
        else
            return cell.getStringCellValue();
    }
}
