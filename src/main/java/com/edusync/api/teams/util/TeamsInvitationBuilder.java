package com.edusync.api.teams.util;

import com.edusync.api.actor.student.model.Student;
import com.microsoft.graph.models.Invitation;
import com.microsoft.graph.models.InvitedUserMessageInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeamsInvitationBuilder {

    private static final String INVITE_REDIRECT_URL = "https://myapps.microsoft.com";
    private static final String INVITE_MESSAGE_BODY = "You have been invited to join online classes on EduSync. Please accept this invitation to access your scheduled classes.";

    public static Invitation buildGuestInvitation(Student student) {
        var invitation = new Invitation();
        invitation.setInvitedUserEmailAddress(student.getEmail());
        invitation.setInviteRedirectUrl(INVITE_REDIRECT_URL);
        invitation.setSendInvitationMessage(true);

        var messageInfo = new InvitedUserMessageInfo();
        messageInfo.setCustomizedMessageBody(INVITE_MESSAGE_BODY);
        invitation.setInvitedUserMessageInfo(messageInfo);

        return invitation;
    }

    public static String extractGuestIdFromInvitation(Invitation result) {
        return Objects.nonNull(result.getInvitedUser()) ? result.getInvitedUser().getId() : null;
    }
}
