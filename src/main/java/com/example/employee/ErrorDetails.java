package com.example.employee;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ErrorDetails {
    Date date;
    String message;
    String details;
}
