package com.wj.manager.common.dto;

import java.io.Serializable;
import java.util.Map;

public class DictAllDto  implements Serializable{
    Map<String,Object> dicts;

    public Map<String, Object> getDicts() {
        return dicts;
    }

    public void setDicts(Map<String, Object> dicts) {
        this.dicts = dicts;
    }
}
