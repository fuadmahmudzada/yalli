package org.yalli.wah.model.enums;

import lombok.Getter;

@Getter
public enum EmailTemplate {
    CONFIRMATION("Your otp code for confirm email {0}", "Confirm Email - OTP"),
    RESET_PASSWORD("Your otp code for resettin password is {0}", "Reset Password - OTP"),
    MENTORSHIP_APPLY("Your mentorship request was delivered.", "Mentorship request"),
    MENTORSHIP_ACCEPT("Your mentorship request was accepted.", "Mentorship request"),
    MENTORSHIP_REJECT("Your mentorship request was rejected.", "Mentorship request");

    private final String body;
    private final String subject;

    EmailTemplate(String body, String subject) {
        this.body = body;
        this.subject = subject;
    }
}
