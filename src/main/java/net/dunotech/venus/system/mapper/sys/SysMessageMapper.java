package net.dunotech.venus.system.mapper.sys;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import net.dunotech.venus.system.entity.sys.SysMessage;

import java.util.List;
import java.util.Map;

public interface SysMessageMapper extends BaseMapper<SysMessage> {

    List<Map<String,Object>> selectSysMessageByPage(Page<?> page,Map<String,Object> param);
}
