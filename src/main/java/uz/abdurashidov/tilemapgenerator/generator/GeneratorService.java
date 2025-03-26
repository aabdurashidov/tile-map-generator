package uz.abdurashidov.tilemapgenerator.generator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneratorService {
    @Value("${default.tile.size}")
    private int defaultTileSize;
    private final TileValidator tileValidator;

    private List<int[][]> tiles;

    @PostConstruct
    public void init() {
        tiles = generateConstrainedTileCombinations();
    }

    public BufferedImage generate(String colorZero, String colorOne, Integer tileSize) {
        tileSize = tileSize == null ? defaultTileSize : tileSize;

        int numMatrices = tiles.size();
        int matrixSize = tiles.getFirst().length;
        int matricesPerRow = (int) Math.ceil(Math.sqrt(numMatrices));

        int imageSize = matricesPerRow * matrixSize * tileSize;

        // Create a BufferedImage with transparency
        BufferedImage tileMap = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tileMap.createGraphics();

        // Enable antialiasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calculate the actual number of tiles to draw
        int actualTilesCount = Math.min(numMatrices, matricesPerRow * matricesPerRow);

        for (int i = 0; i < actualTilesCount; i++) {
            int rowPos = (i / matricesPerRow) * (matrixSize * tileSize);
            int colPos = (i % matricesPerRow) * (matrixSize * tileSize);
            drawMatrix(g2d, tiles.get(i), colorOne, colorZero, colPos, rowPos, tileSize);
        }

        g2d.dispose();

        return tileMap;
    }


    private static void drawMatrix(Graphics2D g2d, int[][] matrix, String hexColorOne, String hexColorZero, int xOffset, int yOffset, int cellSize) {
        Color colorOne = Color.decode(hexColorOne);
        Color colorZero = Color.decode(hexColorZero);

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                g2d.setColor(matrix[row][col] == 1 ? colorOne : colorZero);
                g2d.fillRect(xOffset + col * cellSize, yOffset + row * cellSize, cellSize, cellSize);
            }
        }
    }

    public List<int[][]> generateConstrainedTileCombinations() {
        List<int[][]> validCombinations = new ArrayList<>();

        // Generate all combinations of 0 and 1 for 8 surrounding cells
        for (int a = 0; a < 2; a++) {
            for (int b = 0; b < 2; b++) {
                for (int c = 0; c < 2; c++) {
                    for (int d = 0; d < 2; d++) {
                        for (int e = 0; e < 2; e++) {
                            for (int f = 0; f < 2; f++) {
                                for (int g = 0; g < 2; g++) {
                                    for (int h = 0; h < 2; h++) {
                                        int[][] tile = new int[][]{
                                                {a, b, c},
                                                {d, 0, e},
                                                {f, g, h}
                                        };

                                        if (tileValidator.isValidTile(tile)) {
                                            validCombinations.add(tile);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return validCombinations;
    }
}
