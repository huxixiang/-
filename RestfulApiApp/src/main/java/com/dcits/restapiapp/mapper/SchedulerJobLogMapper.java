package com.dcits.restapiapp.mapper;

import com.dcits.restapiapp.model.SchedulerJobLogModel;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface SchedulerJobLogMapper {
    public void insertALog(SchedulerJobLogModel schedulerJobLogModel)throws Exception;

    public List<SchedulerJobLogModel>searchLogByInterfaceIdAndDate(String interfaceIdOrName, Timestamp startTime,Timestamp endTime)throws Exception;

    public void deleteLogPre15Days()throws Exception;

    public void deleteByInterfaceIds(List<String> ids)throws Exception;
}
