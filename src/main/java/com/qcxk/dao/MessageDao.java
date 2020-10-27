package com.qcxk.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.model.message.Message;
import com.qcxk.model.message.OriginalData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MessageDao extends BaseMapper<Message> {

    int addOriginalData(@Param("originalData") String originalMessage, @Param("deviceNum") String deviceNum, @Param("createTime") Date date);

    List<OriginalData> findOriginalDataList(TerminalDeviceDTO dto);
}