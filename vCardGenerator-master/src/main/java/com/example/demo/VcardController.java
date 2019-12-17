package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

@Controller
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

    private String baseApiUrl = "https://adm.edu.p.lodz.pl/user/users.php?search=";

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private void xd() {
        try {
            Document employeesDocument = Jsoup.connect(baseApiUrl + urlEncode("XD")).get();
            Elements employeeList = employeesDocument.select(".user-info");
            List<Employee> result = new ArrayList<>();

            employeeList.forEach(employee -> {
                Employee modelEmployee = new Employee();

                modelEmployee.setFullName(employee.selectFirst("h3").text());
                modelEmployee.setOrganizationUnit(employee.selectFirst(".item-content").text());
                modelEmployee.setVcardLink(
                        "/vcard/" + base64Encode(modelEmployee.getFullName() + "\n" + modelEmployee.getOrganizationUnit())
                );

                result.add(modelEmployee);
            });

            model.addAttribute("employees", result);
            return "results";
        } catch (Exception e) {
            model.addAttribute("exceptionType", e.getClass().getName());
            model.addAttribute("exception", e);
            return "error";
        }
    }
    private String base64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

}
