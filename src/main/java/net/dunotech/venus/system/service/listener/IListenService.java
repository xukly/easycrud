package net.dunotech.venus.system.service.listener;

import java.util.Map;

public class IListenService {

    public void afterInsert(Map<String,Object> param) throws Exception{}

    public void afterUpdate(Map<String,Object> param) throws Exception{}

    public void afterSelectId(Map<String,Object> param) throws Exception{}

    public void afterSelectPage(Map<String,Object> param) throws Exception{}

    public void beforeSelectPage(Map<String,Object> param) throws Exception{}

    public void beforeUpdate(Map<String,Object> param) throws Exception{}

    public void beforeInsert(Map<String,Object> param) throws Exception{}

    public void beforeDelete(Map<String,Object> param) throws Exception{}

    public void afterDelete(Map<String,Object> param) throws Exception{}
}
