package net.dunotech.venus.system.mapper.sys;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import net.dunotech.venus.system.dto.common.CodeElement;
import net.dunotech.venus.system.entity.sys.SysRole;

import java.util.List;
import java.util.Map;

public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<Map<String,Object>> selectSysRoleByPage(Page<?> page, Map<String, Object> param);

    List<CodeElement> selectSysRoleCodeList(Map<String, Object> param);
}
