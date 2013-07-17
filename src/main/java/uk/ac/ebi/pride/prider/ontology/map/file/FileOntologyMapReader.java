package uk.ac.ebi.pride.prider.ontology.map.file;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import uk.ac.ebi.pride.prider.ontology.map.OntologyMapReader;

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
    public int numTerms() {
        return this.workBook.getSheetAt(0).getPhysicalNumberOfRows();
    }

    @Override
    public String getAccession(int index) {
        HSSFSheet sheet = this.workBook.getSheetAt(0);
        HSSFRow row = sheet.getRow(index);
        HSSFCell cell = row.getCell(FileColumnIndex.ACCESSION_COLUMN_INDEX.getIndex());

        return cell.getStringCellValue();
    }

    @Override
    public String getName(int index) {
        HSSFSheet sheet = this.workBook.getSheetAt(0);
        HSSFRow row = sheet.getRow(index);
        HSSFCell cell = row.getCell(FileColumnIndex.NAME_COLUMN_INDEX.getIndex());

        return cell.getStringCellValue();
    }

    @Override
    public Set<String> getAscendants(int index) {
        HSSFSheet sheet = this.workBook.getSheetAt(0);
        HSSFRow row = sheet.getRow(index);
        int cells = row.getPhysicalNumberOfCells();
        Set<String> res = new TreeSet<String>();
        for (int i = 2; i < cells; i++) {
            res.add(row.getCell(i).getStringCellValue());
        }
        return res;
    }
}