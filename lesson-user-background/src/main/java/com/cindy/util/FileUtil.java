package com.cindy.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author Cindy
 */
public class FileUtil {

    private static final String ENDPOINT = "oss-cn-hangzhou.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAI5tQ7CnTsKHKpPc65jj4K";
    private static final String ACCESS_KEY_SECRET = "M9vwjERjb4fldPUpAhvZVG5yeQOKSe";
    private static final String BUCKET_NAME = "cindy-lesson-bucket";
    private static final String AVATAR_OSS_DIR = "user-avatar/";
    private static final String AVATAR_LOCAL_DIR = "D:\\idea\\upload\\user-avatar";

    /**
     * 将用户头像图片上传到阿里云对象存储OSS服务中，并返回最终头像文件名
     *
     * @param avatarFile 头像图片文件实例
     * @return 头像文件的 "文件名.后缀" 地址
     */
    @SneakyThrows
    public static String uploadAvatarToOss(MultipartFile avatarFile) {
        String originalFilename = FileUtil.checkFileIsExists(avatarFile);
        String avatarFileName = FileUtil.generateRandomFileName(originalFilename);
        OSS oss = FileUtil.createOssClient();
        oss.putObject(BUCKET_NAME, AVATAR_OSS_DIR + avatarFileName, avatarFile.getInputStream());
        oss.shutdown();
        return avatarFileName;
    }

    /**
     * 从OSS删除用户头像文件
     *
     * @param avatarFileName 用户头像文件名
     */
    public static void deleteAvatarFromOss(String avatarFileName) {
        if (StringUtils.isBlank(avatarFileName)) {
            throw new RuntimeException("待删除的文件file为空");
        }
        OSS oss = FileUtil.createOssClient();
        oss.deleteObject(BUCKET_NAME, AVATAR_OSS_DIR + avatarFileName);
        oss.shutdown();
    }

    /**
     * 将图片上传到本地指定目录中
     *
     * @param avatarFile 头像图片文件实例
     * @return 本地文件完整路径
     */
    @SneakyThrows
    @SuppressWarnings("all")
    public static String uploadAvatarToLocal(MultipartFile avatarFile) {
        File destDir = FileUtil.checkDirIsExists(new File(AVATAR_LOCAL_DIR));
        String originalFilename = FileUtil.checkFileIsExists(avatarFile);
        String destFileName = FileUtil.generateRandomFileName(originalFilename);
        avatarFile.transferTo(new File(destDir, destFileName));
        return destFileName;
    }

    /**
     * 从本地删除用户的旧头像文件
     *
     * @param avatarFileName 用户头像文件名
     */
    @SuppressWarnings("all")
    public void deleteAvatarFromLocal(String avatar) {
        if (null == avatar) {
            throw new RuntimeException("文件不存在");
        }
        FileUtil.deleteFile(new File(AVATAR_LOCAL_DIR, avatar));
    }

    /**
     * 检查待上传的原始文件以及原始文件名是否为空，不为空返回原始文件名，若为空直接抛异常
     *
     * @param file 文件对象
     * @return 原始文件名
     */
    private static String checkFileIsExists(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("待上传的文件file为空");
        }
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (null == originalFilename) {
            throw new RuntimeException("待上传的原始文件名为空");
        }
        return originalFilename;
    }

    /**
     * 检查目录是否存在，不存在则创建一个返回
     *
     * @param dir 目录文件
     * @return 目录文件
     */
    @SuppressWarnings("all")
    private static File checkDirIsExists(File dir) {
        if (null == dir) {
            throw new RuntimeException("目录参数为空");
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 删除本地文件
     *
     * @param file 文件对象
     */
    @SuppressWarnings("all")
    @SneakyThrows
    private static void deleteFile(File file) {
        if (null == file) {
            throw new RuntimeException("文件参数为空");
        }
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 创建一个OSS客户端实例
     *
     * @return OSS客户端实例
     */
    private static OSS createOssClient() {
        return new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 根据原始文件名生成一个随机文件名
     *
     * @param originalFilename 原始文件名
     * @return 随机文件名，由 "UUID后10位 + 当前时间戳 + 原始文件后缀" 组成
     */
    private static String generateRandomFileName(String originalFilename) {
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        return BuildUtil.buildUuid().substring(22) + "-" + System.currentTimeMillis() + fileSuffix;
    }
}
