package com.axin.communication.config;

import com.mongodb.MongoClientOptions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * mongodb选项的一些配置
 */
@Configuration
public class MongoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.data.mongodb.options")
    public MongoClientOptionProperties mongoClientOptionProperties() {
        return new MongoClientOptionProperties();
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory,
                                                       MongoMappingContext context, BeanFactory beanFactory, CustomConversions conversions) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        // remove _class field
//    mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mappingConverter.setCustomConversions(conversions);
        return mappingConverter;
    }

    @Bean
    public MongoClientOptions mongoClientOptions() {
        MongoClientOptionProperties properties = this.mongoClientOptionProperties();
        return MongoClientOptions.builder().connectTimeout(properties.getConnectionTimeoutMs())
                .socketTimeout(properties.getReadTimeoutMs()).applicationName(properties.getClientName())
                .heartbeatConnectTimeout(properties.getHeartbeatConnectionTimeoutMs())
                .heartbeatSocketTimeout(properties.getHeartbeatReadTimeoutMs())
                .heartbeatFrequency(properties.getHeartbeatFrequencyMs())
                .minHeartbeatFrequency(properties.getMinHeartbeatFrequencyMs())
                .maxConnectionIdleTime(properties.getConnectionMaxIdleTimeMs())
                .maxConnectionLifeTime(properties.getConnectionMaxLifeTimeMs())
                .maxWaitTime(properties.getPoolMaxWaitTimeMs())
                .connectionsPerHost(properties.getConnectionsPerHost())
                .threadsAllowedToBlockForConnectionMultiplier(
                        properties.getThreadsAllowedToBlockForConnectionMultiplier())
                .minConnectionsPerHost(properties.getMinConnectionsPerHost()).build();
    }

    @Getter
    @Setter
    @Validated
    @Configuration
    public static class MongoClientOptionProperties {
        /**
         * 客户端标识，方便跟踪
         */
        @NotNull
        @Size(min = 1)
        private String clientName;
        /**
         * socket连接超时时间
         */
        @Min(value = 1)
        private int connectionTimeoutMs;
        /**
         * socket读取超时时间
         */
        @Min(value = 1)
        private int readTimeoutMs;
        /**
         * 连接池获取链接等待时间
         */
        @Min(value = 1)
        private int poolMaxWaitTimeMs;
        /**
         * 连接闲置时间
         */
        @Min(value = 1)
        private int connectionMaxIdleTimeMs;
        /**
         * 连接最多可以使用多久
         */
        @Min(value = 1)
        private int connectionMaxLifeTimeMs;
        /**
         * 心跳检测发送频率
         */
        @Min(value = 2000)
        private int heartbeatFrequencyMs;

        /**
         * 最小的心跳检测发送频率
         */
        @Min(value = 300)
        private int minHeartbeatFrequencyMs;
        /**
         * 计算允许多少个线程阻塞等待时的乘数，算法：threadsAllowedToBlockForConnectionMultiplier*connectionsPerHost
         */
        @Min(value = 1)
        private int threadsAllowedToBlockForConnectionMultiplier;
        /**
         * 心跳检测连接超时时间
         */
        @Min(value = 200)
        private int heartbeatConnectionTimeoutMs;
        /**
         * 心跳检测读取超时时间
         */
        @Min(value = 200)
        private int heartbeatReadTimeoutMs;

        /**
         * 每个host最大连接数
         */
        @Min(value = 1)
        private int connectionsPerHost;
        /**
         * 每个host的最小连接数
         */
        @Min(value = 1)
        private int minConnectionsPerHost;

    }
}