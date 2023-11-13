package com.example.demo.service;

import com.example.demo.entity.Meeting;
import com.example.demo.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public void save(Meeting meeting){
        meetingRepository.save(meeting);
    }

    public List<Meeting> findMeetings() {
        return meetingRepository.findAll();
    }
}
