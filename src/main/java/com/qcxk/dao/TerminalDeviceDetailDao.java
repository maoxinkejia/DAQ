package com.qcxk.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcxk.model.device.TerminalDeviceDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TerminalDeviceDetailDao extends BaseMapper<TerminalDeviceDetail> {

    int batchAdd(List<TerminalDeviceDetail> list);

    List<TerminalDeviceDetail> findList(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
                                        @Param("deviceNum") String deviceNum, @Param("type") int valueType);
}
