package com.example.demo.controller;

import com.example.demo.dto.MeetingDto;
import com.example.demo.entity.Meeting;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.MeetingService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping(value = "/meetings/new")
    public String meetingForm(Model model) {
        model.addAttribute("meetingDto", new MeetingDto());
        return "meetings/meetingForm";
    }

    @PostMapping(value = "/meetings/new")
    public String create(@Valid MeetingDto meetingDto, Model model, HttpServletRequest request, String secretKey) {
        try{
            Cookie[] cookies = request.getCookies();
            String nick = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("nick")) {
                        String jwtToken = cookie.getValue();

                        Jws<Claims> claimsJws = Jwts.parser()
                                .setSigningKey(secretKey)
                                .parseClaimsJws(jwtToken);

                        Claims claims = claimsJws.getBody();

                        nick = claims.getSubject();

                        break;
                    }
                }
            }

            if (nick == null) {
                return "member/loginForm";
            }

            String dateString = meetingDto.getDate();
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);

            String timeString = meetingDto.getTime();
            LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ISO_TIME);

            Meeting meeting = Meeting.create(meetingDto, nick);
            meeting.setDate(date);
            meeting.setTime(time);
            meetingService.save(meeting);
            log.info("meeting info : " + meeting);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "모임 등록 중 에러 발생");
            return "/meetings/meetingForm";
        }

        return "meetings/meetingsList";
    }

    @GetMapping(value = "/meetings")
    public String list(Model model) {
        List<Meeting> meetings = meetingService.findMeetings();
        model.addAttribute("meetings", meetings);
        return "meetings/meetingsList";
    }
}
