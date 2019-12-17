package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class ResultsController {

    private String baseApiUrl = "https://adm.edu.p.lodz.pl/user/users.php?search=";

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/results")
    public String searchEmployees(@RequestParam("query") String query, final Model model) {

        try {
            Document employeesDocument = Jsoup.connect(baseApiUrl + urlEncode(query)).get();
            Elements employeeList = employeesDocument.select(".user-info");
            List<Employee> result = new ArrayList<>();

            employeeList.forEach(employee -> {
                Employee modelEmployee = new Employee();

                modelEmployee.setFullName(employee.selectFirst("h3").text());
                modelEmployee.setOrganizationUnit(employee.selectFirst(".item-content").text());
                modelEmployee.setVcardLink(
                        "/vcard/" + base64Encode(modelEmployee.getFullName()+"\n"+modelEmployee.getOrganizationUnit())
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
