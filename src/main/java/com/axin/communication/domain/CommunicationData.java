package com.axin.communication.domain;

import com.axin.communication.domain.enums.NetworkCodeTypeEnum;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "communication")
public class CommunicationData {

    /**
     * 数据类型
     */
    protected NetworkCodeTypeEnum networkCodeType;

    /**
     * x轴参数
     */
    protected List<Double> axis;

    /**
     * y轴参数
     */
    protected Map<String,List<Double>> value;

    /**
     * 数据创建时间
     */
    protected Date creatDate;

}
