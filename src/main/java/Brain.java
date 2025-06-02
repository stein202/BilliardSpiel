package main.java;

import main.java.misc.GameState;
import main.java.misc.Turn;
import main.java.ui.BilliardBall;

import java.util.ArrayList;

public class Brain {
    public static Turn turn;
    public static GameState gameState;

    private static int playerCount = 2;
    private static int currentPlayer = 0;

    private static boolean blackPotted = false;
    private static int[] playerColor = {-1, -1};
    private static int[] pottedBallsRed = {0, 0};
    private static int[] pottedBallsBlue = {0, 0};
    private static boolean gameOver = false;
    private static int winner = -1;

    public static void switchTurn() {
        currentPlayer = (currentPlayer + 1) % playerCount;
        turn = currentPlayer == 0 ? Turn.PLAYER1 : Turn.PLAYER2;
        System.out.println("TURN WURDE GESWITCHED " + turn);
    }

    public static void registerPottedBall(BilliardBall ball) {
        if (gameState == GameState.ENDED) return;

        if (ball.id == 0){
            switchTurn();
            return;
        }

        if (ball.id == 8) {
            blackPotted = true;
            return;
        }

        if (playerColor[0] == -1 && playerColor[1] == -1) {
            if (isRed(ball.id)) {
                playerColor[currentPlayer] = 0;
                playerColor[(currentPlayer + 1) % 2] = 1;
            } else if (isBlue(ball.id)) {
                playerColor[currentPlayer] = 1;
                playerColor[(currentPlayer + 1) % 2] = 0;
            }
        }

        if (isRed(ball.id)) {
            if (playerColor[currentPlayer] == 0) pottedBallsRed[currentPlayer]++;
            else switchTurn();
        } else if (isBlue(ball.id)) {
            if (playerColor[currentPlayer] == 1) pottedBallsBlue[currentPlayer]++;
            else switchTurn();
        }
    }

    public static void checkWinCondition() {
        if (!blackPotted) return;

        boolean eligible = playerColor[currentPlayer] == 0 && pottedBallsRed[currentPlayer] == 7;
        if (playerColor[currentPlayer] == 1 && pottedBallsBlue[currentPlayer] == 7) eligible = true;

        gameOver = true;
        if (eligible) {
            winner = currentPlayer;
        } else {
            winner = (currentPlayer + 1) % 2;
        }
    }

    public static boolean isRed(int id) {
        return id >= 1 && id <= 7;
    }

    public static boolean isBlue(int id) {
        return id >= 9 && id <= 15;
    }
}
