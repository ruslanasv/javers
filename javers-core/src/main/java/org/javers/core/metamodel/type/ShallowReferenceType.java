package org.javers.core.metamodel.type;

import org.javers.common.collections.Optional;
import org.javers.common.collections.Predicate;
import org.javers.core.metamodel.property.Property;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author akrystian
 */
public class ShallowReferenceType extends EntityType {
    public ShallowReferenceType(ManagedType managedType, Optional<Property> idProperty) {
        super(managedType.getManagedClass(), idProperty);
    }

    @Override
    public List<Property> getProperties(Predicate<Property> query) {
        return filterProperties(super.getProperties(query));
    }

    @Override
    public List<Property> getProperties() {
        return Arrays.asList(super.getIdProperty());
    }

    private List<Property> filterProperties(List<Property> input) {
        Property idProperty = super.getIdProperty();
        if (input.contains(idProperty)) {
            return Arrays.asList(idProperty);
        } else {
            return Collections.<Property>emptyList();
        }
    }
}
