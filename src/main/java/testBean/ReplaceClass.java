package testBean;

import org.springframework.beans.factory.support.MethodReplacer;

import java.lang.reflect.Method;

/**
 * Created by zhu on 2019/3/31.
 */
public class ReplaceClass implements MethodReplacer {
    public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
        System.out.println("I am a replace method");
        return method.invoke(obj, args);
    }
}
