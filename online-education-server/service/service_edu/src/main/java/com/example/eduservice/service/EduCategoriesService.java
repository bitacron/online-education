package com.example.eduservice.service;

import com.example.eduservice.entity.EduCategories;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.eduservice.entity.vo.CategoriesInfo;

import java.util.List;

/**
 * <p>
 * 文章分类 服务类
 * </p>
 *
 * @author helloWorld
 * @since 2023-07-05
 */
public interface EduCategoriesService extends IService<EduCategories> {

    // 删除分类方法 删除前先判断该分类是否还有文章，有文章就无法删除
    int deleteCategories(String id);
    // 查询所有分类数据
    List<CategoriesInfo> getCategoriesInfoList();
}
