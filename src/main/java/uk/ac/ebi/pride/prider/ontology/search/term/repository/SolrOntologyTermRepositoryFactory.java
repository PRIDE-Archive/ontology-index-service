package uk.ac.ebi.pride.prider.ontology.search.term.repository;

import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.repository.support.SolrRepositoryFactory;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class SolrOntologyTermRepositoryFactory {

    private org.springframework.data.solr.core.SolrOperations solrOperations;

    public SolrOntologyTermRepositoryFactory(SolrOperations solrOperations) {
        this.solrOperations = solrOperations;
    }

    public SolrOntologyTermRepository create() {
        return new SolrRepositoryFactory(this.solrOperations).getRepository(SolrOntologyTermRepository.class);
    }

}