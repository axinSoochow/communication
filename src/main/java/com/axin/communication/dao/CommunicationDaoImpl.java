package com.axin.communication.dao;

import com.axin.communication.domain.CommunicationData;
import com.axin.communication.domain.enums.NetworkCodeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CommunicationDaoImpl implements CommunicationDao {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void addData(CommunicationData data) {
        log.info("保存类型为{}的计算结果！", data.getNetworkCodeType());
        mongoTemplate.save(data);
    }

    @Override
    public CommunicationData queryData(NetworkCodeTypeEnum type) {
        return null;
    }
}
