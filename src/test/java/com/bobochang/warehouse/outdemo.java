package com.bobochang.warehouse;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Jiaju Zhuang
 */
@Getter
@Setter
@EqualsAndHashCode
public class outdemo {
    private String id;
    private double price;
    private LocalDateTime date;
    private double num;
    private double carPrice;
    private double total;
    private String remarks;
    private String name;
    private String carNum;
}
