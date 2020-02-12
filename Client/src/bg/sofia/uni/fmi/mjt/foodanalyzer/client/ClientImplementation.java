package bg.sofia.uni.fmi.mjt.foodanalyzer.client;

import bg.sofia.uni.fmi.mjt.foodanalyzer.client.configuration.ClientConfiguration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ClientImplementation {

    private final String serverHost;
    private final int serverPort;

    public  ClientImplementation(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void start() {
        try (var socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(this.serverHost, this.serverPort));
            System.out.println("Connected to the server.");

            new Thread(new ClientReadHandler(socketChannel, ClientConfiguration.BUFF_SIZE)).start();
            new ClientWriteHandler(socketChannel, scanner, ClientConfiguration.BUFF_SIZE).run();

        } catch (IOException e) {
            System.out.println("An error occured while connecting to server!");
        } catch (Exception e) {
            System.out.println("Unknown exception occurred while running server client!");
        }
    }
}