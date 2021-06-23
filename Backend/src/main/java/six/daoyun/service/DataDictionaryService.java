package six.daoyun.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import six.daoyun.entity.DictionaryData;
import six.daoyun.entity.DictionaryType;

public interface DataDictionaryService {
    void createDictionaryType(DictionaryType dictType);
    void updateDictionaryType(DictionaryType dictType);
    void deleteDictionaryTypeByTypeCode(String typeCode);

    Optional<DictionaryType> getDictionaryType(String typeCode);
    Page<DictionaryType> getDictionaryTypePage(int pageno, int size, String sortKey, boolean sortDesc, String filter);

    void createDictionaryData(DictionaryData dictData);
    void updateDictionaryData(DictionaryData dictData);
    void deleteDictionaryData(DictionaryType dictType, String keyword);

    Optional<DictionaryData> getDictionaryData(DictionaryType type, String keyword);
    Page<DictionaryData> getDictionaryDataPage(DictionaryType type, int pageno, int size, String sortKey, boolean sortDesc, String filter);
}

