package bg.sofia.uni.fmi.mjt.foodanalyzer.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ClientWriteHandler implements Runnable {

    private final SocketChannel socketChannel;
    private Scanner scanner;
    private final ByteBuffer buffer;
    private boolean isWorking = true;


    public ClientWriteHandler(SocketChannel socketChannel, Scanner scanner, int bufferSize) {
        this.buffer = ByteBuffer.allocate(bufferSize);
        this.scanner = scanner;
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            writeToServer();
        } catch (AsynchronousCloseException e) {
            System.out.println("Channel was closed!");

        } catch (IOException e) {
            System.out.println("Error occurred while writing to server!");
        }
    }

    public void stop() {
        isWorking = false;
    }

    private void writeToServer() throws IOException {
        while (isWorking) {
            String message = scanner.nextLine();

            buffer.clear();
            buffer.put(message.getBytes()); // buffer fill

            buffer.flip();
            socketChannel.write(buffer); // buffer drain
        }
    }
}
