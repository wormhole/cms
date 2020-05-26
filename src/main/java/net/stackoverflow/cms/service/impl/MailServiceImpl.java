package net.stackoverflow.cms.service.impl;

import net.stackoverflow.cms.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 邮件服务实现类
 *
 * @author
 */
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender jms;

    @Override
    public void send(String sender, String receiver, String title, String text) {
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        mainMessage.setFrom(sender);
        mainMessage.setTo(receiver);
        mainMessage.setSubject(title);
        mainMessage.setText(text);
        jms.send(mainMessage);
    }
}
