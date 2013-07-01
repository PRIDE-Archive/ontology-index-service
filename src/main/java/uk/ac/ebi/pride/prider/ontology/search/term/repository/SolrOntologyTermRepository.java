package uk.ac.ebi.pride.prider.ontology.search.term.repository;

import org.springframework.data.solr.repository.SolrCrudRepository;
import uk.ac.ebi.pride.prider.ontology.search.term.OntologyTerm;

import java.util.List;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public interface SolrOntologyTermRepository extends SolrCrudRepository<OntologyTerm, String> {
    List<OntologyTerm> findAllByRelatedAccessions(String relatedAccession);
    List<OntologyTerm> findAllByValue(String value);
    List<OntologyTerm> findAllByLabel(String label);
}
