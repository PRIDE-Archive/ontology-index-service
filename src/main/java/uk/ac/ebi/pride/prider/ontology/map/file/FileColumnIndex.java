package uk.ac.ebi.pride.prider.ontology.map.file;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public enum FileColumnIndex {
    ACCESSION_COLUMN_INDEX(0),
    NAME_COLUMN_INDEX(1)
    ;

    private final int index;

    private FileColumnIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
