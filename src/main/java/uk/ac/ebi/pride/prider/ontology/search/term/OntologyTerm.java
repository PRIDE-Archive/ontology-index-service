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
    private List<String> realtedAccessions;

    @Field(OntologyTermFields.RELATED_VALUES)
    private List<String> relatedValues;

    @Field(OntologyTermFields.RELATED_LABELS)
    private List<String> relatedLabels;

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

    public List<String> getRealtedAccessions() {
        return realtedAccessions;
    }

    public void setRealtedAccessions(List<String> realtedAccessions) {
        this.realtedAccessions = realtedAccessions;
    }

    public List<String> getRelatedValues() {
        return relatedValues;
    }

    public void setRelatedValues(List<String> relatedValues) {
        this.relatedValues = relatedValues;
    }

    public List<String> getRelatedLabels() {
        return relatedLabels;
    }

    public void setRelatedLabels(List<String> relatedLabels) {
        this.relatedLabels = relatedLabels;
    }
}
