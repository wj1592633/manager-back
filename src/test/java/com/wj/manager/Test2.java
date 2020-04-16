package com.wj.manager;

import com.wj.manager.common.Enum.DictCodeEnum;
import com.wj.manager.common.constant.ConstantFactory;
import com.wj.manager.common.log.dict.AbstractLogDict;
import com.wj.manager.common.log.dict.SysUserLogDict;
import com.wj.manager.common.log.generator.DeafaultLogCreater;
import com.wj.manager.common.log.generator.LogCreater;
import com.wj.manager.common.log.vo.LogDataVo;
import com.wj.manager.common.util.ToolUtil;
import com.wj.manager.pojo.SysOperationLog;
import com.wj.manager.pojo.SysUser;
import com.wj.manager.security.vo.AuthToken;
import org.bouncycastle.jcajce.provider.keystore.BC;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Test2 {
    int c = 0;
    SysUser user = new SysUser(); //当user为锁时，内部的属性发升变化时，不影响。但是重新user = new SysUser()，影响
    //user = new SysUser();
    String a1 ="1"; //不要用字符串当锁对象。当用时，d和b其实都指向同一个锁"2",
    String b1 ="2"; //万一再别的地方也用个"2"当锁，就可能死锁
    String d1 ="2";
    @Test  // 死锁
    public void testSync() throws InterruptedException {
        new Thread(()->{
            synchronized (b1){
                System.out.println("子线程bbbbb，，开始"+c);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (a1){
                    System.out.println("子线程aaaaa，，结束"+c);
                }
            }
        }).start();
        /**
         * ynchronized的情况下,本来是死锁，但是出现异常的线程会放弃锁(不想放锁就try、catch)。子线程就会拿到锁，主线程修改c失败了，
         * 子线程读取的会是失败的数据，的对数据处理
         */
       synchronized (a1){
           Thread.sleep(3000);
           System.out.println("主线程aaaaa");
           c++;
           int i = 1/0; //s
           synchronized (b1){
               System.out.println("主线程bbbbb");
           }
       }
        /**
         * 结果
         *  子线程bbbbb，，开始0
         * 主线程aaaaa
         * 子线程aaaaa，，结束1
         * java.lang.ArithmeticException: / by zero
         */

    }

    //volatile关键字，可以使得变量在各线程间可见.定义一个变量给多个线程访问时，最后加上volatile
    volatile boolean a =true;
    int i =0;

    @Test //10条线程，每条都对i自增1w次。可以使用synchronized同步，也可以AtomicInteger，后者比前者高效，具体看下面
    public void testAtomi() throws InterruptedException {
        for(int x= 0;x<10;x++){
            new Thread(()->{
                for(int y= 0;y<1000;y++){
                    i++;
                }
            }).start();
        }
        Thread.sleep(3000);
        System.out.println(i);//本来应该是10万的，但是实际9610

    }

    //相当于创建一个初始值为0的Integer。Atomicxxxx有很多AtomicLong
   AtomicInteger atomicInteger = new AtomicInteger(0);
    @Test ////10条线程，每条都对atomicInteger自增1w次
    public void testAtomiCompare() throws InterruptedException {
        for(int x= 0;x<10;x++){
            new Thread(()->{
                for(int y= 0;y<1000;y++){
                    //if(atomicInteger.get() < 5000) //如果有了这句，AtomicInteger也不能保证原子性。这句和下句中间有可能被打断
                    atomicInteger.addAndGet(1);
                }
            }).start();
        }
        Thread.sleep(3000);
        System.out.println(atomicInteger.intValue());//都是10w
    }



    @Test
    public void testt() throws IOException {
        Thread thread1 = new Thread(()->{  //线程1
            SysUser sysUser = new SysUser();
            sysUser.setPassword("测试密码");
            new Thread(()->{ //线程1中再开一个线程
                try { Thread.sleep(3000);
                } catch (InterruptedException e) { }
                System.out.println("notes测试::"+(sysUser.getPassword()));
            }).start();
        });
        thread1.start();
        try { Thread.sleep(1000);  thread1.stop(); //强制停止线程1
            System.out.println("thread1的状态::"+thread1.getState());
        } catch (Exception e) { }
        System.in.read();
    }

@Test
    public void testda() throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    SysUser user = new SysUser();
    user.setId(11112);
    PropertyDescriptor pd = new PropertyDescriptor("id", SysUser.class);
    Method getMethod = pd.getReadMethod();
    Object o1 = getMethod.invoke(user);
    System.out.println(o1);
}

}
