package uk.ac.ebi.pride.prider.ontology.reader;

import java.util.List;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public interface OntologyMapReader {
    public int numTerms();
    public String getAccession(int index);
    public String getName(int index);
    public List<String> getAscendants(int index);
}
