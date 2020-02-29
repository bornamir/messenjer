package org.bihe.daoimpl;

import org.bihe.interfaces.MessageDAO;
import org.bihe.models.Message;
import org.bihe.models.User;
import org.bihe.sevices.Constants;
import org.bihe.sevices.DBConnection;

import java.sql.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class MessageDoaImpl implements MessageDAO {

    private static DBConnection dbConnection = new DBConnection();
    //constant formaters for read and write to database
    private final static DateTimeFormatter FORMATTER_TO_SQL =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]");
    private final static DateTimeFormatter FORMATTER_FROM_SQL =
            DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss[.SSS]xxx"));
//    private final static String DEFAULT_ZONE_OFFSET = "+00:00";

    private static Connection getConnection() throws SQLException {
        return dbConnection.getConnection();

    }

    private Message createMessageFromResultSet(ResultSet rs) throws SQLException {
        Message message = new Message();
        message.setID(rs.getInt("ID"));
        message.setMessage((rs.getString("Message")));

        User senderUser = new User();
        senderUser.setID(rs.getInt("s.ID"));
        senderUser.setUsername(rs.getString("s.username"));
        senderUser.setFirstName(rs.getString("s.FirstName"));
        senderUser.setLastName(rs.getString("s.LastName"));
        senderUser.setEmail(rs.getString("s.Email"));
        senderUser.setPassword(rs.getString("s.Password"));
        message.setSenderUser(senderUser);

        User receiverUser = new User();
        receiverUser.setID(rs.getInt("r.ID"));
        receiverUser.setUsername(rs.getString("r.username"));
        receiverUser.setFirstName(rs.getString("r.FirstName"));
        receiverUser.setLastName(rs.getString("r.LastName"));
        receiverUser.setEmail(rs.getString("r.Email"));
        receiverUser.setPassword(rs.getString("r.Password"));
        message.setReceiverUser(receiverUser);


        String dateTime = rs.getString("CreatedDate");
        ZonedDateTime zn = ZonedDateTime.parse(dateTime + Constants.DEFAULT_ZONE_OFFSET, FORMATTER_FROM_SQL);
        message.setCreated_date(zn);
        message.setMessageType(rs.getString("MessageType"));

        return message;
    }

    @Override
    public List<Message> getAllMessagesBetweenTwoUsers(String user1, String user2) {
        String sql = "select *" +
                "from messenjer.Messages" +
                "    inner join Users r on Messages.ReceiverID = r.ID" +
                "    inner join Users s on Messages.SenderID = s.ID"+
                " where (s.username=? and r.username=?) or (s.username=? and r.username=?)" +
                "order by createddate";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setString(1, user1);
            pst.setString(2, user2);
            pst.setString(3, user2);
            pst.setString(4, user1);

            try (ResultSet rs = pst.executeQuery()) {
                List<Message> messages = new LinkedList();
                while (rs.next()) {
                    messages.add(this.createMessageFromResultSet(rs));
                }
                return messages;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public boolean saveMessage(Message message) {
        String sql = "INSERT INTO messenjer.Messages (Message, SenderID, ReceiverID, CreatedDate,MessageType) VALUES (?,?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, message.getMessage());
            pstmt.setInt(2,message.getSenderUser().getID());
            pstmt.setInt(3, message.getReceiverUser().getID());
            pstmt.setString(4, FORMATTER_TO_SQL.format(message.getCreated_date()));
            pstmt.setString(5, message.getMessageType());

            int i = pstmt.executeUpdate();
            if (i == 1) {   // if there is one affected row in the query.
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return false;
    }


    @Override
    public Message getMessageByID(Integer id) {
        String sql = "select *" +
                "from messenjer.Messages" +
                "         inner join Users r on Messages.ReceiverID = r.ID" +
                "         inner join Users s on Messages.SenderID = s.ID " +
                "where Messages.ID =" + id;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)
        ) {
            if (rs.next()) {
                return this.createMessageFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    // No need for them in this phase of the project
    // TODO implement these methods
    @Override
    public boolean deleteMessage(Integer id) {
        return false;
    }

    @Override
    public boolean updateMessage(Integer id) {
        return false;
    }

    @Override
    public List<Message> getAllMessages() {
        return null;
    }
}
