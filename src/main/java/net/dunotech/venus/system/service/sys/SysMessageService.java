package net.dunotech.venus.system.service.sys;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import net.dunotech.venus.system.config.Constants;
import net.dunotech.venus.system.entity.sys.SysMessage;
import net.dunotech.venus.system.mapper.sys.SysMessageMapper;
import net.dunotech.venus.system.utils.JacksonJsonUtil;
import net.dunotech.venus.system.utils.ResultMsg;
import net.dunotech.venus.system.utils.StringUtils;
import net.dunotech.venus.system.utils.websocket.WebSocketImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class SysMessageService {

    @Autowired
    private SysMessageMapper sysMessageMapper;

    @Autowired
    private WebSocketImpl webSocketImpl;

    /**
     * 消息新增
     * @param content
     * @param url
     * @param accounts
     * @return
     * @throws Exception
     */
    public ResultMsg messageAdd(String content,String url,String accounts) throws Exception{
        if(StringUtils.isNotEmpty(accounts)){
            String[] accountArray = accounts.split(",");
            for(String account:accountArray){
                SysMessage sysMessage = new SysMessage();
                sysMessage.setAccount(account);
                sysMessage.setContent(content);
                sysMessage.setUrl(url);
                int i = sysMessageMapper.insert(sysMessage);
                if(i==1){
                    List<SysMessage> sysMessageList = sysMessageMapper.selectList(new EntityWrapper<SysMessage>().where("del_flg = {0}",Constants.NO).and("is_read != {0}",Constants.NO));
                    webSocketImpl.sendInfo(JacksonJsonUtil.beanToJson(sysMessageList));
                }
            }
            return ResultMsg.getResultMsg("发送成功",200);
        }
        return ResultMsg.getResultMsg("发送失败，没有选择用户名",10000);
    }

    /**
     * 消息已读
     * @param id
     * @return
     */
    public ResultMsg messageRead(String id) throws Exception{
        String[] idArray = id.split(",");
        for(String msgId:idArray) {
            SysMessage sysMessage = sysMessageMapper.selectById(msgId);
            if (sysMessage != null) {
                sysMessage.setIsRead(Constants.YES);
                sysMessage.setReadTime(new Timestamp(System.currentTimeMillis()));
                int cnt = sysMessageMapper.updateById(sysMessage);
                if (cnt == 1) {
                    List<SysMessage> sysMessageList = sysMessageMapper.selectList(new EntityWrapper<SysMessage>().where("del_flg = {0}", Constants.NO).and("is_read = {0}", Constants.NO));
                    webSocketImpl.sendInfo(JacksonJsonUtil.beanToJson(sysMessageList));
                }else {
                    return ResultMsg.getResultMsg("已读失败：更新失败", msgId,10000);
                }
            }else{
                return ResultMsg.getResultMsg("已读失败：消息不存在", msgId,10000);
            }

        }
        return ResultMsg.getResultMsg("已读成功", 200);
    }

    /**
     * 未读消息数量
     * @param account
     * @return
     */
    public ResultMsg messageNotReadNum(String account){
        List<SysMessage> sysMessages = sysMessageMapper.selectList(new EntityWrapper<SysMessage>().where("del_flg = {0}",Constants.NO).and("is_read = {0}",Constants.NO).and("account = {0}",account));
        return ResultMsg.getResultMsg("获取成功",sysMessages.size(),200);
    }
}
