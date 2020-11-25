package com.dcits.restapiapp.mapper;

import com.dcits.restapiapp.model.ParametersModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParametersMapper {

    public List<ParametersModel> searchParametersByInterfaceId(String interfaceId)throws Exception;

    public void saveParametersList(List<ParametersModel> list)throws Exception;

    public void deleteByInterfaceIds(List<String> interfaceIds)throws Exception;
}
