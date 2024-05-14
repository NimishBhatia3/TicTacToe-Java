import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TicTacToe extends Application {
    // Constants for the game dimensions
    private static final int BOARD_SIZE = 3;
    private static final int CELL_SIZE = 150;
    private static final int WINDOW_WIDTH = BOARD_SIZE * CELL_SIZE;
    private static final int WINDOW_HEIGHT = BOARD_SIZE * CELL_SIZE + 50;

    // Variables for game state
    private String currentPlayer = "X";
    private String[][] board = new String[BOARD_SIZE][BOARD_SIZE];
    private Text statusText;
    private Button[][] buttons = new Button[BOARD_SIZE][BOARD_SIZE];
    private Button playAgainButton;
    private int scoreX = 0;
    private int scoreO = 0;
    private Text scoreTextX;
    private Text scoreTextO;

    public void start(Stage primaryStage) {
        // Create the grid pane for the game board
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Create buttons for each cell in the grid
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Button cell = createCell(row, col);
                buttons[row][col] = cell;
                gridPane.add(cell, col, row);
            }
        }

        // Create status text and play again button
        statusText = new Text("Current player: " + currentPlayer);
        statusText.setFont(Font.font(20));

        // Create score text for player X and player O
        scoreTextX = new Text("Player X score: " + scoreX);
        scoreTextX.setFont(Font.font(20));
        scoreTextO = new Text("Player O score: " + scoreO);
        scoreTextO.setFont(Font.font(20));

        playAgainButton = new Button("Play Again");
        playAgainButton.setFont(Font.font(20));
        playAgainButton.setPrefSize(150,45);
        playAgainButton.setVisible(false);
        playAgainButton.setOnAction(event -> resetGame());

        // Create the main pane and add all components
        Pane mainPane = new Pane();
        mainPane.getChildren().addAll(gridPane, statusText, playAgainButton, scoreTextX, scoreTextO);

        // Positioning the statusText, playAgainButton, and scoreTexts
        statusText.setLayoutX(WINDOW_WIDTH / 2 - statusText.getBoundsInLocal().getWidth() / 2);
        statusText.setLayoutY(WINDOW_HEIGHT - 30);

        playAgainButton.setLayoutX(WINDOW_WIDTH / 2 - playAgainButton.getWidth() / 2);
        playAgainButton.setLayoutY(WINDOW_HEIGHT - 45);
        playAgainButton.setStyle("-fx-font: 22 arial; -fx-base: rgb(100,150,50); -fx-text-fill: rgb(255,255,255);");

        scoreTextX.setLayoutX(1);
        scoreTextX.setLayoutY(20);

        scoreTextO.setLayoutX(WINDOW_WIDTH - scoreTextO.getBoundsInLocal().getWidth() - 1);
        scoreTextO.setLayoutY(20);

        // Set grey background color
        mainPane.setStyle("-fx-background-color: rgb(192,192,192);"); // Use RGB values for a darker shade of grey


        // Create the scene and set it to the stage
        Scene scene = new Scene(mainPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Method to create a cell button
    private Button createCell(int row, int col) {
        Button cell = new Button();
        cell.setPrefSize(CELL_SIZE, CELL_SIZE);
        cell.setStyle("-fx-font-size: 70; -fx-font-weight: bold; -fx-background-color: rgb(192, 192, 192); -fx-border-color: black;"); // Apply background color and border color

        // Action for clicking a cell
        cell.setOnAction(event -> {
            if (board[row][col] == null) {
                board[row][col] = currentPlayer;
                cell.setText(currentPlayer);
                if (checkWin(row, col)) {
                    statusText.setText(currentPlayer + " wins!");
                    markWinningCells(row, col);
                    playAgainButton.setVisible(true);
                    disableAllCells();

                    // Update scores
                    if (currentPlayer.equals("X")) {
                        scoreX++;
                    } else {
                        scoreO++;
                    }
                    scoreTextX.setText("Player X score: " + scoreX);
                    scoreTextO.setText("Player O score: " + scoreO);
                } else if (isBoardFull()) {
                    statusText.setText("It's a draw!");
                    playAgainButton.setVisible(true);
                } else {
                    currentPlayer = getNextPlayer();
                    statusText.setText("Current player: " + currentPlayer);
                }
            }
        });

        return cell;
    }

    // Method to determine the next player
    private String getNextPlayer() {
        if (currentPlayer.equals("X")) {
            return "O";
        } else {
            return "X";
        }
    }

    // Method to check if a player has won
    private boolean checkWin(int row, int col) {
        String player = board[row][col];
        if (player == null) {
            return false;
        }
        // Check row
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!player.equals(board[row][i])) {
                break;
            }
            if (i == BOARD_SIZE - 1) {
                return true;
            }
        }
        // Check column
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!player.equals(board[i][col])) {
                break;
            }
            if (i == BOARD_SIZE - 1) {
                return true;
            }
        }
        // Check diagonal
        if (row == col) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (!player.equals(board[i][i])) {
                    break;
                }
                if (i == BOARD_SIZE - 1) {
                    return true;
                }
            }
        }
        // Check anti-diagonal
        if (row + col == BOARD_SIZE - 1) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (!player.equals(board[i][BOARD_SIZE - 1 - i])) {
                    break;
                }
                if (i == BOARD_SIZE - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    // Method to mark the winning cells
    private void markWinningCells(int row, int col) {
        String player = board[row][col];
        // Check row
        boolean winRow = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!player.equals(board[row][i])) {
                winRow = false;
                break;
            }
        }
        if (winRow) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                buttons[row][i].setTextFill(Color.GREEN);
            }
        }
        // Check column
        boolean winColumn = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!player.equals(board[i][col])) {
                winColumn = false;
                break;
            }
        }
        if (winColumn) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                buttons[i][col].setTextFill(Color.GREEN);
            }
        }
        // Check diagonal
        if (row == col) {
            boolean winDiagonal = true;
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (!player.equals(board[i][i])) {
                    winDiagonal = false;
                    break;
                }
            }
            if (winDiagonal) {
                for (int i = 0; i < BOARD_SIZE; i++) {
                    buttons[i][i].setTextFill(Color.GREEN);
                }
            }
        }
        // Check anti-diagonal
        if (row + col == BOARD_SIZE - 1) {
            boolean winAntiDiagonal = true;
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (!player.equals(board[i][BOARD_SIZE - 1 - i])) {
                    winAntiDiagonal = false;
                    break;
                }
            }
            if (winAntiDiagonal) {
                for (int i = 0; i < BOARD_SIZE; i++) {
                    buttons[i][BOARD_SIZE - 1 - i].setTextFill(Color.GREEN);
                }
            }
        }
    }

    // Method to check if the board is full
    private boolean isBoardFull() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    // Method to disable all cells after game over
    private void disableAllCells() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                buttons[row][col].setDisable(true);
            }
        }
    }

    // Method to reset the game
    private void resetGame() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = null;
                buttons[row][col].setText("");
                buttons[row][col].setTextFill(Color.BLACK); // Reset text color
                buttons[row][col].setDisable(false);
            }
        }
        currentPlayer = "X";
        statusText.setText("Current player: " + currentPlayer);
        playAgainButton.setVisible(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
