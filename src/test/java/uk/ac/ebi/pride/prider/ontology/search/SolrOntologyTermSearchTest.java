package uk.ac.ebi.pride.prider.ontology.search;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.solr.core.SolrTemplate;
import uk.ac.ebi.pride.prider.ontology.search.term.OntologyTerm;
import uk.ac.ebi.pride.prider.ontology.search.term.OntologyTermFields;
import uk.ac.ebi.pride.prider.ontology.search.term.service.OntologyTermIndexingService;
import uk.ac.ebi.pride.prider.ontology.search.term.service.OntologyTermSearchService;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SolrOntologyTermSearchTest extends SolrTestCaseJ4 {

    private static final String TEST_ACCESSION = "TEST00001";
    private static final String TEST_VALUE = "Test value";
    private static final String TEST_LABEL = "TEST";
    private static final int NUM_RELATED_TERMS = 2;
    private static final String TEST_ACCESSION_2 = "TEST00002";

    private SolrServer server;
    private SolrOntologyTermRepositoryFactory solrOntologyTermRepositoryFactory;

    public static final long ZERO_DOCS = 0L;
    public static final long SINGLE_DOC = 1L;

    @BeforeClass
    public static void initialise() throws Exception {
        initCore("src/test/resources/solr/collection1/conf/solrconfig.xml",
                 "src/test/resources/solr/collection1/conf/schema.xml",
                 "src/test/resources/solr");
    }



    @Before
    @Override
    public void setUp() throws Exception {

        super.setUp();
        server = new EmbeddedSolrServer(h.getCoreContainer(), h.getCore().getName());
        server.deleteByQuery("*:*");

        solrOntologyTermRepositoryFactory = new SolrOntologyTermRepositoryFactory(new SolrTemplate(server));


    }

    @Test
    public void testThatNoResultsAreReturned() throws SolrServerException {

        SolrParams params = new SolrQuery("text that is not found");
        QueryResponse response = server.query(params);
        assertEquals(ZERO_DOCS, response.getResults().getNumFound());
    }


    @Test
    public void testThatDocumentIsIndexed() throws SolrServerException, IOException {
        OntologyTerm ontologyTerm = new OntologyTerm();
        ontologyTerm.setAccession(TEST_ACCESSION);
        ontologyTerm.setValue(TEST_VALUE);
        ontologyTerm.setLabel(TEST_LABEL);

        //add a new ontologyTerm to index
        OntologyTermIndexingService ontologyTermIndexingService = new OntologyTermIndexingService(this.solrOntologyTermRepositoryFactory.create());
        ontologyTermIndexingService.save(ontologyTerm);

        SolrParams params = new SolrQuery(OntologyTermFields.ACCESSION+":"+TEST_ACCESSION);
        QueryResponse response = server.query(params);
        assertEquals(SINGLE_DOC, response.getResults().getNumFound());
        assertEquals(TEST_ACCESSION, response.getResults().get(0).get(OntologyTermFields.ACCESSION));
        assertEquals(TEST_LABEL, response.getResults().get(0).get(OntologyTermFields.LABEL));
//        assertEquals(TEST_VALUE, response.getResults().get(0).get(OntologyTermFields.VALUE));
    }

    @Test
    public void testThatDocumentIsSearched() throws SolrServerException, IOException {
        OntologyTerm ontologyTerm = new OntologyTerm();
        ontologyTerm.setAccession(TEST_ACCESSION);
        ontologyTerm.setValue(TEST_VALUE);
        ontologyTerm.setLabel(TEST_LABEL);
        List<String> relatedAccessions = new LinkedList<String>();
        relatedAccessions.add("TEST00008");
        relatedAccessions.add("TEST00009");
        ontologyTerm.setRelatedAccessions(relatedAccessions);

        //add a new ontologyTerm to index
        OntologyTermIndexingService ontologyTermIndexingService = new OntologyTermIndexingService(this.solrOntologyTermRepositoryFactory.create());
        ontologyTermIndexingService.save(ontologyTerm);
        ontologyTerm.setAccession(TEST_ACCESSION_2);
        ontologyTermIndexingService.save(ontologyTerm);

        OntologyTermSearchService ontologyTermSearchService = new OntologyTermSearchService(this.solrOntologyTermRepositoryFactory.create());
        OntologyTerm ontologyTerm2 = ontologyTermSearchService.findByAccession(TEST_ACCESSION);

        assertEquals(TEST_ACCESSION, ontologyTerm2.getAccession());
        assertEquals(TEST_LABEL, ontologyTerm2.getLabel());
//        assertEquals(TEST_VALUE, ontologyTerm2.getValue());

        Collection<OntologyTerm> ontologyTerms = ontologyTermSearchService.findAllByValue(TEST_VALUE);
        assertEquals(2, ontologyTerms.size());

        ontologyTerms = ontologyTermSearchService.findAllByRelatedAccession("TEST00008");
        assertEquals(2, ontologyTerms.size());


    }

////    @Test
//    public void testThatDocumentIsSearchedByRelatedAccession() throws SolrServerException, IOException {
//        OntologyTerm ontologyTerm = new OntologyTerm();
//        ontologyTerm.setAccession(TEST_ACCESSION);
//        ontologyTerm.setValue(TEST_VALUE);
//        ontologyTerm.setLabel(TEST_LABEL);
//
//        //add a new ontologyTerm to index
//        OntologyTermIndexingService ontologyTermIndexingService = new OntologyTermIndexingService(this.solrOntologyTermRepositoryFactory.create());
//        ontologyTermIndexingService.save(ontologyTerm);
//
//        OntologyTermSearchService ontologyTermSearchService = new OntologyTermSearchService(this.solrOntologyTermRepositoryFactory.create());
//        OntologyTerm ontologyTerm2 = ontologyTermSearchService.findByRelatedAccession(TEST_RELATED_ACCESSION);
//
//        assertEquals(TEST_ACCESSION, ontologyTerm2.getAccession());
//        assertEquals(TEST_LABEL, ontologyTerm2.getLabel());
//        assertEquals(TEST_VALUE, ontologyTerm2.getValue());
////        assertEquals(NUM_RELATED_TERMS, ontologyTerm2.getRelatedAccessions().size());
//    }
}