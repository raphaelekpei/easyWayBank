package com.raphael.easywaybank.service.mailSerivce;


import com.raphael.easywaybank.dto.request.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);

    void sendEmailWithAttachment(EmailDetails emailDetails);

}
