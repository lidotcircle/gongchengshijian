package six.daoyun.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import six.daoyun.entity.DictionaryType;

public interface DictTypeRepository extends PagingAndSortingRepository<DictionaryType, Integer> {
    DictionaryType findByTypeName(String typeName);
    DictionaryType findByTypeCode(String typeCode);

    @Query("SELECT dt FROM DictionaryType dt WHERE dt.typeName LIKE %?1% OR dt.typeCode LIKE %?1% OR dt.remark LIKE %?1%")
    Page<DictionaryType> findAll(String filter, Pageable request);
    Page<DictionaryType> findAll(Pageable request);

    @Transactional
    void deleteByTypeName(String typename);
    @Transactional
    void deleteByTypeCode(String typecode);
}

