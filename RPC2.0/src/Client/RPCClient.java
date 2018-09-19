package Client;


import Handler.MyResultHandler;
import MassageEntity.MassageInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RPCClient {
    @SuppressWarnings("unchecked")

    public static <T> T refer(final Object target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        MassageInfo classInfo = new MassageInfo();
                        classInfo.setClassName(target.getClass().getName());
                        classInfo.setMethodName(method.getName());
                        classInfo.setObjects(args);
                        classInfo.setTypes(method.getParameterTypes());
                        final MyResultHandler resultHandler = new MyResultHandler();
                        EventLoopGroup group = new NioEventLoopGroup();
                        try {
                            Bootstrap b = new Bootstrap();
                            b.group(group).channel(NioSocketChannel.class)
                                    .option(ChannelOption.TCP_NODELAY, true)
                                    .handler(new ChannelInitializer<SocketChannel>() {
                                        @Override
                                        public void initChannel(SocketChannel ch) throws Exception {
                                            ChannelPipeline pipeline = ch.pipeline();
                                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                                            pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                                            pipeline.addLast("encoder", new ObjectEncoder());
                                            pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                                            pipeline.addLast("handler", resultHandler);
                                        }
                                    });
                            ChannelFuture future = b.connect("localhost", 8899).sync();
                            future.channel().writeAndFlush(classInfo).sync();
                            future.channel().closeFuture().sync();
                        } finally {
                            group.shutdownGracefully();
                        }
                        return resultHandler.getResponse();
                    }
                });
    }
}
