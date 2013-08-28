package org.javers.model.object.graph;

import org.javers.common.validation.Validate;
import org.javers.model.mapping.Property;

import java.util.List;

/**
 * OneToOne or ManyToOne relation
 * @author bartosz walacik
 */
public class SingleEdge extends Edge {
    private ObjectNode reference;

    public SingleEdge(Property property, ObjectNode referenceTarget) {
        super(property);
        Validate.argumentIsNotNull(referenceTarget);

        this.reference = referenceTarget;
    }

    public ObjectNode getReference() {
        return reference;
    }
}