package uk.ac.ebi.pride.prider.ontology.ols;

import uk.ac.ebi.pride.prider.ontology.olsws.MapItem;
import uk.ac.ebi.pride.prider.ontology.olsws.Query;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public class OlsReadHelper {

    private Query queryService;

    public OlsReadHelper(Query queryService) {
        this.queryService = queryService;
    }

    public String getTermName(String ontology, String accession) {
//        Map map = queryService.getTermMetadata(accession,ontology);
//        return map.getItem().get(0).getValue().toString();
        return queryService.getTermById(accession,ontology);
    }

    public Set<String> getTermParentAccessions(String root, String ontology, String accession) {
        Set<String> res = new TreeSet<String>();

        // TODO: check if parent term exists to avoid stack oveflow errors because of endless recursive calls

        fillWithNodesAndParents(root, ontology, queryService.getTermParents(accession,ontology).getItem(), res);

        return res;
    }

    private void fillWithNodesAndParents(String root, String ontology, List<MapItem> items, Set<String> res) {
        for (MapItem item: items) {
            if (!item.getKey().equals(root)) {
                res.add(item.getKey().toString());
                fillWithNodesAndParents(root, ontology, queryService.getTermParents(item.getKey().toString(), ontology).getItem(),res);
            }
        }
    }
}
