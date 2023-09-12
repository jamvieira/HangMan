package org.academiadecodigo.bootcamp;


import java.io.*;
import java.net.Socket;

public class Guesser {

    Socket guesserSocket;
    Server server;
    Prompt prompt;
    BufferedReader bf;
    PrintWriter out;

    public Guesser(Socket guesserSocket, Server server) {

        this.guesserSocket = guesserSocket;
        this.server = server;
        try {
            bf = new BufferedReader(new InputStreamReader(guesserSocket.getInputStream()));
            out = new PrintWriter(guesserSocket.getOutputStream(), true);
            prompt = new Prompt(guesserSocket.getInputStream(), new PrintStream(guesserSocket.getOutputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void guess() {

        out.println("Guess a char");

        try {
            String guess = bf.readLine();

            while (guess.length() != 1 && !guess.matches("[^a-zA-z]")) {
                out.println("Invalid character, please provide only one letter character");
                guess = bf.readLine();
            }
            server.checkIfCharacterExist(guess);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
