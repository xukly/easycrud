package net.dunotech.venus.system.service.listener;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import net.dunotech.venus.system.config.Constants;
import net.dunotech.venus.system.entity.sys.*;
import net.dunotech.venus.system.mapper.sys.SysRoleMapper;
import net.dunotech.venus.system.mapper.sys.SysUserMapper;
import net.dunotech.venus.system.mapper.sys.SysUserRoleMapper;
import net.dunotech.venus.system.service.exception.UserAlreadyExistException;
import net.dunotech.venus.system.utils.MD5Util;
import net.dunotech.venus.system.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysUserListenService extends IListenService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public void afterInsert(Map<String,Object> param) throws Exception{
        Map paramMap = (Map) param.get(Constants.CRUD_PARAM);
        SysUser sysUser = (SysUser)param.get(Constants.CRUD_ENTITY);
        sysUser.setPassword(MD5Util.MD5(Constants.DEFAULT_PASSWORD));
        sysUserMapper.updateById(sysUser);
        List<String> roles = (List<String>)(paramMap.get("role"));
        for(String roleId:roles){
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserId(sysUser.getId());
            sysUserRoleMapper.insert(sysUserRole);
        }
    }

    @Override
    public void afterUpdate(Map<String,Object> param){
        Map paramMap = (Map) param.get(Constants.CRUD_PARAM);
        SysUser sysUser = (SysUser)param.get(Constants.CRUD_ENTITY);
        List<String> roles = (List<String>)(paramMap.get("role"));
        //删除已有关联逻辑
        if (roles!=null && roles.size()>0) {
            List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new EntityWrapper<SysUserRole>().where("del_flg = {0}", "0").and("user_id = {0}", sysUser.getId()));
            for (SysUserRole sysUserRole : sysUserRoles) {
                sysUserRole.setDelFlg("1");
                sysUserRoleMapper.updateById(sysUserRole);
            }
            for (String roleId : roles) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(roleId);
                sysUserRole.setUserId(sysUser.getId());
                sysUserRoleMapper.insert(sysUserRole);
            }
        }
    }

    @Override
    public void afterSelectId(Map<String,Object> param){
        SysUser sysUser = (SysUser)param.get(Constants.CRUD_ENTITY);
        List<String> roleList = new ArrayList<>();
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new EntityWrapper<SysUserRole>().where("del_flg = {0}","0").and("user_id = {0}",sysUser.getId()));
        for(SysUserRole sysUserRole:sysUserRoles){
            roleList.add(sysUserRole.getRoleId());
        }
        sysUser.setRole(roleList);
    }

    @Override
    public void afterSelectPage(Map<String,Object> param){
        List list = (List)param.get(Constants.CRUD_RESULT);
        for(Object obj:list){
            Map element= (Map<String,Object>)obj;
            String userId = String.valueOf(element.get("id"));
            List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new EntityWrapper<SysUserRole>().where("del_flg = {0}","0").and("user_id = {0}",userId));
            String role = StringUtils.EMPTY;
            for(SysUserRole sysUserRole:sysUserRoles){
                SysRole sysRole = sysRoleMapper.selectById(sysUserRole.getRoleId());
                role+=sysRole.getRoleName()+",";
            }
            if(role.length()>0){
                role = role.substring(0,role.length()-1);
            }
            element.put("role",role);
        }
    }

    @Override
    public void beforeSelectPage(Map<String,Object> param){}

    @Override
    public void beforeUpdate(Map<String,Object> param){}

    @Override
    public void beforeInsert(Map<String,Object> param){
        SysUser sysUser = (SysUser)param.get(Constants.CRUD_ENTITY);
        SysUser fakeUser = sysUserMapper.selectSysUserByAccount(sysUser.getAccount());
        if(fakeUser!=null){
            throw new UserAlreadyExistException("当前用户名已存在");
        }
    }

    @Override
    public void afterDelete(Map<String,Object> param){}
}
