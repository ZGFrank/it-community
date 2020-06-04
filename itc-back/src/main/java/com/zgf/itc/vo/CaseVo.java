package com.zgf.itc.vo;

import com.zgf.itc.entity.Case;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ZGF
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CaseVo extends Case {
    private String nickname;
    private String avatar;
}
