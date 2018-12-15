package com.axin.communication.dao;

import com.axin.communication.domain.CommunicationData;
import com.axin.communication.domain.enums.NetworkCodeTypeEnum;

public interface CommunicationDao {

    /**
     * 插入仿真后数据
     * @param data
     */
    void addData(CommunicationData data);


    /**
     * 查询仿真数据
     *
     * @return
     */
    CommunicationData queryData(NetworkCodeTypeEnum type);

}
