package com.qianmeng.computerroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qianmeng.computerroom.po.Company;

import java.util.List;

/**
 * @author 郭超
 * Date:2020-08-25 13:47
 * Description:
 */
public interface CompanyService extends IService<Company> {

    /**
     * 查询所有未被删除的公司
     *
     * @param companyId 公司ID
     * @return 公司列表
     */
    List<Company> selectNotDeleted(Integer companyId);

    /**
     * 新增公司信息
     *
     * @param company 公司信息
     * @return 新增结果
     */
    int insert(Company company) throws Exception;
}
