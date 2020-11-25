package com.dcits.restapiapp.mapper;

import com.dcits.restapiapp.model.SchedulerCronModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SchedulerCronMapper {
    public String searchCronPatternByInterfaceId(String interfaceId)throws Exception;

    public List<SchedulerCronModel> searchAll()throws Exception;

    public SchedulerCronModel searchSchedulerCronByInterfaceId(String interfaceId) throws Exception;


    public void updateStatusByInterfaceId(String interfaceId,String status)throws Exception;
    public void updateCronByInterfaceId(String interfaceId,String cron)throws RuntimeException;

    public void updateAllTaskStatus()throws Exception;

    public void saveScheduleCron(SchedulerCronModel schedulerCronModel)throws Exception;

    public void deleteByInterfaceIds(List<String> interfaceIds)throws Exception;


    public List<SchedulerCronModel> searchRunningTask()throws Exception;

}
