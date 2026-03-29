package com.minmvp.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private Long total;
    private List<T> list;

    public static <T> PageResult<T> from(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setList(page.getRecords());
        return result;
    }
}
