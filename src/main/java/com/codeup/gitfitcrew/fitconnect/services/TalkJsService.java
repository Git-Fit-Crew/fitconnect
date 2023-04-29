package com.codeup.gitfitcrew.fitconnect.services;

import com.codeup.gitfitcrew.fitconnect.models.ConversationResponse;
import com.codeup.gitfitcrew.fitconnect.models.ConversationResponse.Conversation;
import com.codeup.gitfitcrew.fitconnect.models.Friend;
import com.codeup.gitfitcrew.fitconnect.models.User;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TalkJsService {
    @Value("${talk-js-secret-key}")
    private String talkJsSecretKey;
    @Value("${talk-js-api-key}")
    private String talkJsAppId;
    private final String baseUrl = "https://api.talkjs.com";
    private final FriendService friendService;

    private List<Conversation> getAllConversationsByUserId(String userId) throws IOException {
        URL url = new URL(baseUrl + "/v1/" + talkJsAppId + "/users/" + userId + "/conversations");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type","text/plain");
        connection.setRequestProperty("Authorization", "Bearer " + talkJsSecretKey);
        connection.setRequestMethod("GET");
        connection.getResponseCode();
        String jsonResponse = new String(connection.getInputStream().readAllBytes());
        System.out.println("jsonResponse = " + jsonResponse);

        Gson gson = new Gson();
        ConversationResponse response = gson.fromJson(jsonResponse, ConversationResponse.class);
        System.out.println("response = " + response);
        return response.getData();
    }

    public void updateConversationAccessForUser(String userId) {
        try {
            List<Conversation> conversations = getAllConversationsByUserId(userId);
            List<User> friends = friendService.getFriends();
            List<String> friendIds = new ArrayList<>();
            friends.forEach(friend -> friendIds.add(String.valueOf(friend.getId())));
            friendIds.forEach(System.out::println);
//            friends.forEach(friend -> conversations.removeIf(conversationToRemove -> !conversationToRemove.getParticipants().containsKey(String.valueOf(friend.getId()))));
            conversations.removeIf(conversation -> {
                boolean containsFriend = false;
                System.out.println("conversation = " + String.join(",", conversation.getParticipants().keySet()));
                for (String id : conversation.getParticipants().keySet()) {
                    if (friendIds.contains(id)) {
                        containsFriend = true;
                        break;
                    }
                }
                return !containsFriend;
            });
            conversations.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
