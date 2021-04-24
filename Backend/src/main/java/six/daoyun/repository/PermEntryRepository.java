package six.daoyun.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import six.daoyun.entity.Role;
import six.daoyun.entity.PermEntry;

public interface PermEntryRepository extends CrudRepository<PermEntry, Long> {
    Optional<PermEntry> findByDescriptor(String descriptor);
    Optional<PermEntry> findByLink(String link);

    Optional<PermEntry> findByLinkAndRoles(String link, Role role);
    Collection<PermEntry> findByRoles(Role role);

    Collection<PermEntry> findByParentIsNull();
}

