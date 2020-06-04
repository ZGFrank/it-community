package com.zgf.itc.vo;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ZGF
 */
@Data
public class PassVo {
    private String originPass;
    private String newPass;
}
