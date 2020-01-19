package net.stackoverflow.cms.common;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.model.entity.File;
import net.stackoverflow.cms.security.CmsUserDetails;
import net.stackoverflow.cms.service.FileService;
import net.stackoverflow.cms.util.TimeUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller基类
 *
 * @author 凉衫薄
 */
@Slf4j
public class BaseController {

    private final String telRegex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
    private final String emailRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    private final Pattern telPattern = Pattern.compile(telRegex);
    private final Pattern emailPattern = Pattern.compile(emailRegex);

    /**
     * 获取UserDetails
     *
     * @return
     */
    protected CmsUserDetails getUserDetails() {
        return (CmsUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 文件上传公共服务
     *
     * @param file
     * @return
     */
    protected File saveFile(MultipartFile file, String uploadPath, FileService fileService) throws IOException {
        String filename = file.getOriginalFilename();
        String ext = filename.substring(filename.lastIndexOf("."));
        String path = TimeUtils.pathWithDate() + UUID.randomUUID().toString() + ext;
        String absolutePath = uploadPath + path;
        java.io.File uploadFile = new java.io.File(absolutePath);
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.mkdirs();
        }
        file.transferTo(uploadFile);
        File filePO = new File(UUID.randomUUID().toString(), filename, "/upload" + path, new Date(), getUserDetails().getId());
        fileService.save(filePO);
        return filePO;
    }

    /**
     * 校验电话号码
     *
     * @param telephone
     * @return
     */
    protected boolean validateTelephone(String telephone) {
        if (telephone == null) {
            return false;
        }
        Matcher m = telPattern.matcher(telephone);
        return m.matches();
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return
     */
    protected boolean validateEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher m = emailPattern.matcher(email);
        return m.matches();
    }

    /**
     * 校验密码
     *
     * @param password
     * @return
     */
    protected boolean validatePassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        } else {
            return password.length() >= 6;
        }
    }
}
