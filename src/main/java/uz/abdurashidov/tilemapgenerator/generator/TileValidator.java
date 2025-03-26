package uz.abdurashidov.tilemapgenerator.generator;

import org.springframework.stereotype.Component;

@Component
public class TileValidator {
    public boolean isValidTile(int[][] tile) {
        // Check if the tile is 3x3
        if (tile.length != 3 || tile[0].length != 3) {
            return false;
        }

        // Check if the center is zero
        if (tile[1][1] != 0) {
            return false;
        }

        // Check for connected zero values
        boolean[][] visited = new boolean[3][3];
        int zeroCount = 0;

        // First, count the number of zeros
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tile[i][j] == 0) {
                    zeroCount++;
                }
            }
        }

        // If only the center is zero, return true
        if (zeroCount == 1) {
            return true;
        }

        // Perform depth-first search to check connectivity
        dfs(tile, visited, 1, 1);

        // Check if all zero cells are visited
        int visitedZeroCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tile[i][j] == 0 && visited[i][j]) {
                    visitedZeroCount++;
                }
            }
        }

        // Return true if all zero cells are connected
        return visitedZeroCount == zeroCount;
    }

    private void dfs(int[][] tile, boolean[][] visited, int row, int col) {
        // Check bounds and if the cell is already visited or not zero
        if (row < 0 || row >= 3 || col < 0 || col >= 3 ||
            visited[row][col] || tile[row][col] != 0) {
            return;
        }

        // Mark as visited
        visited[row][col] = true;

        // Check adjacent cells (up, down, left, right - not diagonal)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            dfs(tile, visited, newRow, newCol);
        }
    }
}