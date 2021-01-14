package cn.darkjrong.webuploader.utils;

import cn.darkjrong.webuploader.enums.ResponseEnum;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Cleaner;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.AccessController;
import java.security.PrivilegedAction;
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

    /**
     * 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生
     * 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检 查是否还有线程在读或写
     *
     * @param mappedByteBuffer
     */
    public static void freedMappedByteBuffer(final MappedByteBuffer mappedByteBuffer) {
        try {

            if (mappedByteBuffer == null) {
                return;
            }

            mappedByteBuffer.force();
            AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                try {
                    Method cleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner");
                    cleanerMethod.setAccessible(true);
                    Cleaner cleaner = (Cleaner) cleanerMethod.invoke(mappedByteBuffer, new Object[0]);
                    cleaner.clean();
                } catch (Exception e) {
                    log.error("clean MappedByteBuffer error {}", e.getMessage());
                }
                return null;
            });

        } catch (Exception e) {
            log.error("freedMappedByteBuffer {}", e.getMessage());
        }
    }


}
