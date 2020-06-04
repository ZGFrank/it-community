package com.zgf.itc.utils;

import lombok.Data;
import lombok.ToString;

/**
 * @author ZGF
 */
@Data
@ToString
public class Pagination {
    private Long currentPage;
    private Long pageSize;
}
