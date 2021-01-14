package cn.darkjrong.webuploader.constants;

import java.io.File;

/**
 * 文件常量类
 * @author Rong.Jia
 * @date 2019/11/01 08:32
 */
public class FileConstant {

    public static final String USER_DIR = "user.dir";

    public static final String SEPARATOR = File.separator;

    public static final String DATA = "data";

    public static final String FILE_DIR = DATA + SEPARATOR + "tmp";

    /**
     * 操作系统的名称
     */
    public static final String SYSTEM_ENVIRONMENT = "os.name";

    /**
     * linux 系统
     */
    public static final String LINUX_SYSTEM = "Linux";

    public static final String PART_SUFFIX = ".part";

    public static final String TMP_SUFFIX = "_tmp";

    public static final String CONF_SUFFIX = ".conf";

}
