package uk.ac.ebi.pride.archive.ontology.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uk.ac.ebi.pride.archive.ontology.map.file.FileOntologyMapReader;
import uk.ac.ebi.pride.archive.ontology.map.file.FileOntologyMapWriter;
import uk.ac.ebi.pride.archive.ontology.ols.OlsReadHelper;
import uk.ac.ebi.pride.utilities.ols.web.service.client.OLSClient;
import uk.ac.ebi.pride.utilities.ols.web.service.config.OLSWsConfigProd;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


@Component
public class TermMappingFileBuilder {

  private static FileOntologyMapReader fileOntologyMapReader;
  private static FileOntologyMapWriter fileOntologyMapWriter;
  private static OLSClient olsClient;
  private static OlsReadHelper olsReadHelper;
  private static Logger logger = LoggerFactory.getLogger(TermMappingFileBuilder.class.getName());

  public static void main(String[] args) {
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
      logger.error("Problem mapping terms", e);
    }
    olsClient = new OLSClient(new OLSWsConfigProd());
    olsReadHelper = new OlsReadHelper(olsClient);
  }

  public static void expandTerms(int page, String ontology, String rootTerm) throws IOException {
    Set<String> terms = new TreeSet<>(); // get all the term accessions from the file
    for (int i=0; i<fileOntologyMapReader.numTerms(page); i++) {
      terms.add(fileOntologyMapReader.getAccession(page, i));
    }
    boolean newTerm = true; // expand them if possible
    while (newTerm) {
      int oldSize = terms.size();
      Set<String> auxTerms = new TreeSet<>();
      auxTerms.addAll(terms);
      for (String termAccession: auxTerms) {
        logger.debug("Expanding term " + termAccession);
        terms.addAll(olsReadHelper.getTermParentAccessions(rootTerm, ontology, termAccession));
      }
      newTerm = (oldSize<terms.size());
    }
    Iterator<String> iterator = terms.iterator();  // add the terms and ascendants to the result file
    for (int i=0; iterator.hasNext(); i++) {
      String termAccession = iterator.next();
      logger.debug("-- PARENT TERMS FOR "+ termAccession + " --");
      printItems(olsReadHelper.getTermParentAccessions(rootTerm, ontology, termAccession));
      fileOntologyMapWriter.setAccession(page, i, termAccession);
      fileOntologyMapWriter.setName(page, i, olsReadHelper.getTermName(ontology, termAccession));
      fileOntologyMapWriter.setAscendants(page, i, olsReadHelper.getTermParentAccessions(rootTerm, ontology, termAccession));
      i++;
    }
    logger.info("- Term file created successfully -");
  }

  public static void printItems(Set<String> items) {
    for (String item: items) {
      logger.debug(item);
    }
  }
}
