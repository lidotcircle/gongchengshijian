package six.daoyun.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import six.daoyun.entity.Role;
import six.daoyun.entity.PermEntry;

public interface PermEntryRepository extends CrudRepository<PermEntry, Long> {
    Optional<PermEntry> findByDescriptor(String descriptor);
    Optional<PermEntry> findByLink(String link);

    Optional<PermEntry> findByDescriptorAndRoles_roleName(String descriptor, String roleName);
    Collection<PermEntry> findByRoles(Role role);

    Collection<PermEntry> findByParentIsNull();
}

