package net.stackoverflow.cms.service;

/**
 * 邮件发送服务接口
 *
 * @author 凉衫薄
 */
public interface MailService {

    /**
     * 发送邮件
     *
     * @param sender   发送者
     * @param receiver 接收者
     * @param subject  title
     * @param text     内容
     */
    void send(String sender, String receiver, String subject, String text);
}
