/**
 * Copyright (c) 2015 Source Auditor Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
*/
package org.spdx.rdfparser.model;

import java.util.ArrayList;

import org.spdx.rdfparser.IModelContainer;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.SpdxRdfConstants;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * An SpdxElement is any thing described in SPDX, either a document or an SpdxItem. 
 * SpdxElements can be related to other SpdxElements.
 * 
 * All subclasses should override getType, equals and hashCode.
 * 
 * If a subproperty is used for the name property name, getNamePropertyName should be overridden.
 * 
 * If absolute URIs are required, getUri should be overriden.
 * 
 * @author Gary O'Neall
 *
 */
public class SpdxElement extends RdfModelObject {
	
	Annotation[] annotations;
	String comment;
	String name;
	Relationship[] relationships;
	
	public SpdxElement(IModelContainer modelContainer, Node node) throws InvalidSPDXAnalysisException {
		super(modelContainer, node);
		this.annotations = findAnnotationPropertyValues(SpdxRdfConstants.SPDX_NAMESPACE, SpdxRdfConstants.PROP_ANNOTATION);
		this.comment = findSinglePropertyValue(SpdxRdfConstants.RDFS_NAMESPACE, SpdxRdfConstants.RDFS_PROP_COMMENT);
		this.name = findSinglePropertyValue(SpdxRdfConstants.RDFS_NAMESPACE, this.getNamePropertyName());
		this.relationships = findRelationshipPropertyValues(SpdxRdfConstants.SPDX_NAMESPACE, SpdxRdfConstants.PROP_RELATIONSHIP);
	}

	/**
	 * @param name Name of the element
	 * @param comment Optional comment on the element
	 * @param annotations Optional annotations for the element
	 * @param relationships Optional relationships with other elements
	 */
	public SpdxElement(String name, String comment, Annotation[] annotations,
			Relationship[] relationships) {
		this.name = name;
		this.comment = comment;
		if (annotations != null) {
			this.annotations = annotations;
		} else {
			this.annotations = new Annotation[0];
		}
		if (relationships != null) {
			this.relationships = relationships;
		} else {
			this.relationships = new Relationship[0];
		}
	}
	
	protected void populateModel() throws InvalidSPDXAnalysisException {
		if (this.model != null) {
			if (this.name != null) {
				setPropertyValue(SpdxRdfConstants.RDFS_NAMESPACE, getNamePropertyName(), name);
			}
			if (this.comment != null) {
				setPropertyValue(SpdxRdfConstants.RDFS_NAMESPACE, SpdxRdfConstants.RDFS_PROP_COMMENT, comment);
			}
			if (this.annotations != null) {
				setPropertyValues(SpdxRdfConstants.SPDX_NAMESPACE, SpdxRdfConstants.PROP_ANNOTATION, annotations);
			}
			if (this.relationships != null) {
				setPropertyValues(SpdxRdfConstants.SPDX_NAMESPACE, SpdxRdfConstants.PROP_RELATIONSHIP, relationships);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.spdx.rdfparser.model.IRdfModel#verify()
	 */
	@Override
	public ArrayList<String> verify() {
		ArrayList<String> retval = new ArrayList<String>();
		if (this.name == null) {
			retval.add("Missing required name");
		}
		return retval;
	}


	/**
	 * @return the annotations
	 */
	public Annotation[] getAnnotations() {
		return annotations;
	}


	/**
	 * @param annotations the annotations to set
	 * @throws InvalidSPDXAnalysisException 
	 */
	public void setAnnotations(Annotation[] annotations) throws InvalidSPDXAnalysisException {
		this.annotations = annotations;
		setPropertyValues(SpdxRdfConstants.SPDX_NAMESPACE, SpdxRdfConstants.PROP_ANNOTATION, annotations);
	}


	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}


	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
		setPropertyValue(SpdxRdfConstants.RDFS_NAMESPACE, SpdxRdfConstants.RDFS_PROP_COMMENT, comment);
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the property name used for the Name property.  Override this function if using a subproperty of SPDX Name
	 */
	protected String getNamePropertyName() {
		return SpdxRdfConstants.PROP_NAME;
	}

	/**
	 * Set the name
	 * @param name the name to set 
	 */
	public void setName(String name) {
		this.name = name;
		setPropertyValue(SpdxRdfConstants.SPDX_NAMESPACE, getNamePropertyName(), name);
	}


	/**
	 * @return the relationships
	 */
	public Relationship[] getRelationships() {
		return relationships;
	}


	/**
	 * @param relationships the relationships to set
	 * @throws InvalidSPDXAnalysisException 
	 */
	public void setRelationships(Relationship[] relationships) throws InvalidSPDXAnalysisException {
		this.relationships = relationships;
		setPropertyValues(SpdxRdfConstants.SPDX_NAMESPACE, SpdxRdfConstants.PROP_RELATIONSHIP, relationships);
	}

	/* (non-Javadoc)
	 * @see org.spdx.rdfparser.model.RdfModelObject#getUri(org.spdx.rdfparser.IModelContainer)
	 */
	@Override
	String getUri(IModelContainer modelContainer) {
		if (this.node != null && this.node.isURI()) {
			return this.node.getURI();
		} else {
			return null;
		}
	}


	/* (non-Javadoc)
	 * @see org.spdx.rdfparser.model.RdfModelObject#getType(com.hp.hpl.jena.rdf.model.Model)
	 */
	@Override
	Resource getType(Model model) {
		return model.createResource(SpdxRdfConstants.SPDX_NAMESPACE + SpdxRdfConstants.CLASS_SPDX_ELEMENT);
	}

	/* (non-Javadoc)
	 * @see org.spdx.rdfparser.model.RdfModelObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SpdxElement)) {
			return false;
		}
		SpdxElement comp = (SpdxElement)o;
		return (equalsConsideringNull(comp.getName(), this.getName()) &&
				arraysEqual(comp.getAnnotations(), this.getAnnotations()) &&
				arraysEqual(comp.getRelationships(), this.getRelationships()) &&
				equalsConsideringNull(comp.getComment(), this.getComment()));
	}

	/* (non-Javadoc)
	 * @see org.spdx.rdfparser.model.RdfModelObject#hashCode()
	 */
	@Override
	public int hashCode() {
		int retval = 0;
		if (name != null) {
			retval = retval ^ name.hashCode();
		}
		if (annotations != null) {
			for (int i = 0; i < annotations.length; i++) {
				if (annotations[i] != null) {
					retval = retval ^ annotations[i].hashCode();
				}
			}
		}
		if (relationships != null) {
			for (int i = 0; i < relationships.length; i++) {
				if (relationships[i] != null) {
					retval = retval ^ relationships[i].hashCode();
				}
			}
		}
		if (comment != null) {
			retval = retval ^ comment.hashCode();
		}
		return retval;
	}
	
	public SpdxElement clone() {
		Annotation[] clonedAnnotations = null;
		if (this.annotations != null) {
			clonedAnnotations = new Annotation[this.annotations.length];
			for (int i = 0; i < this.annotations.length; i++) {
				clonedAnnotations[i] = this.annotations[i].clone();
			}
		}
		Relationship[] clonedRelationships = null;
		if (this.relationships != null) {
			clonedRelationships = new Relationship[this.relationships.length];
			for (int i = 0; i < this.relationships.length; i++) {
				clonedRelationships[i] = this.relationships[i].clone();
			}
		}
		return new SpdxElement(this.name, this.comment, clonedAnnotations, clonedRelationships);
	}

}