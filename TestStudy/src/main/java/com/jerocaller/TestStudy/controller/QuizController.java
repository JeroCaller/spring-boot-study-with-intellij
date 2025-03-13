package com.jerocaller.TestStudy.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuizController {

    @GetMapping("/quiz")
    public ResponseEntity<String> quiz(@RequestParam("code") int code) {

        switch (code) {
            case 1:
                return ResponseEntity.created(null).body("Created!");
            case 2:
                return ResponseEntity.badRequest().body("Bad Request!");
            default:
                return ResponseEntity.ok().body("OK!");
        }

    }

    @PostMapping("/quiz")
    public ResponseEntity<String> quiz2(@RequestBody Code code) {

        switch (code.getValue()) {
            case 1:
                return ResponseEntity.status(403).body("Forbidden!");
            default:
                return ResponseEntity.ok().body("OK!");
        }

    }

}

@Getter
@Setter
class Code {
    private int value;
}