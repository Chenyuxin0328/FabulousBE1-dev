package com.creatorsn.fabulous.service.impl;

import com.creatorsn.fabulous.dto.StatusCode;
import com.creatorsn.fabulous.entity.Email;
import com.creatorsn.fabulous.entity.EmailMessage;
import com.creatorsn.fabulous.entity.EmailTemplate;
import com.creatorsn.fabulous.entity.EmailUsedCounter;
import com.creatorsn.fabulous.exception.UserException;
import com.creatorsn.fabulous.mapper.EmailMapper;
import com.creatorsn.fabulous.mapper.EmailMessageMapper;
import com.creatorsn.fabulous.mapper.EmailTemplateMapper;
import com.creatorsn.fabulous.service.EmailService;
import com.creatorsn.fabulous.util.EmailSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    final private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    final private EmailMessageMapper messageMapper;
    final private EmailMapper emailMapper;

    final private EmailTemplateMapper emailTemplateMapper;

    final private ObjectMapper objectMapper;

    public EmailServiceImpl(
            EmailMapper emailMapper,
            EmailMessageMapper messageMapper,
            EmailTemplateMapper emailTemplateMapper,
            ObjectMapper objectMapper
    ) {
        this.emailMapper = emailMapper;
        this.messageMapper = messageMapper;
        this.emailTemplateMapper = emailTemplateMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public Email addEmail(Email email) {
        email.setId(UUID.randomUUID().toString().toUpperCase());
        return emailMapper.add(email);
    }

    @Override
    public boolean deleteEmailById(String id) {
        if (id == null) return false;
        return emailMapper.delete(id);
    }

    @Override
    public boolean updateEmailById(String id, Email email) {
        if (id == null) return false;
        email.setId(id);
        return emailMapper.update(email);
    }

    @Override
    public EmailTemplate updateTemplate(EmailTemplate template) throws UserException {
        if (template.getId() == null) {
            throw new UserException(StatusCode.IdNull);
        }
        return emailTemplateMapper.update(template);
    }

    @Override
    public List<EmailTemplate> getTemplates(OffsetDateTime before, long offset, int pageSize) {
        return emailTemplateMapper.list(before, offset, pageSize);
    }

    @Override
    public EmailTemplate addTemplate(EmailTemplate template) throws UserException {
        if (emailTemplateMapper.existsName(template.getName())) {
            throw new UserException(StatusCode.EmailTemplateExists);
        }
        template.setId(UUID.randomUUID().toString().toUpperCase());
        return emailTemplateMapper.add(template);
    }

    @Override
    public List<Email> getEmails(String query, long offset, int pageSize, String lastKey) {
        return emailMapper.getEmails(query, offset, pageSize, lastKey);
    }

    @Override
    public boolean sendEmail(String id, String to, String subject, String content) {
        return sendEmail(id, to, subject, content, null);
    }

    @Override
    public boolean sendEmail(String id, String to, String templateName, HashMap<String, String> variables) throws UserException {
        return sendEmail(id, to, templateName, variables, null);
    }

    @Override
    public boolean sendEmail(String id, String to, String templateName, HashMap<String, String> variables, List<MultipartFile> files) throws UserException {
        var emailTemplate = emailTemplateMapper.getByName(templateName);
        if (emailTemplate == null) {
            throw new UserException(StatusCode.EmailTemplateNotFound);
        }
        var subject = emailTemplate.getSubject();
        var content = emailTemplate.getContent();
        var defaultVariablesJSON = emailTemplate.getVariables();
        if (defaultVariablesJSON == null) {
            defaultVariablesJSON = "{}";
        }
        HashMap<String, String> defaultVariables = new HashMap<>();
        try {
            defaultVariables = objectMapper.readValue(defaultVariablesJSON, HashMap.class);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        for (var entry : defaultVariables.entrySet()) {
            if (variables.containsKey(entry.getKey()) && variables.get(entry.getKey()) != null) {
                subject = subject.replace("{$$$~" + entry.getKey() + "}", variables.get(entry.getKey()));
                content = content.replace("{$$$~" + entry.getKey() + "}", variables.get(entry.getKey()));
            } else if (entry.getValue() != null) {
                subject = subject.replace("{$$$~" + entry.getKey() + "}", entry.getValue());
                content = content.replace("{$$$~" + entry.getKey() + "}", entry.getValue());
            }
        }
        return sendEmail(id, to, subject, content, files);
    }

    @Override
    public boolean sendEmail(String id, String to, String subject, String content, List<MultipartFile> files) {
        var email = emailMapper.getEmail(id);
        if (email == null) {
            return false;
        }
        var sender = new EmailSender(email.getSmtphost(), email.getSmtpport(), email.getUsername(), email.getPassword(), email.isSmtpssl());
        var message = new EmailMessage();
        message.setId(UUID.randomUUID().toString().toUpperCase());
        message.setEmailId(id);
        message.setTo(to);
        message.setSubject(subject);
        message.setContent(content);
        try {
            sender.send(to, subject, content, files);
            message.setStatus(EmailMessage.EmailStatus.SENT);
            return true;
        } catch (Exception e) {
            message.setStatus(EmailMessage.EmailStatus.FAILED);
            message.setErr(e.getMessage());
            logger.error(message.getErr());
            return false;
        } finally {
            messageMapper.add(message);
        }
    }

    @Override
    public boolean sendEmail(String id, List<String> to, String subject, String content) {
        return sendEmail(id, to, subject, content, null);
    }

    @Override
    public boolean sendEmail(String id, List<String> to, String templateName, HashMap<String, String> variables) throws UserException {
        return sendEmail(id, to, templateName, variables, null);
    }

    @Override
    public boolean sendEmail(String id, List<String> to, String subject, String content, List<MultipartFile> files) {
        return sendEmail(id, String.join(",", to), subject, content, files);
    }

    @Override
    public boolean sendEmail(String id, List<String> to, String templateName, HashMap<String, String> variables, List<MultipartFile> files) throws UserException {
        return sendEmail(id, String.join(",", to), templateName, variables, files);
    }

    @Override
    public EmailUsedCounter getEmailUsedCounter(OffsetDateTime from, OffsetDateTime to) {
        var counter = new EmailUsedCounter();
        counter.setFrom(from);
        counter.setTo(to);
        counter.setEmailUseds(messageMapper.getEmailUsedCounter(from, to));
        return counter;
    }
}
