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

/**
 * 写操作工具类
 */
public class ResponeProcessor {
    //构造线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void ProcessorRespone(final SelectionKey key) {
        //拿到线程并执行
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("start write");
                    // 写操作
                    SocketChannel writeChannel = (SocketChannel) key.channel();
                    //拿到客户端RequestProcessor通过attach携带传递的数据
                    String responseInfo = (String) key.attachment();
                    System.out.println("客户端发送来的数据：" + responseInfo);

                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    buffer.put(responseInfo.getBytes());
                    buffer.flip();
                    writeChannel.write(buffer);
                    writeChannel.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }
}