
package Handler;

import Transport.messageDTO.ClassInfo;
import com.alibaba.fastjson.JSON;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class InvokerHandler {

    public static ConcurrentHashMap classMap = new ConcurrentHashMap();

    public Object remoteHandMethod(ClassInfo requestClassInfo) throws Exception {
        Object interfaceClazz = null;
        if (!classMap.containsKey(requestClassInfo.getClassName())) {
            try {
                interfaceClazz = Class.forName(requestClassInfo.getClassName()).newInstance();
                classMap.put(requestClassInfo.getClassName(), interfaceClazz);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            interfaceClazz = classMap.get(requestClassInfo.getClassName());
        }
        Method method = interfaceClazz.getClass().getMethod(requestClassInfo.getMethodName(), requestClassInfo.getTypes());
        Object result = method.invoke(interfaceClazz, requestClassInfo.getObjects());
        return result;
    }
}

