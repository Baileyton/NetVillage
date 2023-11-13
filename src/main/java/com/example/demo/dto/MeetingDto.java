package com.example.demo.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class MeetingDto {

    private String meetType;
    private String meetLocation;
    private String meetPlace;
    private String date;
    private String time;
    private int meetTo;
    private String meetTitle;
    private String meetDetail;
}
