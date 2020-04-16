package com.wj.manager.common.log.generator;

import com.wj.manager.common.Enum.ExceptionEnum;
import com.wj.manager.common.exception.CustomException;
import com.wj.manager.common.log.dict.AbstractLogDict;
import com.wj.manager.common.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Component
//@ConditionalOnMissingBean(LogCreater.class)
public class DeafaultLogCreater extends AbstractLogCreater {
    @Autowired
    private Map<String, AbstractLogDict> dictMap;

    private SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected String createMsg(Map old, Map now, String dict) {
        ToolUtil.removeEmptyMapEntry(old);
        ToolUtil.removeEmptyMapEntry(now);
        StringBuilder sb = new StringBuilder();
        AbstractLogDict logDict = dictMap.get(dict);
        Set<String> keySet = now.keySet();
        if(null == keySet || keySet.size() >= 0 ){
            throw new CustomException(ExceptionEnum.LOG_CONTEXT_NULL);
        }
        for (String key : keySet){
            //key的中文
            String keyCN = logDict.get(key);
            Object nowValue = null;
            Object oldValue = null;
            if(logDict.isSpecialField(key)){
                //特殊字段的值msg
                nowValue = logDict.converSpecialFieldValue(key, now.get(key));
                oldValue = logDict.converSpecialFieldValue(key, old.get(key));
            }else{
                nowValue = now.get(key);
                oldValue = old.get(key);
                if (nowValue instanceof Date){
                    nowValue = dateFormat.format(nowValue);
                }
                if (oldValue instanceof Date){
                    oldValue = dateFormat.format(oldValue);
                }
            }
            sb.append("["+keyCN+"]->旧值:"+oldValue+",新值:"+nowValue+";;  ");
        }

        return sb.toString();
    }

    @Override
    protected String createMsg(Map now, String dict) {
        ToolUtil.removeEmptyMapEntry(now);
        StringBuilder sb = new StringBuilder();
        AbstractLogDict logDict = dictMap.get(dict);
        Set<String> keySet = now.keySet();
        for (String key : keySet){
            //key的中文
            String keyCN = logDict.get(key);
            Object nowValue = null;
            if(logDict.isSpecialField(key)){
                //特殊字段的值msg
                nowValue = logDict.converSpecialFieldValue(key, now.get(key));
            }else{
                nowValue = now.get(key);
                if (nowValue instanceof Date){
                    nowValue = dateFormat.format(nowValue);
                }
            }
            sb.append("["+keyCN+"]->值:"+nowValue+";;  ");
        }

        return sb.toString();
    }
}
