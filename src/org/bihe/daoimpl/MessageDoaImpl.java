package org.bihe.daoimpl;

import org.bihe.interfaces.MessageDAO;
import org.bihe.models.Message;
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
    private final static String DEFAULT_ZONE_OFFSET = "+00:00";

    private static Connection getConnection() throws SQLException {
        return dbConnection.getConnection();

    }

    private Message createMessageFromResultSet(ResultSet rs) throws SQLException {
        Message message = new Message();
        message.setID(rs.getInt("ID"));
        message.setMessage((rs.getString("Message")));
        message.setSenderID(rs.getInt("SenderID"));
        message.setReceiverID(rs.getInt("ReceiverID"));
        String dateTime = rs.getString("CreatedDate");
        ZonedDateTime zn = ZonedDateTime.parse(dateTime + DEFAULT_ZONE_OFFSET, FORMATTER_FROM_SQL);
        message.setCreated_date(zn);

        return message;
    }

    @Override
    public List<Message> getAllMessagesBetweenTwoUsers(Integer user1, Integer user2) {
        String sql = "SELECT * FROM messenjer.Messages where (SenderID=? and ReceiverID=?) or (SenderID=? and ReceiverID=?)";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setInt(1, user1);
            pst.setInt(2, user2);
            pst.setInt(3, user2);
            pst.setInt(4, user1);

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
    public Message getMessageByID(Integer id) {
        String sql = "SELECT * FROM messenjer.Messages where ID=" + id;
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

    @Override
    public boolean saveMessage(Message message) {
        String sql = "INSERT INTO messenjer.Messages (Message, SenderID, ReceiverID, CreatedDate) VALUES (?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, message.getMessage());
            pstmt.setInt(2,message.getSenderID());
            pstmt.setInt(3, message.getReceiverID());
            pstmt.setString(4, FORMATTER_TO_SQL.format(message.getCreated_date()));
            int i = pstmt.executeUpdate();
            if (i == 1) {   // if there is one affected row in the query.
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return false;
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
