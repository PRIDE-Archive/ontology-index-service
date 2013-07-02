package uk.ac.ebi.pride.prider.ontology.search.tools;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import uk.ac.ebi.pride.prider.ontology.search.term.OntologyTerm;
import uk.ac.ebi.pride.prider.ontology.search.term.service.OntologyTermIndexService;
import uk.ac.ebi.pride.prider.ontology.search.term.service.OntologyTermSearchService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private final static int STEP = 89;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/app-context.xml");

        OntologyIndexBuilder projectIndexBuilder = context.getBean(OntologyIndexBuilder.class);

        indexProjects(projectIndexBuilder, projectIndexBuilder.solrProjectServer);

    }

    public static void indexProjects(OntologyIndexBuilder ontologyIndexBuilder, SolrServer server) {


        System.out.println("Retrieving ontology terms");
//        Iterable<Project> projects = ontologyIndexBuilder.projectRepository.findAll();


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
            List<OntologyTerm> ontologyTerms = new ArrayList<OntologyTerm>();

            // put some fake terms here
            for (int i=0;i<100;i++) {
                OntologyTerm newOntologyTerm = new OntologyTerm();
                newOntologyTerm.setAccession("TERM:"+i);
                newOntologyTerm.setValue("VALUE "+i);
                newOntologyTerm.setLabel("TEST");
                ontologyTerms.add(newOntologyTerm);
            }

            if (ontologyTerms.size() != 0) {
                for (int i=0; i<ontologyTerms.size(); i=i+STEP) {
                    server.addBeans(ontologyTerms.subList(i,Math.min(i + STEP - 1, ontologyTerms.size())));
                    server.commit();
                    System.out.println("Indexed terms from " + i + " to " + (i+STEP-1));
                }
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
