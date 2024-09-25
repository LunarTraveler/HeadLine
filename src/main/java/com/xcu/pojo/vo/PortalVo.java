package com.xcu.pojo.vo;

import lombok.Data;

@Data
public class PortalVo {
    
    private String keyWords;
    private int type;
    private int pageNum = 1;
    private int pageSize = 10;

}