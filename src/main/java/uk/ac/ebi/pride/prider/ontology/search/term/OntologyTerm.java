package uk.ac.ebi.pride.prider.ontology.search.term;

import org.apache.solr.client.solrj.beans.Field;

import java.util.List;

/**
 * @author Jose A. Dianes
 * @version $Id$
 *
 * An ontology term is defined by:
 *   1) its unique accession
 *   2) its value
 *   3) its label
 *   4) a list of related terms that are supposed to be descendants of this term. These terms allow
 *      searching for the main term using any of them and, therefore, to look for all related
 *      parent terms for a given child term that is given to the search service (it will return all
 *      the terms that contain the child term as a related one)
 */
public class OntologyTerm {

    @Field(OntologyTermFields.ACCESSION)
    private String accession;

    @Field(OntologyTermFields.VALUE)
    private String value;

    @Field(OntologyTermFields.LABEL)
    private String label;

    @Field(OntologyTermFields.RELATED_ACCESSIONS)
    private List<String> relatedAccessions;

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getRelatedAccessions() {
        return relatedAccessions;
    }

    public void setRelatedAccessions(List<String> relatedAccessions) {
        this.relatedAccessions = relatedAccessions;
    }

}
