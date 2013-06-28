package uk.ac.ebi.pride.prider.ontology.search.term.repository;

import org.springframework.data.solr.repository.SolrCrudRepository;
import uk.ac.ebi.pride.prider.ontology.search.term.OntologyTerm;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public interface SolrOntologyTermRepository extends SolrCrudRepository<OntologyTerm, String> {
}
