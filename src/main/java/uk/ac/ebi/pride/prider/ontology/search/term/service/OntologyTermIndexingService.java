package uk.ac.ebi.pride.prider.ontology.search.term.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.pride.prider.ontology.search.term.OntologyTerm;
import uk.ac.ebi.pride.prider.ontology.search.term.repository.SolrOntologyTermRepository;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
@Service
public class OntologyTermIndexingService {

    private SolrOntologyTermRepository solrOntologyTermRepository;

    public OntologyTermIndexingService(SolrOntologyTermRepository solrOntologyTermRepository) {
        this.solrOntologyTermRepository = solrOntologyTermRepository;
    }

    public void setSolrOntologyTermRepository(SolrOntologyTermRepository solrOntologyTermRepository) {
        this.solrOntologyTermRepository = solrOntologyTermRepository;
    }

    public void save(OntologyTerm ontologyTerm) {
        solrOntologyTermRepository.save(ontologyTerm);
    }
}
