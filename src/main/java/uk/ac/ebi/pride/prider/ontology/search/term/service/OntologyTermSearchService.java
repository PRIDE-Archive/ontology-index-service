package uk.ac.ebi.pride.prider.ontology.search.term.service;

import org.springframework.stereotype.Service;
import uk.ac.ebi.pride.prider.ontology.search.term.OntologyTerm;
import uk.ac.ebi.pride.prider.ontology.search.term.repository.SolrOntologyTermRepository;

import java.util.List;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
@Service
public class OntologyTermSearchService {

    private SolrOntologyTermRepository solrOntologyTermRepository;

    public OntologyTermSearchService(SolrOntologyTermRepository solrOntologyTermRepository) {
        this.solrOntologyTermRepository = solrOntologyTermRepository;
    }

    public void setSolrOntologyTermRepository(SolrOntologyTermRepository solrOntologyTermRepository) {
        this.solrOntologyTermRepository = solrOntologyTermRepository;
    }

    public List<OntologyTerm> findAllByValue(String value) {
        return solrOntologyTermRepository.findAllByValue(value);
    }

    public List<OntologyTerm> findAllByLabel(String label) {
        return solrOntologyTermRepository.findAllByLabel(label);
    }

    public OntologyTerm findByAccession(String accession) {
        return solrOntologyTermRepository.findOne(accession);
    }

    public List<OntologyTerm> findAllByRelatedAccession(String relatedAccession) {
        return solrOntologyTermRepository.findAllByRelatedAccessions(relatedAccession);
    }
}
