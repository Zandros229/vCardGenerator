package com.example.vcard;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
public class EmployeeController {

    private String baseApiUrl = "https://adm.edu.p.lodz.pl/user/users.php?search=";

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/employees")
    public List<Employee> searchEmployees(@RequestParam("name") String name) {
        List<Employee> result = new ArrayList<>();
        try {
            Document employeesDocument = Jsoup.connect(baseApiUrl + urlEncode(name)).get();
            Elements employeeList = employeesDocument.select(".user-info");


            employeeList.forEach(employee -> {
                Employee modelEmployee = new Employee();

                modelEmployee.setFullName(employee.selectFirst("h3").text());
                modelEmployee.setOrganizationUnit(employee.selectFirst(".item-content").text());
                modelEmployee.setVcardLink(
                        "https://ppkwu-vcard.herokuapp.com/vcard/" + base64Encode(modelEmployee.getFullName()+"\n"+modelEmployee.getOrganizationUnit())
                );

                result.add(modelEmployee);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String base64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

}
