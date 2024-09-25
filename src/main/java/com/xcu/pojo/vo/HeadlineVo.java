package com.xcu.pojo.vo;

import lombok.Data;

@Data
public class HeadlineVo {

    private Integer hid;
    private String title;
    private Integer type;
    private Integer pageViews;
    private Integer pastHours;
    private Integer publisher;

}
