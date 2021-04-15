package six.daoyun.service.DataDictionaryServiceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import six.daoyun.entity.DictionaryData;
import six.daoyun.entity.DictionaryType;
import six.daoyun.repository.DictDataRepository;
import six.daoyun.repository.DictTypeRepository;
import six.daoyun.service.DataDictionaryService;


@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {
    @Autowired
    private DictTypeRepository typeRepository;
    @Autowired
    private DictDataRepository dataRepository;


	@Override
	public void createDictionaryType(DictionaryType dictType) {
        this.typeRepository.save(dictType);
	}

	@Override
	public void updateDictionaryType(DictionaryType dictType) {
        this.typeRepository.save(dictType);
	}

	@Override
	public void deleteDictionaryTypeByTypeCode(String typeCode) {
        this.typeRepository.deleteByTypeCode(typeCode);
	}

	@Override
	public Optional<DictionaryType> getDictionaryType(String typeCode) {
        return Optional.ofNullable(this.typeRepository.findByTypeCode(typeCode));
	}

	@Override
	public Page<DictionaryType> getDictionaryTypePage(int pageno, int size, String sortKey, boolean sortDesc,
            String filter) {
        Sort sort = Sort.by(sortKey);
        if(sortDesc) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable page = PageRequest.of(pageno, size, sort);
        if(filter != null && filter.length() > 0) {
            return this.typeRepository.findAll(page);
        } else {
            return this.typeRepository.findAll(filter, page);
        }
	}

	@Override
	public void createDictionaryData(DictionaryData dictData) {
        this.dataRepository.save(dictData);
	}

	@Override
	public void updateDictionaryData(DictionaryData dictData) {
        this.dataRepository.save(dictData);
	}

	@Override
	public void deleteDictionaryData(DictionaryType dictType, String keyword) {
        this.dataRepository.deleteByDictionaryTypeAndKeyword(dictType, keyword);
	}

	@Override
	public Optional<DictionaryData> getDictionaryData(DictionaryType type, String keyword) {
        return Optional.ofNullable(this.dataRepository.findByDictionaryTypeAndKeyword(type, keyword));
	}

	@Override
	public Page<DictionaryData> getDictionaryDataPage(DictionaryType type, int pageno, int size, 
                                                      String sortKey, boolean sortDesc, String filter) 
    {
        Sort sort = Sort.by(sortKey);
        if(sortDesc) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable page = PageRequest.of(pageno, size, sort);
        if(filter != null && filter.length() > 0) {
            return this.dataRepository.findByDictionaryType(type, page);
        } else {
            return this.dataRepository.findByDictionaryType(filter, type, page);
        }
    }
}

