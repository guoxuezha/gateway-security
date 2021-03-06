package com.qianmeng.computerroom.vo;

import com.qianmeng.computerroom.po.SysFrontendMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * po到vo,前端需要显示父级pid、名称
 *
 * @author qianmeng
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysFrontendVo extends SysFrontendMenu {

    private String parentName;

}
