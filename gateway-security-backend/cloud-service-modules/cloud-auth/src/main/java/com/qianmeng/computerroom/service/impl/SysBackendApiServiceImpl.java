package com.qianmeng.computerroom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianmeng.computerroom.mapper.SysBackendApiMapper;
import com.qianmeng.computerroom.po.SysBackendApi;
import com.qianmeng.computerroom.service.SysBackendApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * (SysBackendApiTable)表服务实现类
 *
 * @author qianmeng
 * @since 2020-08-12 14:31:50
 */
@Service("sysBackendApiService")
@Slf4j
public class SysBackendApiServiceImpl extends ServiceImpl<SysBackendApiMapper, SysBackendApi> implements SysBackendApiService {

}