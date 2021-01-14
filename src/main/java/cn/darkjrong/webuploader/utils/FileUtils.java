package cn.darkjrong.webuploader.utils;

import cn.darkjrong.webuploader.enums.ResponseEnum;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.List;

/**
 *  文件操作工具类
 * @author Rong.Jia
 * @date 2021/01/14 10:43
 */
@Slf4j
public class FileUtils {

    /**
     *  删除文件
     * @param file 文件
     * @date 2020/09/24
     * @author Rong.Jia
     */
    public static boolean del(File file) {
        try {
            return FileUtil.del(file);
        }catch (Exception e) {
            log.error("del {}", e.getMessage());
        }
        return Boolean.FALSE;
    }

    /**
     *  删除文件
     * @param path 文件
     * @date 2020/09/24
     * @author Rong.Jia
     */
    public static void del(Path path) {
        try {
            Files.deleteIfExists(path);
        }catch (Exception ignored) {}
    }

    /**
     *  删除文件
     * @param file 文件
     * @date 2020/09/24
     * @author Rong.Jia
     */
    public static void del(String file) {
        if (FileUtil.exist(file)) {
            try {
                FileUtil.del(file);
            }catch (Exception ignored) {}
        }
    }

    /**
     * 把多个文件合并为一个文件
     * @param file 目标文件
     * @param chunkFiles	分片文件
     * @date 2020/09/24
     * @author Rong.Jia
     * @throws Exception 文件操作异常
     */
    public static void mergeFile (Path file, List<Path> chunkFiles) throws Exception {

        if (CollectionUtil.isEmpty(chunkFiles)) {
            log.error("The sharding file cannot be empty");
            throw new IllegalArgumentException(ResponseEnum.THE_SHARDING_FILE_CANNOT_BE_EMPTY.getMessage());
        }

        try (FileChannel fileChannel = FileChannel.open(file, EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE))){
            for (Path chunkFile : chunkFiles) {
                try(FileChannel chunkChannel = FileChannel.open(chunkFile, EnumSet.of(StandardOpenOption.READ))){
                    chunkChannel.transferTo(0, chunkChannel.size(), fileChannel);
                }
            }
        }
    }


}
