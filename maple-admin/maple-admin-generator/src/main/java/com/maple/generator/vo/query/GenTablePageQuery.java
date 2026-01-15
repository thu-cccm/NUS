package com.maple.generator.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.generator.bean.GenTable;
import lombok.Data;

@Data
public class GenTablePageQuery {

    private Page<GenTable> page;

    private GenTableQuery query;
}
