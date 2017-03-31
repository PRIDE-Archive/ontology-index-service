package uk.ac.ebi.pride.archive.ontology.search.repository;

import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.repository.support.SolrRepositoryFactory;


public class SolrOntologyTermRepositoryFactory {

  private org.springframework.data.solr.core.SolrOperations solrOperations;

  public SolrOntologyTermRepositoryFactory(SolrOperations solrOperations) {
    this.solrOperations = solrOperations;
  }

  public SolrOntologyTermRepository create() {
    return new SolrRepositoryFactory(this.solrOperations).getRepository(SolrOntologyTermRepository.class);
  }
}