package com.qcxk.service.impl;

import com.qcxk.dao.TerminalPicturesDao;
import com.qcxk.model.device.TerminalPictures;
import com.qcxk.service.TerminalPicturesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.qcxk.common.Constants.DELETED;
import static com.qcxk.common.Constants.NOT_DELETED;
import static com.qcxk.util.BusinessUtil.buildTerminalPictures;

@Slf4j
@Service
public class TerminalPicturesServiceImpl implements TerminalPicturesService {
    @Autowired
    private TerminalPicturesDao dao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void batchAddTerminalPictures(List<TerminalPictures> terminalPictures) {
        if (CollectionUtils.isEmpty(terminalPictures)) {
            return;
        }

        int num = dao.batchAddTerminalPictures(terminalPictures);
        log.info("batchAdd terminalPictures success, num: {}", num);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void processNewDevicePictures(List<MultipartFile> files, String pictureUrl, String deviceNum) {
        if (files.size() == 0) {
            return;
        }

        List<TerminalPictures> terminalPictures = new ArrayList<>(files.size());

        for (MultipartFile pictureFile : files) {
            if (pictureFile.isEmpty()) {
                continue;
            }

            String fullPath = getFullPath(pictureUrl, deviceNum, pictureFile.getOriginalFilename());
            log.info("picture fullPath: {}, deviceNum: {}", fullPath, deviceNum);

            try {
                FileUtils.copyInputStreamToFile(pictureFile.getInputStream(), new File(fullPath));
                terminalPictures.add(buildTerminalPictures(fullPath, deviceNum));
            } catch (IOException e) {
                log.error("upload picture failed, picture: {}, error: {}", pictureFile.getOriginalFilename(), e);
            }
        }

        batchAddTerminalPictures(terminalPictures);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void processExistsDevicePictures(List<MultipartFile> files, String pictureUrl, String deviceNum) {
        List<TerminalPictures> existsPictures = findByDeviceNum(deviceNum, NOT_DELETED);
        List<Long> delIds = new ArrayList<>(3);

        for (TerminalPictures existsPicture : existsPictures) {
            boolean delFlag = true;
            Iterator<MultipartFile> iterator = files.iterator();
            while (iterator.hasNext()) {
                MultipartFile file = iterator.next();
                if (Objects.equals(existsPicture.getPicturePath(), getFullPath(pictureUrl, deviceNum, file.getOriginalFilename()))) {
                    // 新上传的图片里面还有此图片则不需要再次上传，而且也说明此图片没有被删除
                    // 反之，如果此图片没有被上传，没有进入到这个分支，则该图片被删除
                    iterator.remove();
                    delFlag = false;
                    break;
                }
            }

            if (delFlag) {
                delIds.add(existsPicture.getId());
            }
        }

        update2Deleted(delIds);

        processNewDevicePictures(files, pictureUrl, deviceNum);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void update2Deleted(List<Long> delIds) {
        if (CollectionUtils.isEmpty(delIds)) {
            return;
        }

        int num = dao.update2Deleted(delIds, DELETED, new Date());
        log.info("del terminalPictures success, num: {}, ids: {}", num, delIds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TerminalPictures> findByDeviceNum(String deviceNum, Integer status) {
        return dao.findByDeviceNum(deviceNum, status);
    }

    private String getFullPath(String pictureUrl, String deviceNum, String fileName) {
        return pictureUrl + File.separator + deviceNum + File.separator + fileName;
    }
}
