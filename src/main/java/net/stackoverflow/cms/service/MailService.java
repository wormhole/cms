package net.stackoverflow.cms.service;

/**
 * 邮件发送服务接口
 *
 * @author 凉衫薄
 */
public interface MailService {

    void send(String sender, String receiver, String subject, String text);
}
