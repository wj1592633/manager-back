package com.wj.manager.common.util;

import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.pojo.SysUser;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ToolUtil {
    private static String saltString = "kosijf78oifa34sd5iuhg21gnbuo11e1551fwqef1gqw";

    //把明文密码和盐进行MD5加密
   /* public static String HashPassword(String password, String salt) {
        ByteSource saltSource = new Md5Hash(salt);
        return new SimpleHash(ShiroKit.hashAlgorithmName, password, saltSource, ShiroKit.hashIterations).toString();
    }*/

    public static String generatorRandomSlat() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(saltString.length()) - 1;
            if (index < 0) {
                index = 1;
            }
            sb.append(saltString.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 完善新注册的用户
     *
     * @param sysUser
     */
    public static void warpperRegisterUser(SysUser sysUser) {

        sysUser.setSalt(ToolUtil.generatorRandomSlat());

        if (StringUtils.isBlank(sysUser.getAvatar())) {
            //sysUser.setAvatar("");
        }
        if (StringUtils.isBlank(sysUser.getName())) {
            sysUser.setName(ConstantFactory.instance().DEFAULT_USER_NAME);
        }
        if (StringUtils.isBlank(sysUser.getPassword())) {
            sysUser.setPassword(ConstantFactory.instance().DEFAULT_PWD);
        }
        if (sysUser.getCreatetime() == null) {
            sysUser.setCreatetime(new Date());
        }
        if (sysUser.getSex() == null) {
            sysUser.setSex(1);
        }
        sysUser.setStatus(1);
        sysUser.setVersion(1);
        //改改sysUser.setPassword(ToolUtil.HashPassword(sysUser.getPassword(), sysUser.getSalt()));
    }

   /* public static Map<Object,Object> bean2Map(Object bean){
        if(bean == null){
            return  null;
        }
        BeanMap beanMap = new BeanMap(bean);
        beanMap.put("dasd","dasd");
        System.out.println(beanMap);
        return beanMap;
    }*/

    public static Map<String, Object> bean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return map;

    }

    /**
     * 去除map的value为null或者""的entry
     * @param map
     */
    public static void removeEmptyMapEntry(Map map){
        if(map != null && map.size() > 0){
            Set<Map.Entry> set = map.entrySet();
            Iterator<Map.Entry> iterator = set.iterator();
            while (iterator.hasNext()){
                Map.Entry next = iterator.next();
                Object value = next.getValue();
                if (null == value){
                    iterator.remove();
                }
                if(value instanceof String){
                    if(StringUtils.isBlank(((String)value))){
                        iterator.remove();
                    }
                }
            }

        }
    }

    public static <T> T map2Bean(Map<String, Object> beanMap, Class<T> beanClass) throws Exception {
        if (beanMap == null) {
            return null;
        }
        T t = beanClass.newInstance();
        BeanUtils.populate(t, beanMap);
        return t;
    }

    public static String genarateUserAvatarName(){
        return String.valueOf(new Date().getTime() + new Random().nextInt(10240));
    }
}
