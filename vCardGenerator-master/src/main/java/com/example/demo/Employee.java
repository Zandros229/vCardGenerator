package com.example.demo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Employee {
    private String fullName;
    private String organizationUnit;
    private String vcardLink;
}
