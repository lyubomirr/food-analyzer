package bg.sofia.uni.fmi.mjt.foodanalyzer.client;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClientReadHandlerTest {
    private static final int BUFF_SIZE = 1024;
    private static final int WAIT_TIME = 1000;

    @Test
    public void run() throws IOException, InterruptedException {
        var socketChannel = mock(SocketChannel.class);
        final var readBytes = 5;
        when(socketChannel.read(any(ByteBuffer.class))).thenReturn(readBytes);
        var handler = new ClientReadHandler(socketChannel, BUFF_SIZE);
        new Thread(handler).start();

        Thread.sleep(WAIT_TIME);
        handler.stop();

        verify(socketChannel, atLeastOnce()).read(any(ByteBuffer.class));
    }
}