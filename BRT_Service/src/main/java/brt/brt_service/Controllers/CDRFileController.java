package brt.brt_service.Controllers;

import brt.brt_service.Services.BRTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class CDRFileController {
    @Autowired
    BRTService brtService;
    private static final Logger LOGGER = Logger.getLogger(BRTService.class.getName());

    @PostMapping("/catchCDR")
    public ResponseEntity<String> catchCDRFile(@RequestPart("file") MultipartFile file, @RequestPart("fileName") String fileName) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fileName + " is empty");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            brtService.handleCDRFile(content.toString());
            LOGGER.log(Level.INFO, "OK: " + fileName + " was accepted successfully" + "\n");

            return ResponseEntity.ok(fileName + " was uploaded successfully");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()) + "\n");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process " + fileName);
        }
    }
}
