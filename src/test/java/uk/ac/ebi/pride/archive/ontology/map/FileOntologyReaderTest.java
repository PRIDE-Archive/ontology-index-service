package uk.ac.ebi.pride.archive.ontology.map;

import org.junit.Test;
import uk.ac.ebi.pride.archive.ontology.map.file.FileOntologyMapReader;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class FileOntologyReaderTest {

    private static final String TEST_ACCESSION_0 = "BTO:0000000";
    private static final String TEST_NAME_0 = "tissues, cell and enzyme sources";
    private static final int NUM_RELATIVES_0 = 0;

    private static final String TEST_ACCESSION_1 = "BTO:0000042";
    private static final String TEST_NAME_1 = "animal";
    private static final int NUM_RELATIVES_1 = 1;

    private static final String TEST_ACCESSION_2 = "BTO:0000089";
    private static final String TEST_NAME_2 = "blood";
    private static final int NUM_RELATIVES_2 = 4;


    @Test
    public void testRead() throws Exception {
        FileOntologyMapReader fileOntologyMapReader = new FileOntologyMapReader(new File("src/test/resources/terms.xls"));

        assertEquals(TEST_ACCESSION_0, fileOntologyMapReader.getAccession(0,0));
        assertEquals(TEST_NAME_0, fileOntologyMapReader.getName(0,0));
        assertEquals(NUM_RELATIVES_0, fileOntologyMapReader.getAscendants(0,0).size());

        assertEquals(TEST_ACCESSION_1, fileOntologyMapReader.getAccession(0,1));
        assertEquals(TEST_NAME_1, fileOntologyMapReader.getName(0,1));
        assertEquals(NUM_RELATIVES_1, fileOntologyMapReader.getAscendants(0,1).size());

        assertEquals(TEST_ACCESSION_2, fileOntologyMapReader.getAccession(0,2));
        assertEquals(TEST_NAME_2, fileOntologyMapReader.getName(0,2));
        assertEquals(NUM_RELATIVES_2, fileOntologyMapReader.getAscendants(0,2).size());
    }
}
