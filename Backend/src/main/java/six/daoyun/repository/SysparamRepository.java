package six.daoyun.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import six.daoyun.entity.SystemParameter;

public interface SysparamRepository extends PagingAndSortingRepository<SystemParameter, Integer> {
    SystemParameter findByParameterName(String param);
    Page<SystemParameter> findAll(Pageable request);

    @Transactional
    void deleteByParameterName(String name);
}

