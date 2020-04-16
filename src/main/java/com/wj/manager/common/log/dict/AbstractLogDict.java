package com.wj.manager.common.log.dict;

import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class AbstractLogDict {
    Logger logger = LoggerFactory.getLogger(getClass());

    private final String constantFactoryFullName= "com.wj.manager.common.constant.ConstantFactory";
    //特殊字段，多个的话用,隔开
    protected String specialField;
    protected HashMap<String, String> dictory = new HashMap<>();
    protected HashMap<String, String> specialFieldDictory = new HashMap<>();

    public AbstractLogDict(){
        put("id","主键id");
        initSpecialField();
        initDictory();
        initSpecialFieldDictory();
    }
    public boolean isSpecialField(String filedName){
        return (specialField.indexOf(filedName) > -1);
    }

    public abstract void initSpecialField();
    /**
     * 初始化字段英文名称和中文名称对应的字典
     *
     */
    public abstract void initDictory();

    /**
     * 初始化需要被包装的字段(例如:性别为1:男,2:女,需要被包装为汉字)
     *
     */
    public abstract void initSpecialFieldDictory();

    public String get(String key) {
        return this.dictory.get(key);
    }

    public void put(String key, String value) {
        this.dictory.put(key, value);
    }

    public String getspecialFieldMethodName(String key){
        return this.specialFieldDictory.get(key);
    }

    public void putSpecialFieldMethodName(String key,String methodName){
        this.specialFieldDictory.put(key,methodName);
    }

    /**
     * 转换特殊字段的值，比如用户的deptid=1,把deptid和1传进去得到"开发部"
     * @param specialField
     * @param value
     * @return
     * @throws Exception
     */
    public Object converSpecialFieldValue(String specialField,Object value){
        String methodName = getspecialFieldMethodName(specialField);
        try {
            Class clazz = Class.forName(constantFactoryFullName);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    SpringContextHolder.getBean(ConstantFactory.class);
                    Object invoke = method.invoke(SpringContextHolder.getBean(ConstantFactory.class), value);
                    return invoke;
                }
            }
        }catch (Exception e){
            logger.error(getClass().getName()+",converSpecialFieldValue方法出错:"+e.getMessage());
        }
        return null;
    }

}
