import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SudokuSolverGUI extends JFrame {
    private JTextField[][] grid;
    private JButton solveButton;
    private JButton clearButton;
    public SudokuSolverGUI() {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }
    private void initComponents() {
        grid = new JTextField[9][9];
        JPanel gridPanel = new JPanel(new GridLayout(9, 9, 2, 2));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = new JTextField(1);
                grid[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                gridPanel.add(grid[i][j]);
            }
        }
        solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudoku();
            }
        });
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearGrid();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    private void solveSudoku() {
        int[][] puzzle = new int[9][9];
        if (!getPuzzleFromInput(puzzle)) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter numbers from 1 to 9.");
            return;
        }
        if (solve(puzzle)) {
            setGridValues(puzzle);
        } else {
            JOptionPane.showMessageDialog(this, "No solution exists for the given puzzle!");
        }
    }
    private boolean getPuzzleFromInput(int[][] puzzle) {
        try {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    String input = grid[i][j].getText().trim();
                    if (!input.isEmpty()) {
                        int num = Integer.parseInt(input);
                        if (num < 1 || num > 9) {
                            return false;
                        }
                        puzzle[i][j] = num;
                    } else {
                        puzzle[i][j] = 0;
                    }
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean solve(int[][] puzzle) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (puzzle[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValidMove(puzzle, row, col, num)) {
                            puzzle[row][col] = num;
                            if (solve(puzzle)) {
                                return true;
                            }
                            puzzle[row][col] = 0; // Undo the move
                        }
                    }
                    return false; // No valid number found
                }
            }
        }
        return true; // Puzzle solved
    }

    private boolean isValidMove(int[][] puzzle, int row, int col, int num) {
        return !usedInRow(puzzle, row, num) && !usedInColumn(puzzle, col, num) && !usedInBox(puzzle, row - row % 3, col - col % 3, num);
    }
    private boolean usedInRow(int[][] puzzle, int row, int num) {
        for (int col = 0; col < 9; col++) {
            if (puzzle[row][col] == num) {
                return true;
            }
        }
        return false;
    }
    private boolean usedInColumn(int[][] puzzle, int col, int num) {
        for (int row = 0; row < 9; row++) {
            if (puzzle[row][col] == num) {
                return true;
            }
        }
        return false;
    }
    private boolean usedInBox(int[][] puzzle, int boxStartRow, int boxStartCol, int num) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (puzzle[row + boxStartRow][col + boxStartCol] == num) {
                    return true;
                }
            }
        }
        return false;
    }
    private void setGridValues(int[][] puzzle) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j].setText(String.valueOf(puzzle[i][j]));
            }
        }
    }
    private void clearGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j].setText("");
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SudokuSolverGUI().setVisible(true);
            }
        });
    }
}