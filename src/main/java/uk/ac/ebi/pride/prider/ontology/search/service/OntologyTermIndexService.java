package uk.ac.ebi.pride.prider.ontology.search.service;

import org.springframework.stereotype.Service;
import uk.ac.ebi.pride.prider.ontology.model.OntologyTerm;
import uk.ac.ebi.pride.prider.ontology.search.repository.SolrOntologyTermRepository;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
@Service
public class OntologyTermIndexService {

    private SolrOntologyTermRepository solrOntologyTermRepository;

    public OntologyTermIndexService(SolrOntologyTermRepository solrOntologyTermRepository) {
        this.solrOntologyTermRepository = solrOntologyTermRepository;
    }

    public void setSolrOntologyTermRepository(SolrOntologyTermRepository solrOntologyTermRepository) {
        this.solrOntologyTermRepository = solrOntologyTermRepository;
    }

    public void save(OntologyTerm ontologyTerm) {
        solrOntologyTermRepository.save(ontologyTerm);
    }
}
