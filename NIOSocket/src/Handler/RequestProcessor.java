package Handler;

import Server.NIOServerSocket;
import Transport.messageDTO.ClassInfo;
import Transport.messageDTO.ResultInfo;
import com.alibaba.fastjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestProcessor {

    //构造线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void ProcessorRequest(final SelectionKey key) {
        //获得线程并执行
        executorService.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    System.out.println("start read");
                    SocketChannel readChannel = (SocketChannel) key.channel();
                    // I/O读数据操作
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int len = 0;
                    while (true) {
                        buffer.clear();
                        len = readChannel.read(buffer);
                        if (len == -1) break;
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            baos.write(buffer.get());
                        }
                    }
                    String requstMessage = new String(baos.toByteArray());
                    System.out.println("服务端收到的数据:" + requstMessage);

                    ClassInfo classInfo = JSON.parseObject(requstMessage, ClassInfo.class);
//                    if (transportMessage.getCode() != (byte) 1) {
//                        return;
//                    }
                    //方法的实现
                    InvokerHandler invoker = new InvokerHandler();
                    try {
                        Object result = invoker.remoteHandMethod(classInfo);
                        ResultInfo resultInfo = new ResultInfo();
                        resultInfo.setResult(result.toString());
                        System.out.println("resultInfo:" + resultInfo.toString());
                        //将数据添加到key中
                        key.attach(resultInfo.toString());
                        //将注册写操作添加到队列中
                        NIOServerSocket.addWriteQueen(key);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}