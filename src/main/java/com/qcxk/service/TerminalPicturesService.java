package com.qcxk.service;

import com.qcxk.model.device.TerminalPictures;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TerminalPicturesService {
    void batchAddTerminalPictures(List<TerminalPictures> terminalImages);

    void processNewDevicePictures(List<MultipartFile> files, String pictureUrl, String deviceNum);

    void processExistsDevicePictures(List<MultipartFile> files, String pictureUrl, String deviceNum);

    void update2Deleted(List<Long> delIds);

    List<TerminalPictures> findByDeviceNum(String deviceNum, Integer status);
}
