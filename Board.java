/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private int perRow;
    private int[][] initialBoard;
    private int manhattan;
    private int hamming;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this(tiles, tiles.length);
    }


    private Board(int[][] tiles, int perRow) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        this.perRow = perRow;
        this.initialBoard = tiles;

        // Calculate hamming
        int res = 0;
        for (int i = 0; i < this.perRow; i++) {
            for (int j = 0; j < this.perRow; j++) {
                int currentValue = this.initialBoard[i][j];
                if (currentValue != 0 && currentValue != i * perRow + j + 1) {
                    res += 1;
                }
            }
        }
        this.hamming = res;

        // Calculate manhattan
        int manRes = 0;
        for (int i = 0; i < perRow; i++) {
            for (int j = 0; j < perRow; j++) {
                int currentValue = this.initialBoard[i][j];
                if (currentValue != 0 && currentValue != i * perRow + j + 1) {
                    int correctPos = currentValue - 1;
                    int row = correctPos / this.perRow;
                    int col = correctPos % this.perRow;
                    int diff = Math.abs(row - i) + Math.abs(col - j);
                    manRes += diff;
                }
            }
        }
        this.manhattan = manRes;
    }


    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d", this.perRow) + "\n");

        for (int i = 0; i < this.perRow; i++) {
            for (int j = 0; j < this.perRow; j++) {
                sb.append(initialBoard[i][j] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return this.perRow;
    }

    // number of tiles out of place
    public int hamming() {
        return this.hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return this.manhattan;
    }


    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // does this board equal y?

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return perRow == board.perRow
                && Arrays.deepEquals(initialBoard, board.initialBoard);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return createNeighborBoards();
    }

    private List<Board> createNeighborBoards() {
        List<Board> neighbors = new ArrayList<>();
        for (int i = 0; i < this.perRow; i++) {
            if (!neighbors.isEmpty()) {
                break;
            }
            for (int j = 0; j < perRow; j++) {
                if (!neighbors.isEmpty()) {
                    break;
                }
                else if (this.initialBoard[i][j] == 0) {
                    if (isOnGrid(i - 1, j)) {
                        neighbors.add(
                                new Board(this.newBoardBySwappingTile(i, j, i - 1, j)));
                    }
                    if (isOnGrid(i + 1, j)) {
                        neighbors.add(new Board(this.newBoardBySwappingTile(i, j, i + 1, j)));
                    }
                    if (isOnGrid(i, j - 1)) {
                        neighbors.add(new Board(this.newBoardBySwappingTile(i, j, i, j - 1)));
                    }
                    if (isOnGrid(i, j + 1)) {
                        neighbors.add(new Board(this.newBoardBySwappingTile(i, j, i, j + 1)));
                    }
                }
            }
        }
        return neighbors;
    }

    private boolean isOnGrid(int row, int col) {
        return row >= 0 && row < this.perRow && col >= 0 && col < this.perRow;

    }

    private int[][] newBoardBySwappingTile(int zeroRow, int zeroCol, int row, int col) {
        int[][] board = cloneInitialBoard();
        swap(board, zeroRow, zeroCol, row, col);
        return board;
    }

    private int[][] cloneInitialBoard() {
        int[][] board = new int[perRow][perRow];
        for (int i = 0; i < perRow; i++) {
            board[i] = initialBoard[i].clone();
        }
        return board;
    }

    private void swap(int[][] originalBoard, int aRow, int aCol, int bRow, int bCol) {
        int temp = originalBoard[aRow][aCol];
        originalBoard[aRow][aCol] = originalBoard[bRow][bCol];
        originalBoard[bRow][bCol] = temp;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(cloneInitialBoard());
        if (twin.initialBoard[0][0] != 0 && twin.initialBoard[0][1] != 0)
            swap(twin.initialBoard, 0, 0, 0, 1);
        else
            swap(twin.initialBoard, 1, 0, 1, 1);
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            StdOut.println(initial.toString());
            StdOut.println(initial.hamming());
            StdOut.println(initial.manhattan());

            for (Board bboard : initial.neighbors()) {
                StdOut.println(bboard);
            }
        }
    }

}
