package uk.ac.ebi.pride.archive.ontology.tools;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import uk.ac.ebi.pride.archive.ontology.model.OntologyTerm;
import uk.ac.ebi.pride.archive.ontology.map.file.FileOntologyMapReader;
import uk.ac.ebi.pride.archive.ontology.search.service.OntologyTermIndexService;
import uk.ac.ebi.pride.archive.ontology.search.service.OntologyTermSearchService;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;

@Component
public class OntologyIndexBuilder {

  private static Logger logger = LoggerFactory.getLogger(OntologyIndexBuilder.class.getName());

  private static final int NUM_PAGES = 4;
  /*
      HttpSolrServer is thread-safe and if you are using the following constructor,
      you *MUST* re-use the same instance for all requests.  If instances are created on
      the fly, it can cause a connection leak. The recommended practice is to keep a
      static instance of HttpSolrServer per solr server url and share it for all requests.
      See https://issues.apache.org/jira/browse/SOLR-861 for more details
      */
  @Autowired
  private SolrServer solrOntologyServer;

  @Autowired
  private OntologyTermSearchService ontologyTermSearchService;

  @Autowired
  private OntologyTermIndexService ontologyTermIndexService;

  private FileOntologyMapReader fileOntologyMapReader;

  private final static int STEP = 89;

  public static void main(String[] args) throws IOException {
    ApplicationContext context = new ClassPathXmlApplicationContext("spring/app-context.xml");
    OntologyIndexBuilder projectIndexBuilder = context.getBean(OntologyIndexBuilder.class);
    projectIndexBuilder.fileOntologyMapReader = new FileOntologyMapReader(new File("src/main/resources/terms.xls"));
    indexProjects(projectIndexBuilder, projectIndexBuilder.solrOntologyServer);
  }

  public static void indexProjects(OntologyIndexBuilder ontologyIndexBuilder, SolrServer server) {
    Map<String,List<String>> ontologyTermDescendants = new HashMap<String, List<String>>();
    List<OntologyTerm> ontologyTerms = new ArrayList<OntologyTerm>();
    for (int page = 0; page<NUM_PAGES; page++) { // for each ontology (page)
      logger.debug("Retrieving ontology terms for page " + page);
      // invert map from ascendants in file, to descendants
      for (int i=0; i<ontologyIndexBuilder.fileOntologyMapReader.numTerms(page); i++) {
        for (String termAscendantAccession : ontologyIndexBuilder.fileOntologyMapReader.getAscendants(page,i)) {
          if (ontologyTermDescendants.containsKey(termAscendantAccession)) {
            ontologyTermDescendants.get(termAscendantAccession).add(ontologyIndexBuilder.fileOntologyMapReader.getAccession(page,i));
          } else {
            ontologyTermDescendants.put(termAscendantAccession, new ArrayList<>(asList(ontologyIndexBuilder.fileOntologyMapReader.getAccession(page,i))));
          }
        }
      }
      for (int i=0; i<ontologyIndexBuilder.fileOntologyMapReader.numTerms(page); i++) {
        OntologyTerm newOntologyTerm = new OntologyTerm();
        newOntologyTerm.setAccession(ontologyIndexBuilder.fileOntologyMapReader.getAccession(page,i));
        newOntologyTerm.setName(ontologyIndexBuilder.fileOntologyMapReader.getName(page,i));
        newOntologyTerm.setDescendants(ontologyTermDescendants.get(ontologyIndexBuilder.fileOntologyMapReader.getAccession(page,i)));
        ontologyTerms.add(newOntologyTerm);
      }
    }
    try {
      logger.info("Deleting index"); //WARNING: deletes ALL entries from index
      server.deleteByQuery("*:*");
      server.commit();
    } catch (SolrServerException|IOException e) {
      throw new RuntimeException("Failed to delete data in Solr. "
          + e.getMessage(), e);
    }
    try {
      logger.info("Adding terms to index "); //add all projects
      if (ontologyTerms.size() != 0) {
        for (int i=0; i<ontologyTerms.size(); i=i+STEP) {
          server.addBeans(ontologyTerms.subList(i,Math.min(i + STEP - 1, ontologyTerms.size())));
          server.commit();
          logger.debug("Indexed terms from " + i + " to " + (i+STEP-1));
        }
      } else {
        logger.info("No terms to index found");
      }
    } catch (SolrServerException|IOException e) {
      logger.error("Problem indexing terms", e);
    }
  }
}