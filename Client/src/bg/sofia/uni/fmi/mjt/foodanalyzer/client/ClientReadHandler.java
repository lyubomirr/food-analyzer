package bg.sofia.uni.fmi.mjt.foodanalyzer.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.SocketChannel;

public class ClientReadHandler implements Runnable {
    private final ByteBuffer buffer;
    private final SocketChannel socketChannel;
    private boolean isWorking = true;

    public ClientReadHandler(SocketChannel socketChannel, int bufferSize) {
        this.buffer = ByteBuffer.allocate(bufferSize);
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            readFromServer();
        }
        catch (AsynchronousCloseException e) {
            //Channel was closed.
            System.out.println("Channel was closed");
        }
        catch (IOException e) {
            System.out.println("Error occurred while reading from server!");
        }
    }

    public void stop() {
        isWorking = false;
    }

    private void readFromServer() throws IOException {
        while (isWorking) {
            int read = socketChannel.read(buffer); // buffer fill
            if (read < 0) {
                System.out.println("Closing connection!");
                this.socketChannel.close();
                return;
            }

            buffer.flip();

            String reply = new String(this.buffer.array(), 0, this.buffer.limit()); // buffer drain
            System.out.println(reply);

            buffer.clear();
        }
    }
}
