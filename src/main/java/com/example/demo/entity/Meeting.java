package com.example.demo.entity;


import com.example.demo.dto.MeetingDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
@Getter
@Setter
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nick;
    private String meetType;
    private String meetLocation;
    private String meetPlace;
    private LocalDate date;
    private LocalTime time;
    private int meetTo;
    private int meetPo;
    private String meetTitle;
    private String meetDetail;

    public static Meeting create(MeetingDto meetingDto, String nick) {
        Meeting meeting = new Meeting();
        meeting.setNick(nick);
        meeting.setMeetType(meetingDto.getMeetType());
        meeting.setMeetLocation(meetingDto.getMeetLocation());
        meeting.setMeetPlace(meetingDto.getMeetPlace());
        meeting.setDate(LocalDate.parse(meetingDto.getDate()));
        meeting.setTime(LocalTime.parse(meetingDto.getTime()));
        meeting.setMeetTo(meetingDto.getMeetTo());
        meeting.setMeetPo(1);
        meeting.setMeetTitle(meetingDto.getMeetTitle());
        meeting.setMeetDetail(meetingDto.getMeetDetail());
        return meeting;
    }
}
