package com.example.AutoSched.Websocket;

import com.example.AutoSched.HttpBodies.SessionId;
import com.example.AutoSched.User.User;
import com.example.AutoSched.User.UserRepository;
import com.example.AutoSched.User.UserService;
import com.example.AutoSched.exception.ErrorException;
import net.bytebuddy.implementation.bytecode.Throw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Controller
@ServerEndpoint(value = "/directmessage/{userName}")
public class Websocket {

    @Autowired
    private static UserService userRepository;
    private static MessageRepo MsgRepo;


    @Autowired
    public void setMsgRepo(MessageRepo repo) {
        MsgRepo = repo;
    }

    @Autowired
    public void setUserRepository(UserService usrRepo){
        userRepository = usrRepo;
    }

    private static Map<Session, String> sessionUser = new Hashtable<>();
    private static Map<String, Session> userSession = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(Websocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) throws IOException {

        logger.info("entered open");

        User user = userRepository.findByUsername(userName);

        logger.info(user.getName());
        logger.info(Integer.toString(user.getId()));

        sessionUser.put(session, userName);
        userSession.put(userName, session);

        directMessage(userName, getChatHistory());

        String message = "User:" + user.getName() + " has Joined chat";
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        logger.info("entered onMessage function: message is:" + message);
        String sendUser = sessionUser.get(session);

        /*
        if (message.startsWith("@Admin")){
            String destUser = "VinceQuatt";
            User adminUser = userRepository.findByUsername(destUser);

            directMessage(adminUser.getName(), "[DM]" + sendUser.getName() + ":" + message);
            directMessage(sendUser.getName(), "[DM]" + sendUser.getName() + ":" + message);
        }

        else if (message.startsWith("@Instructor")){
            String destUser = "SMitra";
            User instUser = userRepository.findByUsername(destUser);

            directMessage(instUser.getName(), "[DM]" + sendUser.getName() + ":" + message);
            directMessage(sendUser.getName(), "[DM]" + sendUser.getName() + ":" + message);
        }

        else {
            broadcast(sendUser.getName() + ": " + message);

            MsgRepo.save(new Message(sendUser.getName(), message));
        }
        */
        if (message.startsWith("@Admin") && userRepository.findByUsername(sendUser).isAdmin() == false){
            String destUser = "VinceQuatt";
            User adminUser = userRepository.findByUsername(destUser);

            directMessage(destUser, "[DM]" + sendUser + ": " + message);
            directMessage(sendUser, "[DM]" + sendUser + ": " + message);
        }
        else if (message.startsWith("@Admin") && userRepository.findByUsername(sendUser).isAdmin() == true){
            directMessage(sendUser, "[DM]" + "SERVER" + ": " + "Try not to message other admins on the app, use Discord");
        }

        else if (message.startsWith("@Instructor") && (userRepository.findByUsername(sendUser).isAdmin() == false && userRepository.findByUsername(sendUser).isInstructor() == false)){
            String destUser = "SMitra";
            User instUser = userRepository.findByUsername(destUser);

            directMessage(destUser, "[DM]" + sendUser + ": " + message);
            directMessage(sendUser, "[DM]" + sendUser + ": " + message);
        }
        else if (message.startsWith("@Instructor") && userRepository.findByUsername(sendUser).isAdmin() == true){
            directMessage(sendUser, "[DM]" + "SERVER" + ": " + "Try to contact Instructors professionally, use Email");
        }
        else if (message.startsWith("@Instructor") && userRepository.findByUsername(sendUser).isInstructor() == true){
            directMessage(sendUser, "[DM]" + "SERVER" + ": " + "Try to contact other Instructors professionally, use Email");
        }

        else {
            broadcast(sendUser + ": " + message);

            MsgRepo.save(new Message(sendUser, message));
        }

    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered close");

        String user = sessionUser.get(session);

        sessionUser.remove(session);
        logger.info("removed session");

        userSession.remove(user);
        logger.info("removed user");

        String message = user + " disconnected";
        broadcast(message);

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("ERROR ");
        throwable.printStackTrace();
    }
/*
    private void directMessage(String user, String message) {
        try {
            User actualUser = userRepository.findByUsername(user);
            logger.info(user);
            logger.info(actualUser.getName());
            userSession.get(actualUser).getBasicRemote().sendText(message);
        } catch(IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();

        }
    }
*/

    private void directMessage(String user, String message) {
        try {
            userSession.get(user).getBasicRemote().sendText(message);
        } catch(IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();

        }
    }



    private void broadcast(String message) {
        sessionUser.forEach((session, user) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }
        });
    }

    private String getChatHistory() {
        List<Message> messageList = MsgRepo.findAll();

        StringBuilder sb = new StringBuilder();
        if(messageList != null && messageList.size() != 0){
            for (Message message : messageList){
                sb.append(message.getName() + ": " + message.getContent() + "\n");
            }
        }
        return sb.toString();
    }
}
