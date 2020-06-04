package com.zgf.itc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZGF
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private Integer uId;
    private Integer roleId;
    private String name;
    private String phone;
    private String email;
    private String roleName;
    private Integer available;
}
