package uk.ac.ebi.pride.archive.ontology.search.service;

import org.springframework.stereotype.Service;
import uk.ac.ebi.pride.archive.ontology.model.OntologyTerm;
import uk.ac.ebi.pride.archive.ontology.search.repository.SolrOntologyTermRepository;

import java.util.List;

@Service
public class OntologyTermSearchService {

  private SolrOntologyTermRepository solrOntologyTermRepository;

  public OntologyTermSearchService(SolrOntologyTermRepository solrOntologyTermRepository) {
    this.solrOntologyTermRepository = solrOntologyTermRepository;
  }

  public void setSolrOntologyTermRepository(SolrOntologyTermRepository solrOntologyTermRepository) {
    this.solrOntologyTermRepository = solrOntologyTermRepository;
  }

  public List<OntologyTerm> findAllByName(String name) {
    return solrOntologyTermRepository.findAllByName(name);
  }

  public OntologyTerm findByAccession(String accession) {
    return solrOntologyTermRepository.findOne(accession);
  }

  public List<OntologyTerm> findAllByDescendant(String descendant) {
    return solrOntologyTermRepository.findAllByDescendants(descendant);
  }
}
