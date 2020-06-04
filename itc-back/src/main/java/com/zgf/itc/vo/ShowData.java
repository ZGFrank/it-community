package com.zgf.itc.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author ZGF
 */
@Data
@Builder
public class ShowData {
    private Long total;
    private List data;
}
