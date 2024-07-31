package com.example.seating.service;

import com.example.seating.entity.SeatingChart;
import com.example.seating.mapper.SeatingChartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatingChartService {

    @Autowired
    private SeatingChartMapper seatingChartMapper;

    public SeatingChart getAllFloorsWithSeats() {
        List<SeatingChart> SeatingChartList = seatingChartMapper.selectList(null);
        return SeatingChartList.get(SeatingChartList.size() - 1);
    }

}
