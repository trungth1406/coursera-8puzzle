/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {


    private final Stack<Board> solutionBoards;
    private final Board initialBoard;
    private boolean isSolvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        solutionBoards = new Stack<>();
        this.initialBoard = initial;
        this.isSolvable = true;
        trySolving();
    }

    private void trySolving() {
        MinPQ<SearchNode> boardTree = new MinPQ<>();
        MinPQ<SearchNode> twinTree = new MinPQ<>();
        boardTree.insert(new SearchNode(this.initialBoard, null));
        twinTree.insert(new SearchNode(this.initialBoard.twin(), null));
        SearchNode currentNode = boardTree.delMin();
        SearchNode twinNode = twinTree.delMin();
        while (!currentNode.currentBoard.isGoal()) {

            if (twinNode.currentBoard.isGoal()) {
                isSolvable = false;
                break;
            }
            for (Board neigh : currentNode.currentBoard.neighbors()) {
                if (currentNode.previousNode == null || !neigh
                        .equals(currentNode.previousNode.currentBoard)) {
                    boardTree.insert(new SearchNode(neigh, currentNode));
                }

            }

            for (Board neigh : twinNode.currentBoard.neighbors()) {
                if (twinNode.previousNode == null || !neigh
                        .equals(twinNode.previousNode.currentBoard)) {
                    twinTree.insert(new SearchNode(neigh, twinNode));
                }
            }


            currentNode = boardTree.delMin();
            twinNode = twinTree.delMin();
        }

        if (isSolvable) {
            while (currentNode != null) {
                solutionBoards.push(currentNode.currentBoard);
                currentNode = currentNode.previousNode;
            }
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return solutionBoards.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        return solutionBoards;
    }


    private class SearchNode implements Comparable<SearchNode> {

        private Board currentBoard;
        private SearchNode previousNode;
        private int count;


        public SearchNode(Board currentBoard, SearchNode previousNode) {
            this.currentBoard = currentBoard;
            this.previousNode = previousNode;
            if (this.previousNode != null) {
                this.count += this.previousNode.count + 1;
            }
            else {
                this.count = 1;
            }
        }


        @Override
        public int compareTo(SearchNode that) {
            int priorityDiff = (this.currentBoard.manhattan() + this.count) - (
                    that.currentBoard.manhattan() + that.count);
            return priorityDiff == 0 ?
                   this.currentBoard.manhattan() - that.currentBoard.manhattan() : priorityDiff;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // Empty
    }

}
