package org.bihe.interfaces;

import org.bihe.models.Message;

import java.util.List;

public interface MessageDAO {
    List<Message> getAllMessagesBetweenTwoUsers(Integer user1, Integer user2) ;
    Message getMessageByID(Integer id);
    boolean saveMessage (Message message);
    boolean deleteMessage(Integer id);
    boolean updateMessage(Integer id);
    List<Message> getAllMessages();
}
