package com.zytx.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 7965910375158391091L;
    private Integer id;
    private String phone;
    private String name;
    private Integer level;//用户等级
    private Integer type;//用户类型 0管理员 1会员 2群主
}
