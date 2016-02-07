package org.javers.core.graph;

import org.javers.common.collections.Optional;
import org.javers.core.metamodel.object.Cdo;
import org.javers.core.metamodel.object.CdoWrapper;
import org.javers.core.metamodel.object.GlobalId;
import org.javers.core.metamodel.object.GlobalIdFactory;
import org.javers.core.metamodel.object.OwnerContext;
import org.javers.core.metamodel.type.EntityType;
import org.javers.core.metamodel.type.ManagedType;
import org.javers.core.metamodel.type.ShallowReferenceType;
import org.javers.core.metamodel.type.TypeMapper;

/**
 * @author bartosz walacik
 */
public class LiveCdoFactory implements CdoFactory {

    private final GlobalIdFactory globalIdFactory;
    private ObjectAccessHook objectAccessHook;
    private TypeMapper typeMapper;

    public LiveCdoFactory(GlobalIdFactory globalIdFactory, ObjectAccessHook objectAccessHook, TypeMapper typeMapper) {
        this.globalIdFactory = globalIdFactory;
        this.objectAccessHook = objectAccessHook;
        this.typeMapper = typeMapper;
    }

    @Override
    public Cdo create(Object wrappedCdo, OwnerContext owner, boolean isShallowReference) {
        Object wrappedCdoAccessed = objectAccessHook.access(wrappedCdo);
        GlobalId globalId = globalIdFactory.createId(wrappedCdoAccessed, owner);
        ManagedType originalManagedType = typeMapper.getJaversManagedType(wrappedCdoAccessed.getClass());
        ManagedType managedType;
        if (!isShallowReference) {
            managedType = originalManagedType;
        } else {
            if (!(originalManagedType instanceof EntityType)) {
                throw new RuntimeException("Shallow Reference support only entityType");
            }
            managedType = new ShallowReferenceType(originalManagedType,
                    Optional.fromNullable(((EntityType) originalManagedType).getIdProperty()));
        }
        return new CdoWrapper(wrappedCdoAccessed, globalId, managedType);
    }

    @Override
    public String typeDesc() {
        return "live";
    }
}
