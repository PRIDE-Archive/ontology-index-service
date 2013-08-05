package uk.ac.ebi.pride.prider.ontology.map;

import java.io.IOException;
import java.util.Set;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public interface OntologyMapWriter {
    public void setAccession(int page, int index, String accession) throws IOException;
    public void setName(int page, int index, String name) throws IOException;
    public void setAscendants(int page, int index, Set<String> ascendants) throws IOException;
}
