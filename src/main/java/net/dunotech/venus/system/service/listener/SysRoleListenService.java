package net.dunotech.venus.system.service.listener;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import net.dunotech.venus.system.config.Constants;
import net.dunotech.venus.system.dto.sys.RoleInfoOutputDto;
import net.dunotech.venus.system.entity.sys.SysPermission;
import net.dunotech.venus.system.entity.sys.SysRole;
import net.dunotech.venus.system.entity.sys.SysRoleDept;
import net.dunotech.venus.system.entity.sys.SysRolePermission;
import net.dunotech.venus.system.mapper.sys.SysPermissionMapper;
import net.dunotech.venus.system.mapper.sys.SysRoleDeptMapper;
import net.dunotech.venus.system.mapper.sys.SysRolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleListenService extends IListenService{

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;

    @Override
    public void afterInsert(Map<String,Object> param){
        Map paramMap = (Map) param.get(Constants.CRUD_PARAM);
        SysRole sysRole = (SysRole)param.get(Constants.CRUD_ENTITY);
        List<String> permissionList = (List<String>)paramMap.get("permissionList");
        List<String> deptList = (List<String>)paramMap.get("deptList");
        //权限关联
        for(String ic:permissionList){
            List<SysPermission> sysPermissionList = sysPermissionMapper.selectList(new EntityWrapper<SysPermission>().where("del_flg = {0}","0").and("ic = {0}",ic));
            if(sysPermissionList.size()>0){
                SysPermission sysPermission = sysPermissionList.get(0);
                SysRolePermission sysRolePermission = new SysRolePermission();
                sysRolePermission.setPermissionId(sysPermission.getId());
                sysRolePermission.setRoleId(sysRole.getId());
                sysRolePermissionMapper.insert(sysRolePermission);
            }
        }
        //部门关联
        for(String deptId:deptList){
            SysRoleDept sysRoleDept = new SysRoleDept();
            sysRoleDept.setDeptId(deptId);
            sysRoleDept.setRoleId(sysRole.getId());
            sysRoleDeptMapper.insert(sysRoleDept);
        }
    }

    @Override
    public void afterUpdate(Map<String,Object> param){
        Map paramMap = (Map) param.get(Constants.CRUD_PARAM);
        SysRole sysRole = (SysRole)param.get(Constants.CRUD_ENTITY);
        List<String> permissionList = (List<String>)paramMap.get("permissionList");
        List<String> deptList = (List<String>)paramMap.get("deptList");

        //逻辑删除已有权限
        List<SysRolePermission> sysRolePermissions = sysRolePermissionMapper.selectList(new EntityWrapper<SysRolePermission>().where("del_flg = {0}","0").and("role_id = {0}",sysRole.getId()));
        for(SysRolePermission sysRolePermission:sysRolePermissions){
            sysRolePermission.setDelFlg("1");
            sysRolePermissionMapper.updateById(sysRolePermission);
        }
        List<SysRoleDept> sysRoleDepts = sysRoleDeptMapper.selectList(new EntityWrapper<SysRoleDept>().where("del_flg = {0}","0").and("role_id = {0}",sysRole.getId()));
        for(SysRoleDept sysRoleDept:sysRoleDepts){
            sysRoleDept.setDelFlg("1");
            sysRoleDeptMapper.updateById(sysRoleDept);
        }
        //权限关联
        for(String ic:permissionList){
            List<SysPermission> sysPermissionList = sysPermissionMapper.selectList(new EntityWrapper<SysPermission>().where("del_flg = {0}","0").and("ic = {0}",ic));
            if(sysPermissionList.size()>0){
                SysPermission sysPermission = sysPermissionList.get(0);
                SysRolePermission sysRolePermission = new SysRolePermission();
                sysRolePermission.setPermissionId(sysPermission.getId());
                sysRolePermission.setRoleId(sysRole.getId());
                sysRolePermissionMapper.insert(sysRolePermission);
            }
        }
        //部门关联
        for(String deptId:deptList){
            SysRoleDept sysRoleDept = new SysRoleDept();
            sysRoleDept.setDeptId(deptId);
            sysRoleDept.setRoleId(sysRole.getId());
            sysRoleDeptMapper.insert(sysRoleDept);
        }
    }

    @Override
    public void afterSelectId(Map<String,Object> param){
        SysRole sysRole = (SysRole)param.get(Constants.CRUD_ENTITY);
        List<String> deptList = new ArrayList<>();
        List<String> permissionList = new ArrayList<>();
        List<SysRoleDept> sysRoleDepts = sysRoleDeptMapper.selectList(new EntityWrapper<SysRoleDept>().where("del_flg = {0}","0").and("role_id = {0}",sysRole.getId()));
        List<SysRolePermission> sysRolePermissions = sysRolePermissionMapper.selectList(new EntityWrapper<SysRolePermission>().where("del_flg = {0}","0").and("role_id = {0}",sysRole.getId()));
        for(SysRoleDept sysRoleDept:sysRoleDepts){
            deptList.add(sysRoleDept.getDeptId());
        }
        for(SysRolePermission sysRolePermission:sysRolePermissions){
            SysPermission sysPermission = sysPermissionMapper.selectById(sysRolePermission.getPermissionId());
            permissionList.add(sysPermission.getIc());
        }
        sysRole.setDeptList(deptList);
        sysRole.setPermissionList(permissionList);
    }

    @Override
    public void afterSelectPage(Map<String,Object> param){}

    @Override
    public void beforeSelectPage(Map<String,Object> param){}

    @Override
    public void beforeUpdate(Map<String,Object> param){}

    @Override
    public void beforeInsert(Map<String,Object> param){}

    @Override
    public void afterDelete(Map<String,Object> param){}
}

