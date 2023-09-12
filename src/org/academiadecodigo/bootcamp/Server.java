package org.academiadecodigo.bootcamp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

public class Server {

    private HashMap<String, Socket> players;
    private PrintWriter outMaster;
    private PrintWriter outGuesser;
    private String word;
    private Master master;
    private Guesser guesser;
    private HashSet<String> charsGuessed = new HashSet<>();
    private String[] underscores;
    private String[] charsToGuess;
    private int wrongGuesses;
    private int correctGuesses;

    private int maxWrongGuesses = 6;
    String currentStructure;
    private boolean gameOver = false;


    public static void main(String[] args) {
        Server server = new Server();
        server.start();

    }

    public void start() {
        players = new HashMap<>();
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("Server is starting....");


            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("Player connected");
                if (players.containsKey("master")) {
                    players.put("guesser", clientSocket);
                    startGame();
                    break;
                }
                players.put("master", clientSocket);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startGame() {

        try {
            outGuesser = new PrintWriter(players.get("guesser").getOutputStream(), true);
            outMaster = new PrintWriter(players.get("master").getOutputStream(), true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        guesser = new Guesser(players.get("guesser"), this);
        master = new Master(players.get("master"), this);
        master.chooseWord();

    }

    public void startRound() {
        drawGame();
        startGuessing();
    }

    public void drawGame() {
        drawCurrentStructure();
        drawUnderscores();
    }

    public void startGuessing() {
        guesser.guess();
    }

    public void checkIfCharacterExist(String charGuessed) {
        charsGuessed.add(charGuessed);
        outGuesser.println("The guess was: " + charGuessed);
        outMaster.println("The guess was: " + charGuessed);

        boolean correctGuess = false;

        for (int i = 0; i < word.length(); i++) {
            if (charGuessed.equalsIgnoreCase(charsToGuess[i])) {
                underscores[i] = charGuessed.toUpperCase();
                correctGuess = true;
                correctGuesses++;
            }
        }

        if (correctGuess) {
            outGuesser.println("Correct guess!");
            outMaster.println("Correct guess!");
            printChars();
            drawCurrentStructure();
        } else {
            outGuesser.println("Wrong guess!");
            outMaster.println("Wrong guess!");
            wrongGuesses++;
            drawCurrentStructure();
            printChars();
        }

        if (wrongGuesses == maxWrongGuesses) {
            outGuesser.println("You lost!");
            outMaster.println("You win!");
            return;
        }
        if (correctGuesses == word.length()) {
            outGuesser.println("You win!");
            outMaster.println("You lost!");

        } else {
            guesser.guess();
        }
    }


    public void drawCurrentStructure() {

        switch (wrongGuesses) {
            case 0:
                currentStructure = "\n" +
                        "+---+\n" +
                        "  |   |\n" +
                        "      |\n" +
                        "      |\n" +
                        "      |\n" +
                        "      |\n" +
                        "=========''', '''\n";
                break;

            case 1:
                currentStructure = "\n" + "+---+\n" +
                        "  |   |\n" +
                        "  O   |\n" +
                        "      |\n" +
                        "      |\n" +
                        "      |\n" +
                        "=========''', '''\n";
                break;

            case 2:
                currentStructure = "\n" + "+---+\n" +
                        "  |   |\n" +
                        "  O   |\n" +
                        "  |   |\n" +
                        "      |\n" +
                        "      |\n" +
                        "=========''', '''\n";
                break;
            case 3:
                currentStructure = "\n" + "+---+\n" +
                        "  |   |\n" +
                        "  O   |\n" +
                        " /|   |\n" +
                        "      |\n" +
                        "      |\n" +
                        "=========''', '''\n";
                break;

            case 4:
                currentStructure = "\n" + "+---+\n" +
                        "  |   |\n" +
                        "  O   |\n" +
                        " /|\\  |\n" +
                        "      |\n" +
                        "      |\n" +
                        "=========''', '''\n";
                break;
            case 5:
                currentStructure = "\n" +
                        "+---+\n" +
                        "  |   |\n" +
                        "  O   |\n" +
                        " /|\\  |\n" +
                        " /    |\n" +
                        "      |\n" +
                        "=========''', '''\n";
                break;

            case 6:
                currentStructure = "\n" +
                        " +---+\n" +
                        "  |   |\n" +
                        "  O   |\n" +
                        " /|\\  |\n" +
                        " / \\  |\n" +
                        "      |\n" +
                        "=========''']\n";
                break;
        }

        outGuesser.println(currentStructure);
        outMaster.println(currentStructure);
    }

    public void drawUnderscores() {
        underscores = new String[word.length()];

        String secretWord = "";

        for (int i = 0; i < word.length(); i++) {
            underscores[i] = "_";
            secretWord = secretWord + underscores[i];
        }
        outGuesser.println("Secret word: " + secretWord);
        outMaster.println("Secret word: " + secretWord);
    }

    public void printChars() {
        String secretWord = "";

        for (int i = 0; i < word.length(); i++) {
            secretWord = secretWord + underscores[i];
        }
        outGuesser.println("Secret word: " + secretWord);
        outMaster.println("Secret word: " + secretWord);
    }

    public void setWord(String word) {
        this.word = word;
        charsToGuess = word.split("");
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
