package com.dcits.restapiapp.mapper;

import com.dcits.restapiapp.model.ExternalSystemModel;
import com.dcits.restapiapp.model.ExternalSystemSchedulerLogModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DataAccessMapper {
    public List<ExternalSystemModel> selectExternalSystemMetaDataBySysIdAndBussinessType(String systemId,String bussinessType)throws Exception;


    public void insertALog(ExternalSystemSchedulerLogModel externalSystemSchedulerLogModel)throws Exception;
}
