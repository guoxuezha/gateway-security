package com.qianmeng.computerroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianmeng.computerroom.mapper.CompanyMapper;
import com.qianmeng.computerroom.po.Company;
import com.qianmeng.computerroom.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.qianmeng.computerroom.config.AuthExceptionConfig.companyNameRepeatException;

/**
 * @author 郭超
 * Date:2020-08-25 13:47
 * Description:
 */
@Slf4j
@Service("companyService")
@CacheConfig(cacheNames = "auth_company")
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    @Resource
    private CompanyMapper companyMapper;

    @Override
    public List<Company> selectNotDeleted(Integer companyId) {
        return companyMapper.selectNotDeleted(companyId);
    }

    @Override
    public int insert(Company company) throws Exception {
        LambdaQueryWrapper<Company> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Company::getCompanyName, company.getCompanyName());
        Company existCompany = companyMapper.selectOne(queryWrapper);
        if (existCompany != null){
            throw new Exception(companyNameRepeatException());
        }
        return companyMapper.insert(company);
    }
}
