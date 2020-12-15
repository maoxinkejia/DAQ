package com.qcxk.dao;

import com.qcxk.model.device.TerminalPictures;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TerminalPicturesDao {
    int batchAddTerminalPictures(@Param("list") List<TerminalPictures> terminalImages);

    List<TerminalPictures> findByDeviceNum(@Param("deviceNum") String deviceNum, @Param("status") Integer status);

    int update2Deleted(@Param("ids") List<Long> delIds,@Param("deleted") Integer deleted,@Param("delTime") Date delTime);
}
