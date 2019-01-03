package net.dunotech.venus.system.service.listener;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import net.dunotech.venus.system.config.Constants;
import net.dunotech.venus.system.entity.sys.SysMessage;
import net.dunotech.venus.system.mapper.sys.SysMessageMapper;
import net.dunotech.venus.system.utils.JacksonJsonUtil;
import net.dunotech.venus.system.utils.websocket.WebSocketImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysMessageListenService extends IListenService {

    @Autowired
    private SysMessageMapper sysMessageMapper;

    @Autowired
    private WebSocketImpl webSocket;

    @Override
    public void afterInsert(Map<String, Object> param) throws Exception {
        List<SysMessage> sysMessageList = sysMessageMapper.selectList(new EntityWrapper<SysMessage>().where("del_flg = {0}", Constants.NO).and("is_read = {0}",Constants.NO));
        webSocket.sendInfo(JacksonJsonUtil.beanToJson(sysMessageList));
    }

    @Override
    public void afterUpdate(Map<String, Object> param) throws Exception {
        super.afterUpdate(param);
    }

    @Override
    public void afterSelectId(Map<String, Object> param) throws Exception {
        super.afterSelectId(param);
    }

    @Override
    public void afterSelectPage(Map<String, Object> param) throws Exception {
        super.afterSelectPage(param);
    }

    @Override
    public void beforeSelectPage(Map<String, Object> param) throws Exception {
        super.beforeSelectPage(param);
    }

    @Override
    public void beforeUpdate(Map<String, Object> param) throws Exception {
        super.beforeUpdate(param);
    }

    @Override
    public void beforeInsert(Map<String, Object> param) throws Exception {
        super.beforeInsert(param);
    }

    @Override
    public void afterDelete(Map<String, Object> param) throws Exception {
        super.afterDelete(param);
    }
}
