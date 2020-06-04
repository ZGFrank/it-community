package com.zgf.itc.service;

import com.zgf.itc.entity.Report;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface ReportService extends IService<Report> {

    boolean saveReport(Report entity);
}
