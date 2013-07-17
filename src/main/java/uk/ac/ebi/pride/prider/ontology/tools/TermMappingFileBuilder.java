package uk.ac.ebi.pride.prider.ontology.tools;

import org.springframework.stereotype.Component;
import uk.ac.ebi.pride.prider.ontology.map.file.FileOntologyMapReader;
import uk.ac.ebi.pride.prider.ontology.map.file.FileOntologyMapWriter;
import uk.ac.ebi.pride.prider.ontology.ols.OlsReadHelper;
import uk.ac.ebi.pride.prider.ontology.olsws.Query;
import uk.ac.ebi.pride.prider.ontology.olsws.QueryService;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
@Component
public class TermMappingFileBuilder {

    private static FileOntologyMapReader fileOntologyMapReader;
    private static FileOntologyMapWriter fileOntologyMapWriter;

    public static void main(String[] args) {
        Query queryService = new QueryService().getOntologyQuery();
        OlsReadHelper olsReadHelper = new OlsReadHelper(queryService);
        try {
            File termFile = new File("src/main/resources/terms.xls");

            // get all the term accessions from the file
            fileOntologyMapReader = new FileOntologyMapReader(termFile);
            Set<String> terms = new TreeSet<String>();
            for (int i=0; i<fileOntologyMapReader.numTerms(); i++) {
                terms.add(fileOntologyMapReader.getAccession(i));
            }

            // expand them if possible
            boolean newTerm = true;
            while (newTerm) {
                int oldSize = terms.size();
                for (String termAccession: terms) {
                    terms.addAll(olsReadHelper.getTermParentAccessions("BTO:0000000","BTO",termAccession));
                }
                newTerm = (oldSize<terms.size());
            }

            // add the terms and ascendants to the result file
            fileOntologyMapWriter = new FileOntologyMapWriter(termFile);
            int i=0;
            for (String termAccession : terms) {
                System.out.println("-- PARENT TERMS FOR "+ termAccession +"--");
                printMap(olsReadHelper.getTermParentAccessions("BTO:0000000", "BTO", termAccession));
                fileOntologyMapWriter.setAccession(i, termAccession);
                fileOntologyMapWriter.setName(i, olsReadHelper.getTermName("BTO", termAccession));
                fileOntologyMapWriter.setAscendants(i, olsReadHelper.getTermParentAccessions("BTO:0000000", "BTO", termAccession));
                i++;
            }


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



    }

    public static void printMap(Set<String> map) {
        for (String item: map) {
            System.out.println(item);
        }
    }

//    public static void printNodesAndParents(Query queryService, String root, String ontology, List<MapItem> items) {
//        for (MapItem item: items) {
//            if (!item.getKey().equals(root)) {
//                System.out.println(item.getKey());
//                printNodesAndParents(queryService, root, ontology, queryService.getTermParents(item.getKey().toString(), ontology).getItem());
//            }
//        }
//    }

}
