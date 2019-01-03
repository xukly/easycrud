package net.dunotech.venus.system.service.sys;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import net.dunotech.venus.system.config.Constants;
import net.dunotech.venus.system.entity.common.JwtUser;
import net.dunotech.venus.system.entity.sys.SysPermissionUrl;
import net.dunotech.venus.system.entity.sys.SysRolePermission;
import net.dunotech.venus.system.entity.sys.SysUser;
import net.dunotech.venus.system.entity.sys.SysUserRole;
import net.dunotech.venus.system.mapper.sys.SysPermissionUrlMapper;
import net.dunotech.venus.system.mapper.sys.SysRolePermissionMapper;
import net.dunotech.venus.system.mapper.sys.SysUserMapper;
import net.dunotech.venus.system.mapper.sys.SysUserRoleMapper;
import net.dunotech.venus.system.utils.JwtTokenUtils;
import net.dunotech.venus.system.utils.MD5Util;
import net.dunotech.venus.system.utils.ResultMsg;
import net.dunotech.venus.system.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserService implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysPermissionUrlMapper sysPermissionUrlMapper;

    private String defaultPassword;

    @Override
    public UserDetails loadUserByUsername(String account){
        SysUser user = sysUserMapper.selectSysUserByAccount(account);
        if(user==null){
            throw new UsernameNotFoundException("用户不存在");
        }
        StringBuilder urls = new StringBuilder();
        String result = StringUtils.EMPTY;
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectList(new EntityWrapper<SysUserRole>().where("del_flg = {0}","0").and("user_id = {0}",user.getId()));
        for(SysUserRole sysUserRole:sysUserRoleList){
            List<SysRolePermission> sysRolePermissions = sysRolePermissionMapper.selectList(new EntityWrapper<SysRolePermission>().where("del_flg = {0}","0").and("role_id = {0}",sysUserRole.getRoleId()));
            for(SysRolePermission sysRolePermission:sysRolePermissions){
                String permissionId = sysRolePermission.getPermissionId();
                List<SysPermissionUrl> sysPermissionUrls = sysPermissionUrlMapper.selectList(new EntityWrapper<SysPermissionUrl>().where("del_flg = {0}","0").and("permission_id = {0}",permissionId).and("url_type = {0}","1"));
                for(SysPermissionUrl sysPermissionUrl:sysPermissionUrls){
                    urls.append(sysPermissionUrl.getUrl()).append(",");
                }
            }
        }
        if(StringUtils.isNotEmpty(urls)){
            result = urls.substring(0,urls.toString().length()-1);
        }
        return new JwtUser(user,result);
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @param token
     * @return
     */
    public ResultMsg changePassword(String oldPassword,String newPassword,String token) throws Exception{
        String account = JwtTokenUtils.getUsername(token.replace(JwtTokenUtils.TOKEN_PREFIX,StringUtils.EMPTY));
        SysUser sysUser = sysUserMapper.selectSysUserByAccount(account);
        if(sysUser!=null){
            String oldMd5Password = MD5Util.MD5(oldPassword);
            if(oldMd5Password.equals(sysUser.getPassword())){
                int cnt = updatePassword(newPassword,sysUser);
                if(cnt==1){
                    return ResultMsg.getResultMsg("修改成功",200);
                }else{
                    return ResultMsg.getResultMsg("修改失败：数据库更新失败",10000);
                }
            }else{
                return ResultMsg.getResultMsg("修改失败：旧密码不匹配",10000);
            }
        }else{
            return ResultMsg.getResultMsg("修改失败：当前用户不存在",10000);
        }

    }

    /**
     * 重置密码
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultMsg resetPassword(String userId) throws Exception{
        SysUser sysUser = sysUserMapper.selectById(userId);
        int cnt = updatePassword(Constants.DEFAULT_PASSWORD,sysUser);
        return cnt==1?ResultMsg.getResultMsg("重置成功",200):ResultMsg.getResultMsg("重置失败：数据库更新失败",10000);
    }

    private int updatePassword(String newPassword,SysUser sysUser) throws Exception{
        String password = MD5Util.MD5(newPassword);
        sysUser.setPassword(password);
        return sysUserMapper.updateById(sysUser);
    }

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }
}
