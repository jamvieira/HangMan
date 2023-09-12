package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

public class Master {

    private Server server;
    Socket masterSocket;
    BufferedReader bf;
    PrintWriter out;
    String word;
    Path dictionary = Paths.get("resources/englishWords.txt");

    public Master(Socket socket, Server server) {

        this.masterSocket = socket;
        this.server = server;
    }


    public void chooseWord() {
        try {
            bf = new BufferedReader(new InputStreamReader(masterSocket.getInputStream()));
            out = new PrintWriter(masterSocket.getOutputStream(), true);
            out.println("Choose your word");
            word = bf.readLine();
            checkDictionary(word);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void checkDictionary(String word) {

        try {

            Optional<String> result = Files.lines(dictionary)
                    .map(line -> line.split(" "))
                    .flatMap(arr -> Arrays.stream(arr))
                    .filter(sentence -> sentence.equalsIgnoreCase(word))
                    .findFirst();


            if (result.isPresent()) {
                server.setWord(word);
                server.startRound();
                return;
            }
            out.println("That word is not valid!");
            chooseWord();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
