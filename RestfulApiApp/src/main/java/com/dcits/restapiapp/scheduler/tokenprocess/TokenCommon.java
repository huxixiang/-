package com.dcits.restapiapp.scheduler.tokenprocess;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @desc 接口请求时获取外系统的有效token
 * @author huxx
 * @date 2020-02-20
 */
public interface TokenCommon {

    /**
     * 每次定时调度时首先获取该接口对应的有效token和最后的后效时间
     * 每次与外系统对接时需要新建获取Token类实现此接口中的方法
     * 实现思路：
     * 1、根据接口ID去数据库中查询该接口对应的token是否处于有效状态（根据effective_end_time字段进行判断）
     * 2、如果有效则使用数据库中保存的token
     * 3、如果无效则重新向外系统发送请求获取有效token并将token和有效期保存到数据库中。
     *
     * @return
     */
    public Map<String, Timestamp> getEffectiveToken();
}



