package six.daoyun.service.SysparamServiceImpl;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.SystemParameter;
import six.daoyun.repository.SysparamRepository;
import six.daoyun.service.SysparamService;

@Service
public class SysparamServiceImpl implements SysparamService {
	@Autowired
    private SysparamRepository repository;
    @Autowired
    private RedisTemplate<String, SystemParameter> cache;
    @Autowired
    private RedisTemplate<String, String> kvCache;

    private static String keyname(String key) {
        return "systemparam_" + key;
    }

    private void invalidateKey(String paramkey) {
        final String key = keyname(paramkey);
        if(this.kvCache.hasKey(key)) {
            this.kvCache.delete(key);
        }
    }

	@Override
	public void create(SystemParameter sysparam) {
        this.repository.save(sysparam);
	}

	@Override
	public Optional<String> get(String paramkey) {
        final String key = keyname(paramkey);
        ValueOperations<String, String> operation = this.kvCache.opsForValue();
        if(this.cache.hasKey(key)) {
            return Optional.of(operation.get(key));
        }

        SystemParameter param = this.repository.findByParameterName(paramkey);
        if(param != null) {
            operation.set(key, param.getParameterValue());
            return Optional.of(param.getParameterValue());
        }

        return Optional.empty();
	}

	@Override
	public void update(String key, String value) {
        SystemParameter param = this.repository.findByParameterName(key);
        if(param == null) {
            throw new HttpNotFound(key + " doesn't exist");
        } else {
            param.setParameterValue(value);
            this.repository.save(param);
            this.invalidateKey(key);
        }
	}

	@Override
	public void delete(String key) {
        this.repository.deleteByParameterName(key);
        this.invalidateKey(key);
	}

	@Override
	public Iterable<SystemParameter> getAll() {
        return this.repository.findAll();
	}

	@Override
	public Page<SystemParameter> getAll(Integer pageno, Integer size) {
        Pageable page = PageRequest.of(pageno, size, Sort.by("parameterName"));
        return this.repository.findAll(page);
	}
}

