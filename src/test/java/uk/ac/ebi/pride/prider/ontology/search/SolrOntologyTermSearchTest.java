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
import uk.ac.ebi.pride.prider.ontology.search.term.repository.SolrOntologyTermRepository;

import java.io.IOException;

public class SolrOntologyTermSearchTest extends SolrTestCaseJ4 {

    private static final String TEST_ACCESSION = "TEST00001";
    private static final String TEST_VALUE = "Test value";
    private static final String TEST_LABEL = "TEST";

    private SolrServer server;
    private OntologyTerm ontologyTerm;
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
    public void testThatDocumentIsFound() throws SolrServerException, IOException {
        ontologyTerm = new OntologyTerm();
        ontologyTerm.setAccession(TEST_ACCESSION);
        ontologyTerm.setValue(TEST_VALUE);
        ontologyTerm.setLabel(TEST_LABEL);

        //add a new ontologyTerm to index
        SolrOntologyTermRepository solrOntologyTermRepository = this.solrOntologyTermRepositoryFactory.create();
        solrOntologyTermRepository.save(ontologyTerm);

        SolrParams params = new SolrQuery(OntologyTermFields.ACCESSION+":"+TEST_ACCESSION);
        QueryResponse response = server.query(params);
        assertEquals(SINGLE_DOC, response.getResults().getNumFound());
        assertEquals(TEST_ACCESSION, response.getResults().get(0).get(OntologyTermFields.ACCESSION));
        assertEquals(TEST_LABEL, response.getResults().get(0).get(OntologyTermFields.LABEL));
        assertEquals(TEST_VALUE, response.getResults().get(0).get(OntologyTermFields.VALUE));
    }
}