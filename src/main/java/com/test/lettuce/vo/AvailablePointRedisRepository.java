package com.test.lettuce.vo;

import org.springframework.data.repository.CrudRepository;

public interface AvailablePointRedisRepository extends CrudRepository<String, String> {
}
