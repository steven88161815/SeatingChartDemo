package com.example.seating.controller;


import com.example.seating.entity.SeatingChart;
import com.example.seating.service.SeatingChartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/seating")
@CrossOrigin(origins = "http://localhost:9000")
public class SeatingController {

    @Autowired
    private SeatingChartService seatingChartService;

    @GetMapping("/all")
    public SeatingChart getSeatsRowsAndColumns() {
        return seatingChartService.getAllFloorsWithSeats();
    }

}

