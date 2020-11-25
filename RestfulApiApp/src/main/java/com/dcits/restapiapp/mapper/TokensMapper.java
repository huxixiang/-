package com.dcits.restapiapp.mapper;

import com.dcits.restapiapp.model.TokensModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TokensMapper {
    public TokensModel searchTokenByInterfaceId(String interfaceId)throws Exception;

    public void deleteByInterfaceIds(List<String>interfaceIds)throws Exception;

    public void insertToken(Map<String,Object> tokens)throws Exception;


    public TokensModel searchTokenBySystemId(String systemId)throws Exception;



}
