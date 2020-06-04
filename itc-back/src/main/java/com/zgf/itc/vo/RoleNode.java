package com.zgf.itc.vo;

import com.zgf.itc.entity.Role;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ZGF
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleNode extends Role {
    private List<RightsNode> children;
}
