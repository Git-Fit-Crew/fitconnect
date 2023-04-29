package com.codeup.gitfitcrew.fitconnect.models;

import lombok.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@Setter
public class ConversationResponse {
    private final List<Conversation> data;

    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Conversation {
        private long createdAt;
        private Map<String, Object> custom;
        private String id;
        private Message lastMessage;
        private Map<String, Participant> participants;
        private String photoUrl;
        private String subject;
        private String topicId;
        private List<String> welcomeMessages;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Message {
        private String attachment;
        private String conversationId;
        private long createdAt;
        private Map<String, Object> custom;
        private String id;
        private String location;
        private String origin;
        private List<String> readBy;
        private String senderId;
        private String text;
        private String type;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Participant {
        private String access;
        private boolean notify;
    }
}
