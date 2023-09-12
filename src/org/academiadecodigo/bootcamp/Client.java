package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private String nickname = null;
    private Scanner sc;
    private PrintWriter cout;
    private Socket clientSocket;


    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }


    public void start() {

        connect("localhost", 8000);

    }

    public void connect(String host, int portNum) {

        try {

            clientSocket = new Socket(host, portNum);
            cout = new PrintWriter(clientSocket.getOutputStream(), true);

            ExecutorService singleExecutor = Executors.newSingleThreadExecutor();
            singleExecutor.submit(new ServerToClient(clientSocket));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        clientToServer();

    }

    public void clientToServer() {

        Scanner scanner = new Scanner(System.in);
        while(true) {
            String message = scanner.nextLine();
            cout.println(message);
        }
    }


    public class ServerToClient implements Runnable {

        private Socket clientSocket;
        private BufferedReader bReader;


        private ServerToClient(Socket clientSocket) {
            this.clientSocket = clientSocket;

        }

        @Override
        public void run() {

            try {
                this.bReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (true) {
                    String messageReceived = bReader.readLine();
                    if (messageReceived == null) {
                        break;
                    }
                    System.out.println(messageReceived);
                }
            } catch (IOException e) {
                System.out.println("Connection ended!");
                throw new RuntimeException(e);
            }

        }
    }

}