package com.example.cms.service;

import com.example.cms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-19
 */
public interface CrmBannerService extends IService<CrmBanner> {

    // 查询全部banner
    List<CrmBanner> selectAllBanner();

}
