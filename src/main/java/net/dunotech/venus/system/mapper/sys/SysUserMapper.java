package net.dunotech.venus.system.mapper.sys;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import net.dunotech.venus.system.dto.common.CodeElement;
import net.dunotech.venus.system.entity.sys.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据登陆账号获得用户
     * @param account
     * @return
     */
    SysUser selectSysUserByAccount(String account);

    List<Map<String,Object>> selectSysUserByPage(Page<?> page, Map<String, Object> param);

    List<CodeElement> selectSysUserCodeList(Map<String,Object> param);
}
