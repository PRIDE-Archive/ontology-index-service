package uk.ac.ebi.pride.prider.ontology.tools;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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
    private static Query queryService;
    private static OlsReadHelper olsReadHelper;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/app-context.xml");

        TermMappingFileBuilder termMappingFileBuilder = context.getBean(TermMappingFileBuilder.class);

        queryService = new QueryService().getOntologyQuery();
        olsReadHelper = new OlsReadHelper(queryService);

        try {

            File inputTerms = new File("src/main/resources/inputTerms.xls");
            File expandedTerms = new File("src/main/resources/terms.xls");
            fileOntologyMapReader = new FileOntologyMapReader(inputTerms);
            fileOntologyMapWriter = new FileOntologyMapWriter(expandedTerms);
            // BTO
            expandTerms(0, "BTO","BTO:0000000");
            // DOID
            expandTerms(1, "DOID","DOID:0000000");
            // NEWT
            expandTerms(2, "NEWT","1");
            // CL
            expandTerms(3, "CL","CL:0000000");

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



    }

    public static void expandTerms(int page, String ontology, String rootTerm) throws IOException {
        // get all the term accessions from the file

        Set<String> terms = new TreeSet<String>();
        for (int i=0; i<fileOntologyMapReader.numTerms(page); i++) {
            terms.add(fileOntologyMapReader.getAccession(page, i));
        }

        // expand them if possible
        boolean newTerm = true;
        while (newTerm) {
            int oldSize = terms.size();
            Set<String> auxTerms = new TreeSet<String>();
            auxTerms.addAll(terms);
            for (String termAccession: auxTerms) {
                System.out.println("Expanding term "+termAccession);
                terms.addAll(olsReadHelper.getTermParentAccessions(rootTerm,ontology,termAccession));
            }
            newTerm = (oldSize<terms.size());
        }

        // add the terms and ascendants to the result file

        int i=0;
        for (String termAccession : terms) {
            System.out.println("-- PARENT TERMS FOR "+ termAccession + " --");
            printItems(olsReadHelper.getTermParentAccessions(rootTerm, ontology, termAccession));
            fileOntologyMapWriter.setAccession(page, i, termAccession);
            fileOntologyMapWriter.setName(page, i, olsReadHelper.getTermName(ontology, termAccession));
            fileOntologyMapWriter.setAscendants(page, i, olsReadHelper.getTermParentAccessions(rootTerm, ontology, termAccession));
            i++;
        }

        System.out.println("- Term file created successfully -");
    }

    public static void printItems(Set<String> items) {
        for (String item: items) {
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
