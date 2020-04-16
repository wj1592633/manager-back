package com.wj.manager.common.aop;

import com.wj.manager.common.dto.ResponseResult;
import com.wj.manager.common.log.factory.LogTaskerFactory;
import com.wj.manager.common.log.vo.LogDataVo;
import com.wj.manager.common.thread.LogThreadData;
import com.wj.manager.common.util.ToolUtil;
import com.wj.manager.pojo.SysUser;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.stereotype.Component;

import javax.naming.NoPermissionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * spring的话xml要开启注解aop功能:<aop:sapectj-autoproxy></aop:sapectj-autoproxy>
 * aop作用的类要在spring容器中，否则不能生效
 */
//@Aspect
//@Component
public class AopTest {

    @Pointcut("execution(public * com.wj.manager.controller.SysMenuController.getTreeData(*))")
    public void pointCut(){};

    //execution(* *(..)) && execution(* com.xx*(..))同时满足2次表达式,
    //execution(* *(..)) || execution(* com.xx*(..))满足其中一个表达式,
    // !execution(* com.xx*(..))不是这个位置的就切,
   // @Before("execution(public * com.wj.manager.controller.*.*(..))")
    public void before(JoinPoint joinPoint){
        Object target = joinPoint.getTarget();//SysMenuController对象
        Class<?> aClass1 = target.getClass();//SysMenuController.class
        SourceLocation sourceLocation = joinPoint.getSourceLocation();
        Object aThis = joinPoint.getThis();
        Class<?> aClass = aThis.getClass();
        JoinPoint.StaticPart staticPart = joinPoint.getStaticPart();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature =null;
        if(signature instanceof  MethodSignature){
            methodSignature = (MethodSignature)signature;
        }
        String[] parameterNames = methodSignature.getParameterNames();
        Class[] parameterTypes = methodSignature.getParameterTypes();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> bean2Map = ToolUtil.bean2Map(args[0]);

        Class[] exceptionTypes = methodSignature.getExceptionTypes();
        Method method = methodSignature.getMethod();
        Class returnType = methodSignature.getReturnType();

        //方法名
        String name = signature.getName();
        //com.wj.manager.controller.SysMenuController
        Class declaringType = signature.getDeclaringType();
        String declaringTypeName = signature.getDeclaringTypeName();
        System.out.println("方法执行之前+++:::+++==="+args+":::"+name+":::"+declaringTypeName);
    }
    //after相当于finally，无论有无异常都会执行
   // @After("execution(public * com.wj.manager.controller.SysMenuController.getTreeData(*))")
    public void after(){
        System.out.println("方法结束后+++:::+++===");
    }
    //public 不能用*代替，要么写要不不写 ;最模糊,千万别这么切，所有的方法都会被切到execution(* *(..))
    //returning = "result"值必须和第二个参数相同，表示方法执行完成后返回值
    //@AfterReturning(returning = "result", value="execution(public * com.wj.manager..SysMenuController.*(..))")
    public void afterReturning(JoinPoint joinPoint,Object result){
        if(result instanceof ResponseResult){
            ResponseResult result2 = (ResponseResult)result;
            result2.getData();
            result2.getState();
        }
        Object[] args = joinPoint.getArgs();
        String name = joinPoint.getSignature().getName();
        Class declaringType = joinPoint.getSignature().getDeclaringType();
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        System.out.println("方法正常返回后+++:+++:::+++==="+args+":::"+name+":::"+declaringTypeName);
    }
    //throwing="ex"和上面的result相同原理
    @AfterThrowing(throwing="ex",value = "pointCut()")
    public void afterTrowing(Exception ex){
        System.out.println("方法抛出异常后+++:::+++===");
    }

    //@Around(value = "pointCut()")
    @Around("execution(public * com.wj.manager.controller.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object data = LogThreadData.instance().getData();

        System.out.println("父线程"+Thread.currentThread().getName());
       /* new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    System.out.println("子线程要开始睡10秒"+"::=="+Thread.currentThread().getName());
                    Thread.sleep(10000);
                    System.out.println("子线程睡完10秒了"+"::=="+Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(3000);
        System.out.println("父线程睡完3秒");*/



        Signature signature = joinPoint.getSignature();

        Object result =null;
        String signatureName = signature.getName();

        MethodSignature msig = null;
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) signature;

        Object target = joinPoint.getTarget();
        Class declaringType = signature.getDeclaringType();

        Method method1 = declaringType.getMethod(signatureName,msig.getParameterTypes());
        Method method = target.getClass().getMethod(signatureName,msig.getParameterTypes());

        System.out.println(method == method1);
        System.out.println(method.equals(method1));
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = msig.getParameterNames();
        Class<?>[] parameterTypes1 = method1.getParameterTypes();
        Class[] parameterTypes = msig.getParameterTypes();
        Annotation[] annotations = method.getAnnotations();
        ApiOperation api = method1.getAnnotation(ApiOperation.class);
        String value = api.value();

        String notes = api.notes(); //notes = 注意！！时间格式为：2019-03-23 10:25:52
        new Thread(()->{
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("notes测试线程"+Thread.currentThread().getName());
            System.out.println("notes测试::"+(notes == null));
            System.out.println("notes测试::"+(notes));
        }).start();



        try {
           // System.out.println("方法执行之前+++:::+++===");

           /* new Thread(()->{
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("notes测试::"+(notes == null));
                System.out.println("notes测试::"+(notes));
            }).start();*/

         /*
        // 这样就可以实现权限的判断
         boolean result = PermissionCheck.check(permissions);
            if (result) {
                return point.proceed();
            } else {
                throw new NoPermissionException();
            }*/

            //让方法执行，必须要写，不然方法不执行。可以控制方法的执行与否
            result = joinPoint.proceed(args);
            //System.out.println("方法结束后,后置arount ---value+++:::+++==="+value);
           /* Object data = ThreadData.instance().getData();
            System.out.println(data.getClass());
            SysUser aa = ((SysUser) data);
            if(data instanceof SysUser){
                String account = ((SysUser) data).getAccount();
                System.out.println(account);
                ThreadData.release();
            }
            System.out.println("主线程::"+Thread.currentThread().getName());*/
           /* new Thread(()->{
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("aa测试::"+Thread.currentThread().getName());
                System.out.println("aa测试::"+aa.getAccount());
            }).start();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("TimerTask测试::" + Thread.currentThread().getName());
                    System.out.println("TimerTask测试::" + aa.getAccount());
                }
            };

            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
            executor.schedule(task,3000, TimeUnit.MILLISECONDS);*/

        }catch (Exception e){
           // System.out.println("方法抛出异常后+++:::+++===");
        }finally {
           // System.out.println("方法正常返回后+++:+++:::");
        }
        //方法运行的到的结果:ResponseResult。如果返回静态的，那么被切的方法的返回值都相同
        return result;
    }
}
