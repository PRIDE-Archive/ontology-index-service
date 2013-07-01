package uk.ac.ebi.pride.prider.ontology.search.term;

import org.apache.solr.client.solrj.beans.Field;

import java.util.List;

/**
 * @author Jose A. Dianes
 * @version $Id$
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
