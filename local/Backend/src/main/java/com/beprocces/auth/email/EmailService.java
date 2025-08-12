package com.beprocces.auth.email;

import com.beprocces.auth.model.Role;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender ;
    private final SpringTemplateEngine templateEngine;
    @Async
    public void sendEmail(
         String to ,
         String username ,
         String password ,
         String telephone,
         String function,
         List<Role> role,
         EmailTemplateName emailTemplate ,
         String confirmationUrl ,
         String activationCode ,
         String subject
    ) throws MessagingException {
    String templateName ;
    if (emailTemplate == null){
       templateName = "confirm-email"  ;
    }else {
      templateName =emailTemplate.name();
    }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage ,
                MULTIPART_MODE_MIXED ,
                UTF_8.name()
          );
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("to", to);
        properties.put("password", password);
        properties.put("telephone", telephone);
        properties.put("function", function);
        properties.put("role", role);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("nabil.daghbari15@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateName, context);

        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }

    @Async
    public void sendEmailResetPassword(
            String to ,
            String username ,
            EmailTemplateName emailTemplate ,
            String confirmationUrl ,
            String subject
    ) throws MessagingException {
        String templateName ;
        if (emailTemplate == null){
            templateName = "confirm-email"  ;
        }else {
            templateName =emailTemplate.name();
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage ,
                MULTIPART_MODE_MIXED ,
                UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("nabil.daghbari15@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateName, context);

        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }


    @Async
    public void sendEmailCalifornia(
            String to ,
            String firstname ,
            String lastname ,
            String telephone,
            LocalDate dateInscription ,
            LocalDate dateExpiration ,
            EmailTemplateName emailTemplate ,
            String subject
    ) throws MessagingException {
        String templateName ;
        if (emailTemplate == null){
            templateName = "gym"  ;
        }else {
            templateName =emailTemplate.name();
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage ,
                MULTIPART_MODE_MIXED ,
                UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        properties.put("firstname", firstname);
        properties.put("lastname", lastname);
        properties.put("to", to);
        properties.put("telephone", telephone);
        properties.put("dateInscription", dateInscription);
        properties.put("dateExpiration", dateExpiration);
        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("nabil.daghbari15@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateName, context);

        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }
 }
