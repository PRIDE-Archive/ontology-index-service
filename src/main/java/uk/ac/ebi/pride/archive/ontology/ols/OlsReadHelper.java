package uk.ac.ebi.pride.archive.ontology.ols;

import uk.ac.ebi.pride.utilities.ols.web.service.client.OLSClient;
import uk.ac.ebi.pride.utilities.ols.web.service.config.OLSWsConfigProd;
import uk.ac.ebi.pride.utilities.ols.web.service.model.Identifier;
import uk.ac.ebi.pride.utilities.ols.web.service.model.Term;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class OlsReadHelper {

    private OLSClient olsClient;


  public OlsReadHelper() {
    new OlsReadHelper(new OLSClient(new OLSWsConfigProd()));
  }

    public OlsReadHelper(OLSClient olsClient) {
        this.olsClient = olsClient;
    }

    public String getTermName(String ontology, String accession) {
        return olsClient.getTermByOBOId(accession, ontology).getLabel();
    }

    public Set<String> getTermParentAccessions(String root, String ontology, String accession) {
        Set<String> result = new TreeSet<>();
        fillWithNodesAndParents(root,
            ontology,
            olsClient.getTermParents(new Identifier(accession, Identifier.IdentifierType.OBO),
                ontology,
                1),
            result);
        return result;
    }

    private void fillWithNodesAndParents(String root, String ontology, List<Term> terms, Set<String> result) {
        for (Term term: terms) {
            if (!term.getTermOBOId().getIdentifier().equals(root)) {
                if (!result.contains(term.getTermOBOId().getIdentifier())) {
                    result.add(term.getTermOBOId().getIdentifier());
                    fillWithNodesAndParents(root,
                        ontology,
                        olsClient.getTermParents(new Identifier(term.getTermOBOId().getIdentifier(), Identifier.IdentifierType.OBO),
                            ontology,
                            1),
                        result);
                }
            }
        }
    }
}
