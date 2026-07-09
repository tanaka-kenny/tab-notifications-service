package za.co.pacifish.notification_service.enumeration;

import lombok.Getter;

@Getter
public enum EmailTemplate {

    TAB_PLATFORM_INVITE("tab-invitation-email.ftl");

    private final String file;

    EmailTemplate(String fileName) {
        this.file = fileName;
    }
}
