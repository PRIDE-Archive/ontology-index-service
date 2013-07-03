package uk.ac.ebi.pride.prider.ontology.search.repository;

import org.springframework.data.solr.repository.SolrCrudRepository;
import uk.ac.ebi.pride.prider.ontology.model.OntologyTerm;

import java.util.List;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public interface SolrOntologyTermRepository extends SolrCrudRepository<OntologyTerm, String> {
    List<OntologyTerm> findAllByRelatives(String relative);
    List<OntologyTerm> findAllByName(String name);
}
