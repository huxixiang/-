package com.dcits.restapiapp.mapper;

import com.dcits.restapiapp.model.HeadersModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
public interface HeadersMapper {

    public List<HeadersModel> searchHeadersByInterfaceId(String interfaceId)throws Exception;

    public void saveHeaderList(List<HeadersModel>list)throws Exception;

    public void deleteByInerfaceIds(List<String>interfaceIds)throws Exception;
}
