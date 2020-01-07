package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VcardController {

    @Autowired
    private VcardService vcardService;

    @GetMapping("/vcard/{vcard}")
    public ResponseEntity<String> generateVcard(@PathVariable("vcard") String vcard) {
        String vcardFile = vcardService.fromURL(vcard);
        return ResponseEntity
                .ok()
                .header("Content-Disposition", "attachment; filename=\"VCARD.VCF\"")
                .header("Content-Type", "text/x-vcard")
                .body(vcardFile);
    }
}
