package com.example.seating.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("employee") // 指定表名
public class Employee {

    @TableId // 標識這是主鍵
    private String empId;

    private String name;

    private String email;

    private Long floorSeatSeq; // 如果有座位資訊的外鍵關係，可以設置相應的欄位

}
