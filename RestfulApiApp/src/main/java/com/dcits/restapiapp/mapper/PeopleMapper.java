package com.dcits.restapiapp.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PeopleMapper {

    public List<Map> search()throws Exception;
}
