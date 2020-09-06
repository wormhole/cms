package net.stackoverflow.cms.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 文件类型常量类
 *
 * @author 凉衫薄
 */
public class FileTypeConst {

    public static final List<String> IMAGE = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "tif");

    public static final List<String> VIDEO = Arrays.asList("mp4", "avi", "3gp", "wmv", "mpeg", "mpg", "mov");

    public static final List<String> AUDIO = Arrays.asList("mp3", "wav", "flac", "wma");

    public static final Integer T_IMAGE = 1;
    public static final Integer T_VIDEO = 2;
    public static final Integer T_AUDIO = 3;
}
