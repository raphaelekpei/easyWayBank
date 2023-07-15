package com.raphael.easywaybank.impl;

import com.musdon.thejavaacademybank.dto.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);

    void sendEmailWithAttachment(EmailDetails emailDetails);

}
