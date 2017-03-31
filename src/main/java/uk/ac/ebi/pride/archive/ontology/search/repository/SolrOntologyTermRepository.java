package uk.ac.ebi.pride.archive.ontology.search.repository;

import org.springframework.data.solr.repository.SolrCrudRepository;
import uk.ac.ebi.pride.archive.ontology.model.OntologyTerm;

import java.util.List;

public interface SolrOntologyTermRepository extends SolrCrudRepository<OntologyTerm, String> {
  List<OntologyTerm> findAllByDescendants(String descendant);
  List<OntologyTerm> findAllByName(String name);
}
