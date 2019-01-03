package net.dunotech.venus.system.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import net.dunotech.venus.system.dto.sys.RoleInfoOutputDto;
import net.dunotech.venus.system.dto.sys.RoleInsertInputDto;
import net.dunotech.venus.system.entity.oss.OssEntity;
import net.dunotech.venus.system.entity.sys.*;
import net.dunotech.venus.system.mapper.oss.OssMapper;
import net.dunotech.venus.system.mapper.sys.*;
import net.dunotech.venus.system.utils.JwtTokenUtils;
import net.dunotech.venus.system.utils.ResultMsg;
import net.dunotech.venus.system.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermissionService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private OssMapper ossMapper;

    private static final String NO_PARENT_ID_VALUE = "0";

    public ResultMsg getUserPermission(String token){
        String username = JwtTokenUtils.getUsername(token.replace(JwtTokenUtils.TOKEN_PREFIX, StringUtils.EMPTY));
        SysUser sysUser = sysUserMapper.selectSysUserByAccount(username);
        if(sysUser==null){
            return ResultMsg.getResultMsg("获取失败,用户不存在",10000);
        }
        Map<String,Object> result = new HashMap<>();
        result.put("username",sysUser.getUsername());
        result.put("account",sysUser.getAccount());
        result.put("userId",sysUser.getId());
        result.put("avator",sysUser.getAvator());
        HashSet<String> permissionSet = new HashSet<>();
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectList(new EntityWrapper<SysUserRole>().where("del_flg = {0}","0").and("user_id = {0}",sysUser.getId()));
        for(SysUserRole sysUserRole : sysUserRoleList){
            List<SysRolePermission> sysRolePermissions = sysRolePermissionMapper.selectList(new EntityWrapper<SysRolePermission>().where("del_flg = {0}","0").and("role_id = {0}",sysUserRole.getRoleId()));
            for(SysRolePermission sysRolePermission:sysRolePermissions){
                SysPermission sysPermission = sysPermissionMapper.selectById(sysRolePermission.getPermissionId());
                if(sysPermission==null){
                    continue;
                }
                permissionSet.add(sysPermission.getIc());
            }
        }
        result.put("access",permissionSet);
        return ResultMsg.getResultMsg("获取成功",result,200);
    }

    /**
     * 新增 角色权限
     * @param roleInsertInputDto
     * @return
     */
    public ResultMsg insertRolePermission(RoleInsertInputDto roleInsertInputDto){
        SysRole sysRole = new SysRole();
        sysRole.setRemark(roleInsertInputDto.getRemark());
        sysRole.setRoleName(roleInsertInputDto.getRoleName());
        sysRoleMapper.insert(sysRole);
        //权限关联
        for(String ic:roleInsertInputDto.getPermissionList()){
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
        for(String deptId:roleInsertInputDto.getDeptList()){
            SysRoleDept sysRoleDept = new SysRoleDept();
            sysRoleDept.setDeptId(deptId);
            sysRoleDept.setRoleId(sysRole.getId());
            sysRoleDeptMapper.insert(sysRoleDept);
        }
        return ResultMsg.getResultMsg("新增成功",200);
    }

    /**
     * 更新 角色权限
     * @param roleInsertInputDto
     * @return
     */
    public ResultMsg updateRolePermission(RoleInsertInputDto roleInsertInputDto){
        String roleId = roleInsertInputDto.getId();
        SysRole sysRole = sysRoleMapper.selectById(roleId);
        sysRole.setRoleName(roleInsertInputDto.getRoleName());
        sysRole.setRemark(roleInsertInputDto.getRemark());
        sysRoleMapper.updateById(sysRole);

        //逻辑删除已有权限
        List<SysRolePermission> sysRolePermissions = sysRolePermissionMapper.selectList(new EntityWrapper<SysRolePermission>().where("del_flg = {0}","0").and("role_id = {0}",roleId));
        for(SysRolePermission sysRolePermission:sysRolePermissions){
            sysRolePermission.setDelFlg("1");
            sysRolePermissionMapper.updateById(sysRolePermission);
        }
        List<SysRoleDept> sysRoleDepts = sysRoleDeptMapper.selectList(new EntityWrapper<SysRoleDept>().where("del_flg = {0}","0").and("role_id = {0}",roleId));
        for(SysRoleDept sysRoleDept:sysRoleDepts){
            sysRoleDept.setDelFlg("1");
            sysRoleDeptMapper.updateById(sysRoleDept);
        }
        //权限关联
        for(String ic:roleInsertInputDto.getPermissionList()){
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
        for(String deptId:roleInsertInputDto.getDeptList()){
            SysRoleDept sysRoleDept = new SysRoleDept();
            sysRoleDept.setDeptId(deptId);
            sysRoleDept.setRoleId(sysRole.getId());
            sysRoleDeptMapper.insert(sysRoleDept);
        }
        return ResultMsg.getResultMsg("修改成功",200);
    }

    /**
     * 获取 角色权限数据
     * @return
     */
    public ResultMsg initRolePermissionData(String roleId){
        List<String> deptList = new ArrayList<>();
        List<String> permissionList = new ArrayList<>();
        SysRole sysRole = sysRoleMapper.selectById(roleId);
        RoleInfoOutputDto roleInfoOutputDto = new RoleInfoOutputDto();
        roleInfoOutputDto.setId(roleId);
        roleInfoOutputDto.setRoleName(sysRole.getRoleName());
        List<SysRoleDept> sysRoleDepts = sysRoleDeptMapper.selectList(new EntityWrapper<SysRoleDept>().where("del_flg = {0}","0").and("role_id = {0}",roleId));
        List<SysRolePermission> sysRolePermissions = sysRolePermissionMapper.selectList(new EntityWrapper<SysRolePermission>().where("del_flg = {0}","0").and("role_id = {0}",roleId));
        for(SysRoleDept sysRoleDept:sysRoleDepts){
            deptList.add(sysRoleDept.getDeptId());
        }
        for(SysRolePermission sysRolePermission:sysRolePermissions){
            SysPermission sysPermission = sysPermissionMapper.selectById(sysRolePermission.getPermissionId());
            permissionList.add(sysPermission.getIc());
        }
        roleInfoOutputDto.setDeptList(deptList);
        roleInfoOutputDto.setPermissionList(permissionList);
        return ResultMsg.getResultMsg("获取成功",roleInfoOutputDto,200);
    }

    /**
     * 获得部门列表数据
     * @return
     */
    public ResultMsg getDeptData(){
        List<Map<String,Object>> result = new ArrayList<>();
        List<SysDept> sysDepts = sysDeptMapper.selectList(new EntityWrapper<SysDept>().where("del_flg = {0}","0"));
        for(SysDept sysDept:sysDepts){
            if(sysDept.getParentId().equals(NO_PARENT_ID_VALUE)){
                Map<String,Object> element = new LinkedHashMap<>();
                element.put("label",sysDept.getDeptName());
                element.put("id",sysDept.getId());
                element.put("children",initDataInfo(sysDepts,sysDept.getId()));
                result.add(element);
            }
        }
        return ResultMsg.getResultMsg("获取成功",result,200);
    }

    /**
     * 递归获得部门列表
     * @param sysDepts
     * @param parentId
     * @return
     */
    private List<Map<String,Object>> initDataInfo(List<SysDept> sysDepts,String parentId){
        List<Map<String,Object>> result = new ArrayList<>();
        for(SysDept sysDept:sysDepts){
            if(sysDept.getParentId().equals(parentId)){
                Map<String,Object> element = new LinkedHashMap<>();
                element.put("label",sysDept.getDeptName());
                element.put("id",sysDept.getId());
                element.put("children",initDataInfo(sysDepts,sysDept.getId()));
                result.add(element);
            }
        }
        return result;
    }

}
