package Transport.messageDTO;

import com.alibaba.fastjson.JSON;

public class ClassInfo  {
//    private static final long serialVersionUID = -8970942815543515064L;

    private String className;
    //类名

    private String methodName;
    //方法名称

    private Class[] types;
    //参数类型

    private Object[] objects;
    //参数列表

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getTypes() {
        return types;
    }

    public void setTypes(Class[] types) {
        this.types = types;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
