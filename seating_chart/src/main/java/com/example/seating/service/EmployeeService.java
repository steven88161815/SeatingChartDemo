package com.example.seating.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.seating.entity.Employee;
import com.example.seating.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    public Employee getEmployeeById(String empId) {
        return employeeMapper.selectById(empId);
    }

    public List<Employee> getAllEmployees() {
        return employeeMapper.selectList(null);
    }

    @Transactional
    public void updateSeat(String empId, Long seat) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("FLOOR_SEAT_SEQ", seat);
        Employee employee1 = employeeMapper.selectOne(queryWrapper); // selectOne 用於返回單個結果
        if (employee1 != null && !employee1.getEmpId().equals(empId)) {
            UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("EMP_ID", employee1.getEmpId())
                    .set("FLOOR_SEAT_SEQ", null);
            employeeMapper.update(null, updateWrapper);
        }
        Employee employee2 = getEmployeeById(empId);
        employee2.setFloorSeatSeq(seat);
        employeeMapper.updateById(employee2);
    }

}