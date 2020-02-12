package bg.sofia.uni.fmi.mjt.foodanalyzer.client;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClientWriteHandlerTest {
    private static final int BUFF_SIZE = 1024;
    private static final int WAIT_TIME = 1000;

    @Test
    public void run() throws InterruptedException, IOException {
        var socketChannel = mock(SocketChannel.class);
        final var msg = "message\n";
        final InputStream stream = new ByteArrayInputStream(msg.getBytes());

        var scanner = new Scanner(stream);
        var handler = new ClientWriteHandler(socketChannel, scanner, BUFF_SIZE);
        new Thread(handler).start();

        Thread.sleep(WAIT_TIME);
        handler.stop();

        verify(socketChannel, atLeastOnce()).write(any(ByteBuffer.class));
    }
}