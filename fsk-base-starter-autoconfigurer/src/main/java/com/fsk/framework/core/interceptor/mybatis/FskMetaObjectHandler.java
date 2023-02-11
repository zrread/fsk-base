package com.fsk.framework.core.interceptor.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mybatis-plus
 * 表字段自动填充
 */
@Slf4j
@Component
public class FskMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createDate", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "createCode", String.class, AutoOTC.getOperatorCode());
        this.strictInsertFill(metaObject, "createName", String.class, AutoOTC.getOperatorName());
        this.strictInsertFill(metaObject, "updateDate", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateCode", String.class, AutoOTC.getOperatorCode());
        this.strictInsertFill(metaObject, "updateName", String.class, AutoOTC.getOperatorName());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateDate", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updateCode", String.class, AutoOTC.getOperatorCode());
        this.strictUpdateFill(metaObject, "updateName", String.class, AutoOTC.getOperatorName());
    }
}
