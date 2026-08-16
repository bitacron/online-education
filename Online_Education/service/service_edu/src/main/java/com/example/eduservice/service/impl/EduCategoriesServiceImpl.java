package com.example.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.eduservice.entity.EduArticle;
import com.example.eduservice.entity.EduCategories;
import com.example.eduservice.entity.vo.CategoriesInfo;
import com.example.eduservice.mapper.EduCategoriesMapper;
import com.example.eduservice.service.EduArticleService;
import com.example.eduservice.service.EduCategoriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 文章分类 服务实现类
 * </p>
 *
 * @author helloWorld
 * @since 2023-07-05
 */
@Service
public class EduCategoriesServiceImpl extends ServiceImpl<EduCategoriesMapper, EduCategories> implements EduCategoriesService {
    @Autowired
    private EduArticleService articleService;

    // 删除分类方法 删除前先判断该分类是否还有文章，有文章就无法删除
    public int deleteCategories(String id) {
        QueryWrapper<EduArticle> wrapper = new QueryWrapper<>();
        wrapper.eq("cate_id", id);
        int count = articleService.count(wrapper);
        if (count > 0) {
            return -1; // 该分类还有文章，返回-1
        }
        QueryWrapper<EduCategories> categoriesQueryWrapper = new QueryWrapper<>();
        categoriesQueryWrapper.eq("id", id);
        int delete = baseMapper.delete(categoriesQueryWrapper);
        return delete; // 返回删除的行数
    }

    // 查询所有分类数据
    @Override
    public List<CategoriesInfo> getCategoriesInfoList() {
        return baseMapper.getCategoriesInfoList();
    }
}
