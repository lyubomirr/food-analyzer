package bg.sofia.uni.fmi.mjt.foodanalyzer.client;

import bg.sofia.uni.fmi.mjt.foodanalyzer.client.configuration.ClientConfiguration;

public class Client {
    public static void main(String[] args) {
        var client = new ClientImplementation(ClientConfiguration.SERVER_HOST, ClientConfiguration.SERVER_PORT);
        client.start();
    }
}