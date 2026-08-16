package com.example.cms.controller;

import com.example.cms.entity.CrmBanner;
import com.example.cms.service.CrmBannerService;
import com.example.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前台banner显示
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-19
 */
@RestController
@RequestMapping("/educms/banner")
@Api(description = "网站首页Banner列表")
//@CrossOrigin //跨域
public class BannerFrontController {

    @Autowired
    private CrmBannerService bannerService;

    // 查询全部banner
    @ApiOperation(value = "获取首页banner")
    @GetMapping("getAllBanner")
    public R getAllBanner() {
        List<CrmBanner> list = bannerService.selectAllBanner();
        return R.ok().data("list", list);
    }

}
