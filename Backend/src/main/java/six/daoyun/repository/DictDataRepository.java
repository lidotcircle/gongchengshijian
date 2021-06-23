package six.daoyun.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import six.daoyun.entity.DictionaryData;
import six.daoyun.entity.DictionaryType;

public interface DictDataRepository extends PagingAndSortingRepository<DictionaryData, Integer> {
    @Query("SELECT dd FROM DictionaryData dd WHERE dd.keyword LIKE %?1% OR dd.value LIKE %?1% OR dd.remark LIKE %?1%")
    Page<DictionaryData> findByDictionaryType(String filter, DictionaryType type, Pageable request);
    Page<DictionaryData> findByDictionaryType(DictionaryType type, Pageable request);

    DictionaryData findByDictionaryTypeAndKeyword(DictionaryType type, String keyword);

    @Transactional
    void deleteByDictionaryTypeAndKeyword(DictionaryType type, String keyword);
}

