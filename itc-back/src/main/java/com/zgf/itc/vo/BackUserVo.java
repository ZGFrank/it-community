package com.zgf.itc.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author ZGF
 */
@Data
@Builder
public class BackUserVo {
    private Integer uId;
    private String name;
    private String nickname;
    private String account;
    private String phone;
    private String email;
    private String classGrade;
    private Integer gender;
    private String role_name;
}
