package uk.ac.ebi.pride.prider.ontology.tools;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import uk.ac.ebi.pride.prider.ontology.model.OntologyTerm;
import uk.ac.ebi.pride.prider.ontology.reader.file.FileOntologyMapReader;
import uk.ac.ebi.pride.prider.ontology.search.service.OntologyTermIndexService;
import uk.ac.ebi.pride.prider.ontology.search.service.OntologyTermSearchService;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
@Component
public class OntologyIndexBuilder {

    /*
    HttpSolrServer is thread-safe and if you are using the following constructor,
    you *MUST* re-use the same instance for all requests.  If instances are created on
    the fly, it can cause a connection leak. The recommended practice is to keep a
    static instance of HttpSolrServer per solr server url and share it for all requests.
    See https://issues.apache.org/jira/browse/SOLR-861 for more details
    */
    @Autowired
    private SolrServer solrProjectServer;

    @Autowired
    private OntologyTermSearchService ontologyTermSearchService;

    @Autowired
    private OntologyTermIndexService ontologyTermIndexService;

    private FileOntologyMapReader fileOntologyMapReader;

    private final static int STEP = 89;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/app-context.xml");

        OntologyIndexBuilder projectIndexBuilder = context.getBean(OntologyIndexBuilder.class);

        try {
            projectIndexBuilder.fileOntologyMapReader = new FileOntologyMapReader(new File("src/main/resources/terms.xls"));
            indexProjects(projectIndexBuilder, projectIndexBuilder.solrProjectServer);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }




    }

    public static void indexProjects(OntologyIndexBuilder ontologyIndexBuilder, SolrServer server) {

        System.out.println("Retrieving ontology terms");
        // invert map from ascendants in file, to descendants
        Map<String,List<String>> ontologyTermDescendants = new HashMap<String, List<String>>();
        for (int i=0; i<ontologyIndexBuilder.fileOntologyMapReader.numTerms(); i++) {
            for (String termAscendantAccession : ontologyIndexBuilder.fileOntologyMapReader.getAscendants(i)) {
                if (ontologyTermDescendants.containsKey(termAscendantAccession)) {
                    ontologyTermDescendants.get(termAscendantAccession).add(ontologyIndexBuilder.fileOntologyMapReader.getAccession(i));
                } else {
                    ontologyTermDescendants.put(termAscendantAccession, new ArrayList<String>(asList(ontologyIndexBuilder.fileOntologyMapReader.getAccession(i))));
                }
            }

        }
        // build the list of terms
        List<OntologyTerm> ontologyTerms = new ArrayList<OntologyTerm>();
        for (int i=0; i<ontologyIndexBuilder.fileOntologyMapReader.numTerms(); i++) {
            OntologyTerm newOntologyTerm = new OntologyTerm();
            newOntologyTerm.setAccession(ontologyIndexBuilder.fileOntologyMapReader.getAccession(i));
            newOntologyTerm.setName(ontologyIndexBuilder.fileOntologyMapReader.getName(i));
            newOntologyTerm.setDescendants(ontologyTermDescendants.get(ontologyIndexBuilder.fileOntologyMapReader.getAccession(i)));
            ontologyTerms.add(newOntologyTerm);
        }

        try {
            //WARNING: deletes ALL entries from index
            System.out.println("Deleting index");
            server.deleteByQuery("*:*");
            server.commit();
        } catch (SolrServerException e) {
            throw new RuntimeException("Failed to delete data in Solr. "
                    + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete data in Solr. "
                    + e.getMessage(), e);
        }

        try {
            //add all projects
            System.out.println("Adding terms to index ");

            if (ontologyTerms.size() != 0) {
                for (int i=0; i<ontologyTerms.size(); i=i+STEP) {
                    server.addBeans(ontologyTerms.subList(i,Math.min(i + STEP - 1, ontologyTerms.size())));
                    server.commit();
                    System.out.println("Indexed terms from " + i + " to " + (i+STEP-1));
                }
//                List<OntologyTerm> searchResults = ontologyIndexBuilder.ontologyTermSearchService.findAllByRelatedAccession("TERM:1");
//                for (OntologyTerm searchResult: searchResults)
//                    System.out.println(searchResult.getAccession());
            } else {
                System.out.println("No terms to index found");
            }
        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
