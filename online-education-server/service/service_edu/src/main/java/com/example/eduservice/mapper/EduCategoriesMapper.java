package com.example.eduservice.mapper;

import com.example.eduservice.entity.EduCategories;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.eduservice.entity.vo.CategoriesInfo;

import java.util.List;

/**
 * <p>
 * 文章分类 Mapper 接口
 * </p>
 *
 * @author helloWorld
 * @since 2023-07-05
 */
public interface EduCategoriesMapper extends BaseMapper<EduCategories> {
    // 查询所有分类数据
    List<CategoriesInfo> getCategoriesInfoList();
}
