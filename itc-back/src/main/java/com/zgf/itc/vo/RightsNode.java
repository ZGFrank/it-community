package com.zgf.itc.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author ZGF
 */
@Data
@Builder
public class RightsNode {

    private Integer id;
    private Integer pid;
    private String authName;
    private String path;
    private String icon;
    private Integer available;
    List<RightsNode> children;

}
