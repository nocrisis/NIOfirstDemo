import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class ChannelDemo {
    public static void main(String[] args)
    {
        ReadableByteChannel src = Channels.newChannel(System.in);
        WritableByteChannel dest = Channels.newChannel(System.out);
        try
        {
            copy(src, dest);
            // or copyAlt(src, dest);
        }
        catch (IOException ioe)
        {
            System.err.println("I/O error: " + ioe.getMessage());
        }
        finally
        {
            try
            {
                src.close();
                dest.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
    }
    static void copy(ReadableByteChannel src, WritableByteChannel dest)
            throws IOException
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4);
        while (src.read(buffer) != -1)
        {
            buffer.flip();
            dest.write(buffer);
            buffer.compact();
            //compact只清空已读取的数据,未被读取的数据会被移动到buffer的开始位置，写入位置则近跟着未读数据之后
        }
        buffer.flip();
        while (buffer.hasRemaining()){
            dest.write(buffer);
        }
    }
    static void copyAlt(ReadableByteChannel src, WritableByteChannel dest)
            throws IOException
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4);
        while (src.read(buffer) != -1)
        {
            buffer.flip();
            while (buffer.hasRemaining()){
                dest.write(buffer);
            }
            buffer.clear();
        }
    }
}
