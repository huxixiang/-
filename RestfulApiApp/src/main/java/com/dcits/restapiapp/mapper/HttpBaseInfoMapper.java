package com.dcits.restapiapp.mapper;

import com.dcits.restapiapp.model.HttpBaseInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
public interface HttpBaseInfoMapper {

    public HttpBaseInfoModel searchHttpBaseInfoByInterfaceId(String interfaceId)throws Exception;

    public void saveHttpBaseInfo(HttpBaseInfoModel httpBaseInfoModel)throws Exception;


    public List<HttpBaseInfoModel> searchInterfaceIdByName(String interfaceName)throws Exception;

    public List<String> searchIdsByIdOrName(String interfaceIdOrName)throws Exception;

    public void deletebyInterfaceIds(List<String> interfaceIds)throws Exception;


    public String searchNameById(String interfaceId)throws Exception;
}
