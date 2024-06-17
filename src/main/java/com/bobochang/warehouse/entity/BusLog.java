package com.bobochang.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 业务操作日志
 * @TableName bus_log
 */
@TableName(value ="bus_log")
@Data
public class BusLog implements Serializable {
    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 业务名称
     */
    private String busName;

    /**
     * 业务操作描述
     */
    private String busDescrip;

    /**
     * 操作人
     */
    private String operPerson;

    /**
     * 操作时间
     */
    private String operTime;

    /**
     * 操作来源ip
     */
    private String ipFrom;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}