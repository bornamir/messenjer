package org.bihe.interfaces;

import org.bihe.models.Message;

import java.util.List;

public interface MessageDAO {
    List<Message> getAllMessagesBetweenTwoUsers(String user1, String user2) ;
    boolean saveMessage (Message message);
    // Do not need now
    Message getMessageByID(Integer id);
    boolean deleteMessage(Integer id);
    boolean updateMessage(Integer id);
    List<Message> getAllMessages();
}
