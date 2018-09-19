package Client;

import API.FlyBehavior;
import API.impl.DuckFlyBehaviorImpl;
import Transport.messageDTO.ClassInfo;
import Transport.messageDTO.ResultInfo;
import com.alibaba.fastjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * NIO 客户端
 */
public class NIOClientSocket {

    public static void main(String[] args) throws IOException {
         String[] birds = {"鸡","牛","鱼","猪"};
        //使用线程模拟用户 并发访问
        for (String bird:birds) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        //1.创建SocketChannel
                        SocketChannel socketChannel = SocketChannel.open();
                        //2.连接服务器
                        socketChannel.connect(new InetSocketAddress("192.168.2.63", 8989));

                        //写数据
                        String msg = "我是客户端" + Thread.currentThread().getId();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        FlyBehavior duckFlyBehavior = new DuckFlyBehaviorImpl();
                        FlyBehavior flyBehavior = (FlyBehavior) GenerateProxyInstance(duckFlyBehavior,buffer,socketChannel);
                        String result = flyBehavior.fly(bird);
                        System.out.println("返回结果:" + result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }.start();
        }
    }

    public static <T> T GenerateProxyInstance(final Object target,ByteBuffer buffer,SocketChannel socketChannel) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        ClassInfo classInfo = new ClassInfo();
                        classInfo.setClassName(target.getClass().getName());
                        classInfo.setMethodName(method.getName());
                        classInfo.setObjects(args);
                        classInfo.setTypes(method.getParameterTypes());
                        buffer.put(classInfo.toString().getBytes());
                        buffer.flip();
                        socketChannel.write(buffer);
                        socketChannel.shutdownOutput();

                        //读取返回结果数据
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        int len = 0;
                        while (true) {
                            buffer.clear();
                            len = socketChannel.read(buffer);
                            if (len == -1)
                                break;
                            buffer.flip();
                            while (buffer.hasRemaining()) {
                                bos.write(buffer.get());
                            }
                        }
                        String responseMessage = new String(bos.toByteArray());
                        Object result = JSON.parseObject(responseMessage, ResultInfo.class).getResult();
                        return result;
                    }
                });
    }
}