package com.java.companyhouse.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdvisoryLockMapper {
    Boolean acquireAdvisoryLock(@Param("key") String key);
}