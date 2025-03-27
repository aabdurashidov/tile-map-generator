package uz.abdurashidov.tilemapgenerator.generator;

import org.springframework.stereotype.Component;

@Component
public class TileValidator {
    public boolean isValidTile(int[][] tile) {
        if (tile.length != 3 || tile[0].length != 3) {
            return false;
        }

        if (tile[1][1] != 0) {
            return false;
        }

        boolean[][] visited = new boolean[3][3];
        int zeroCount = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tile[i][j] == 0) {
                    zeroCount++;
                }
            }
        }

        if (zeroCount == 1) {
            return true;
        }

        dfs(tile, visited, 1, 1);

        int visitedZeroCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tile[i][j] == 0 && visited[i][j]) {
                    visitedZeroCount++;
                }
            }
        }

        return visitedZeroCount == zeroCount;
    }

    private void dfs(int[][] tile, boolean[][] visited, int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3 ||
            visited[row][col] || tile[row][col] != 0) {
            return;
        }

        visited[row][col] = true;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            dfs(tile, visited, newRow, newCol);
        }
    }
}