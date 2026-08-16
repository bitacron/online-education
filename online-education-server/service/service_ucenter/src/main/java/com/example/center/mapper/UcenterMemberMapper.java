package com.example.center.mapper;

import com.example.center.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-21
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {
    // 查询某一天的注册人数
    Integer countRegisterDay(String day);
}
