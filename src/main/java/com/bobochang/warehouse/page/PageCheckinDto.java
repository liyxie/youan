package com.bobochang.warehouse.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author LI
 * @date 2023/9/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageCheckinDto {
    //当前页码
    private Integer pageNum;

    //每页显示行数
    private Integer pageSize;

    //总行数
    private Integer totalNum;

    //总页数
    private Integer pageCount;

    //limit函数参数一每页起始行
    private Integer limitIndex;

    //存储当前页查询到的数据的List<?>集合
    private List<?> resultList;

    private String startDate;

    private String endDate;

    private Integer userId;
    private String realName;

    @NotNull
    @Range(min = 2000, max = 3000)
    private Integer year;

    @NotNull
    @Range(min = 1, max = 12)
    private Integer month;

    //计算总页数
    public Integer getPageCount() {
        return totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
    }

    //计算limit函数参数一每页起始行
    public Integer getLimitIndex() {
        return pageSize * (pageNum - 1);
    }
}
