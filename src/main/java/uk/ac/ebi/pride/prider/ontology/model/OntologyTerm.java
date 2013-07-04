package uk.ac.ebi.pride.prider.ontology.model;

import org.apache.solr.client.solrj.beans.Field;

import java.util.List;

/**
 * @author Jose A. Dianes
 * @version $Id$
 *
 * An ontology term is defined by:
 *   1) its unique accession
 *   2) its name
 *   3) a list of related terms that are supposed to be descendants of this term. These terms allow
 *      searching for the main term using any of them and, therefore, to look for all related
 *      parent terms for a given child term that is given to the search service (it will return all
 *      the terms that contain the child term as a related one)
 */
public class OntologyTerm {

    @Field(OntologyTermFields.ACCESSION)
    private String accession;

    @Field(OntologyTermFields.NAME)
    private String name;

    @Field(OntologyTermFields.DESCENDANTS)
    private List<String> descendants;

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDescendants() {
        return descendants;
    }

    public void setDescendants(List<String> descendants) {
        this.descendants = descendants;
    }
}
