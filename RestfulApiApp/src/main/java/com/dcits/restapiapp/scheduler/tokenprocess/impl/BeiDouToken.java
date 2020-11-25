package com.dcits.restapiapp.scheduler.tokenprocess.impl;

import com.dcits.restapiapp.scheduler.tokenprocess.TokenCommon;

import java.sql.Timestamp;
import java.util.Map;

public class BeiDouToken implements TokenCommon {

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
    @Override
    public Map<String, Timestamp> getEffectiveToken() {
        return null;
    }


    public Map<String, Timestamp> getEffectiveToken(Map<String,String>params) {
        String url = params.get("url");
        String method = params.get("method");
        String userName = params.get("userName");
        String password = params.get("password");


        return null;
    }



}
