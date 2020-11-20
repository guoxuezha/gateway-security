package com.qianmeng.computerroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianmeng.computerroom.po.Company;

/**
 * @author 郭超
 * Date:2020-08-25 13:48
 * Description:
 */
public interface CompanyMapper extends BaseMapper<Company> {

    /**
     * 根据用户名查询公司信息
     *
     * @param userName 用户名
     * @return 公司信息
     */
    Company selectByUserName(String userName);

}
