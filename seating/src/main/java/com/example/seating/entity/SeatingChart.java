package com.example.seating.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("seatingchart") // 指定表名
public class SeatingChart {

    @TableId // 標識這是主鍵
    private Long floorSeatSeq;

    private Integer floorNo;

    private Integer seatNo;

    // 可以加入其他欄位和方法
}

