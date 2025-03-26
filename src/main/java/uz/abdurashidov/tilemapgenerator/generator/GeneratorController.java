package uz.abdurashidov.tilemapgenerator.generator;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maps")
public class GeneratorController {
    private final GeneratorService generatorService;

    @PostMapping
    public ResponseEntity<byte[]> generate(@RequestParam String colorZero,
                                           @RequestParam String colorOne,
                                           @RequestParam(required = false) Integer tileSize) {
        BufferedImage tileMap = generatorService.generate(colorZero, colorOne, tileSize);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(tileMap, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
