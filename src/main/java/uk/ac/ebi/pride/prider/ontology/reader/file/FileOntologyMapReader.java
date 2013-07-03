package uk.ac.ebi.pride.prider.ontology.reader.file;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import uk.ac.ebi.pride.prider.ontology.reader.OntologyMapReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class FileOntologyMapReader implements OntologyMapReader {

    private static final int NAME_COLUMN_INDEX = 1;
    private static final int ACCESSION_COLUMN_INDEX = 0;
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
        HSSFCell cell = row.getCell(ACCESSION_COLUMN_INDEX);

        return cell.getStringCellValue();
    }

    @Override
    public String getName(int index) {
        HSSFSheet sheet = this.workBook.getSheetAt(0);
        HSSFRow row = sheet.getRow(index);
        HSSFCell cell = row.getCell(NAME_COLUMN_INDEX);

        return cell.getStringCellValue();
    }

    @Override
    public List<String> getRelatives(int index) {
        HSSFSheet sheet = this.workBook.getSheetAt(0);
        HSSFRow row = sheet.getRow(index);
        int cells = row.getPhysicalNumberOfCells();
        List<String> res = new LinkedList<String>();
        for (int i = 2; i < cells; i++) {
            res.add(row.getCell(i).getStringCellValue());
        }
        return res;
    }
}
