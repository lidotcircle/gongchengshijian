package six.daoyun.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import six.daoyun.entity.SystemParameter;

public interface SysparamService {
    void create(SystemParameter sysparam);
    Optional<String> get(String key);
    void update(String key, String value);
    void delete(String key);

    Iterable<SystemParameter> getAll();
    Page<SystemParameter> getAll(Integer start, Integer length);
}

