package com.wj.manager.controller.warpper;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class BaseWarpper {
    private IPage<Map<String,Object>>  page = null;
    private Map<String,Object> single = null;
    private List<Map<String, Object>> mutil = null;

    public BaseWarpper(IPage<Map<String,Object>>  page){
        if(page != null && (page.getRecords() != null)) {
            this.page = page;
            this.mutil = page.getRecords();
        }
    }

    public BaseWarpper(Map<String,Object> single){
        this.single = single;
    }

    public BaseWarpper(List<Map<String, Object>> mutil){
        this.mutil = mutil;
    }


    public Object warpper(){
        if (this.single != null){
            this.wrapTheMap(this.single);
        }else if(this.mutil != null && this.mutil.size() > 0){
            Iterator<Map<String, Object>> iterator = this.mutil.iterator();
            while (iterator.hasNext()){
               this.wrapTheMap(iterator.next());
            }
        }

        if(this.page != null){
            this.page.setRecords(this.mutil);
            return this.page;
        }else {
            if(single != null){
                return this.single;
            }else {
                return this.mutil;
            }
        }

    }

    protected abstract void wrapTheMap(Map<String, Object> entity);
}
