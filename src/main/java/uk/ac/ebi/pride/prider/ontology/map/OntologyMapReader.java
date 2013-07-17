package uk.ac.ebi.pride.prider.ontology.map;

import java.util.Set;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public interface OntologyMapReader {
    public int numTerms();
    public String getAccession(int index);
    public String getName(int index);
    public Set<String> getAscendants(int index);
}
