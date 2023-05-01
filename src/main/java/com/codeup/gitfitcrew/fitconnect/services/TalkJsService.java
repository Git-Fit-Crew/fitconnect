package com.codeup.gitfitcrew.fitconnect.services;

import com.codeup.gitfitcrew.fitconnect.models.ConversationResponse;
import com.codeup.gitfitcrew.fitconnect.models.ConversationResponse.Conversation;
import com.codeup.gitfitcrew.fitconnect.models.User;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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
        URL url = new URL(baseUrl + "/v1/" + talkJsAppId + "/users/" + userId + "/conversations" + "?limit=20");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type","text/plain");
        connection.setRequestProperty("Authorization", "Bearer " + talkJsSecretKey);
        connection.setRequestMethod("GET");
        System.out.println(connection.getURL().toString() + " responded with status code: " + connection.getResponseCode());
        String jsonResponse = new String(connection.getInputStream().readAllBytes());

        Gson gson = new Gson();
        ConversationResponse response = gson.fromJson(jsonResponse, ConversationResponse.class);
        connection.getInputStream().close();
        connection.disconnect();
        return response.getData();
    }

    /**
     * Filters the list of conversations by a user's friends list.
     *
     * @param userId the ID of the user to filter conversations for
     * @param getFriends true to return conversations with friends, false to return conversations without friends
     * @return a list of conversations filtered by the user's friends list
     * @throws IOException if there is an error retrieving conversations or friend data
     */
    private List<Conversation> filterConversationsByFriendsList(String userId, boolean getFriends) throws IOException {
        List<Conversation> conversations = getAllConversationsByUserId(userId);
        List<User> friends = friendService.getFriends();
        List<String> friendIds = new ArrayList<>();
        friends.forEach(friend -> friendIds.add(String.valueOf(friend.getId())));
        conversations.removeIf(conversation -> {
            for (String id : conversation.getParticipants().keySet()) {
                if (friendIds.contains(id)) {
                    return !getFriends;
                }
            }
            return getFriends;
        });
        return conversations;
    }

    /**
     * Sets the access level for all participants in the given conversation.
     *
     * @param conversation the conversation to update
     * @param access the new access level to set (e.g. "Read" or "ReadWrite")
     */
    private void setAccessForAllParticipants(Conversation conversation, String access) {
        conversation.getParticipants().forEach((id, participant) -> {
            if (participant.getAccess().equals(access)) {
                return;
            }
            String requestBody = "{ \"access\": \"" + access + "\" }";
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/v1/" + talkJsAppId + "/conversations/" + conversation.getId() + "/participants/" + id))
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + talkJsSecretKey)
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            try {
                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.uri() + " responded with status code: " + response.statusCode());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void setAccessForAllConversations(User user) {
        String userId = String.valueOf(user.getId());
        try {
            List<Conversation> nonFriendConversations = filterConversationsByFriendsList(userId, false);
            List<Conversation> friendConversations = filterConversationsByFriendsList(userId, true);
            nonFriendConversations.forEach(conversation -> setAccessForAllParticipants(conversation, "Read"));
            friendConversations.forEach(conversation -> setAccessForAllParticipants(conversation, "ReadWrite"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
