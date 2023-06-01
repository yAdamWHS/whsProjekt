package test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SinkingShipsGame extends Application {
    private static final int GRID_SIZE = 10;
    private static final int CELL_SIZE = 40;
    private static final Color SHIP_COLOR = Color.DARKGRAY;
    private static final Color WATER_COLOR = Color.LIGHTBLUE;

    private Rectangle[][] playerGrid;
    private Rectangle[][] opponentGrid;
    private List<Ship> playerShips;
    private List<Ship> opponentShips;
    private boolean playerTurn;

    @Override
    public void start(Stage primaryStage) {
        playerGrid = createGrid();
        opponentGrid = createGrid();

        playerShips = new ArrayList<>();
        opponentShips = new ArrayList<>();

        // Place player's ships
        placeShips(playerShips, playerGrid);

        // Place opponent's ships randomly
        placeShipsRandomly(opponentShips, opponentGrid);

        playerTurn = true;

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        gridPane.add(playerGridPane, 0, 0);
        gridPane.add(opponentGridPane, 1, 0);

        Scene scene = new Scene(gridPane);
        primaryStage.setTitle("Sinking Ships");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Rectangle[][] createGrid() {
        Rectangle[][] grid = new Rectangle[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setFill(WATER_COLOR);
                grid[row][col] = cell;
            }
        }
        return grid;
    }

    private void placeShips(List<Ship> ships, Rectangle[][] grid) {
        // Implement your ship placement logic here
    }

    private void placeShipsRandomly(List<Ship> ships, Rectangle[][] grid) {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int size;
            if (i == 0) {
                size = 5; // battleship
            } else if (i < 3) {
                size = 4; // cruisers
            } else if (i < 6) {
                size = 3; // destroyers
            } else {
                size = 2; // submarines
            }
            Ship ship = new Ship(size);

            boolean placed = false;
            while (!placed) {
                int row = random.nextInt(GRID_SIZE);
                int col = random.nextInt(GRID_SIZE);
                boolean horizontal = random.nextBoolean();

                placed = tryPlaceShip(ship, grid, row, col, horizontal);
            }

            ships.add(ship);
        }
    }

    private boolean tryPlaceShip(Ship ship, Rectangle[][] grid, int row, int col, boolean horizontal) {
        int size = ship.getSize();
        if (horizontal && col + size <= GRID_SIZE) {
            for (int i = col; i < col + size; i++) {
                if (grid[row][i].getFill() != WATER_COLOR) {
                    return false;
                }
            }

            for (int i = col; i < col + size; i++) {
                grid[row][i].setFill(SHIP_COLOR);
            }
            ship.setCoordinates(row, col, row, col + size - 1);
            return true;
        } else if (!horizontal && row + size <= GRID_SIZE) {
            for (int i = row; i < row + size; i++) {
                if (grid[i][col].getFill() != WATER_COLOR) {
                    return false;
                }
            }

            for (int i = row; i < row + size; i++) {
                grid[i][col].setFill(SHIP_COLOR);
            }
            ship.setCoordinates(row, col, row + size - 1, col);
            return true;
        }
        return false;
    }

    private void handleGridClick(Rectangle[][] grid, int row, int col) {
        if (!playerTurn) {
            return; // Wait for the player's turn
        }

        Rectangle cell = grid[row][col];
        if (cell.getFill() == WATER_COLOR) {
            cell.setFill(Color.GRAY); // Mark as a missed shot

            playerTurn = false; // Switch turns

            // Simulate opponent's turn
            performOpponentTurn();
        } else if (cell.getFill() == SHIP_COLOR) {
            cell.setFill(Color.RED); // Mark as a hit

            boolean allShipsSunk = true;
            for (Ship ship : opponentShips) {
                if (ship.isHit(row, col)) {
                    ship.hit();
                    if (ship.isSunk()) {
                        markSunkShip(ship, grid);
                    }
                }
                if (!ship.isSunk()) {
                    allShipsSunk = false;
                }
            }

            if (allShipsSunk) {
                endGame(true); // Player wins
            } else {
                // Simulate opponent's turn
                performOpponentTurn();
            }
        }
    }

    private void markSunkShip(Ship ship, Rectangle[][] grid) {
        int startRow = ship.getStartRow();
        int startCol = ship.getStartCol();
        int endRow = ship.getEndRow();
        int endCol = ship.getEndCol();

        if (startRow == endRow) { // Horizontal ship
            for (int col = startCol; col <= endCol; col++) {
                grid[startRow][col].setFill(Color.RED); // Mark the ship as sunk
            }
        } else { // Vertical ship
            for (int row = startRow; row <= endRow; row++) {
                grid[row][startCol].setFill(Color.RED); // Mark the ship as sunk
            }
        }
    }

    private void performOpponentTurn() {
        // Implement your opponent's turn logic here
        // Randomly select a cell on the player's grid to shoot at
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(GRID_SIZE);
            col = random.nextInt(GRID_SIZE);
        } while (playerGrid[row][col].getFill() == Color.GRAY || playerGrid[row][col].getFill() == Color.RED);

        handleGridClick(playerGrid, row, col);
    }

    private void endGame(boolean playerWon) {
        String message = playerWon ? "Congratulations! You won the game." : "You lost the game.";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        System.exit(0); // Exit the program after the game ends
    }

    public static void main(String[] args) {
        launch(args);
    }
}
