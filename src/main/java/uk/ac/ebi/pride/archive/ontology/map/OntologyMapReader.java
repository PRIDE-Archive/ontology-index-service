package uk.ac.ebi.pride.archive.ontology.map;

import java.util.Set;

public interface OntologyMapReader {
  public int numTerms(int page);

  public String getAccession(int page, int index);

  public String getName(int page, int index);

  public Set<String> getAscendants(int page, int index);
}
