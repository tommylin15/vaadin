package com.scsb.crpro.im;

import java.util.Collection;
import java.util.Iterator;
import javax.net.SocketFactory;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Session;
import org.jivesoftware.smack.packet.Message.Type;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

 

/**

 * <b>function:</b> 利用Smack框架完成 XMPP 協議通信

 * @author hoojo

 * @createDate 2012-5-22 上午10:28:18

 * @file ConnectionServerTest.java

 * @package com.hoo.smack.conn

 * @project jwchat

 * @blog http://blog.csdn.net/IBM_hoojo

 * @email hoojo_@126.com

 * @version 1.0

 */

public class SmackXMPPTest {

 

    private Connection connection;

    private ConnectionConfiguration config;

    /** openfire服務器address */

    private final static String server = "10.10.4.124";

    

    private final void fail(Object o) {

        if (o != null) {

            System.out.println(o);

        }

    }

    

    private final void fail(Object o, Object... args) {

        if (o != null && args != null && args.length > 0) {

            String s = o.toString();

            for (int i = 0; i < args.length; i++) {

                String item = args[i] == null ? "" : args[i].toString();

                if (s.contains("{" + i + "}")) {

                    s = s.replace("{" + i + "}", item);

                } else {

                    s += " " + item;

                }

            }

            System.out.println(s);

        }

    }

    

    /**

     * <b>function:</b> 初始Smack對openfire服務器鏈接的基本配置

     * @author hoojo

     * @createDate 2012-6-25 下午04:06:42

     */

    @Before

    public void init() {

        try {

            //connection = new XMPPConnection(server);

            //connection.connect();

            /** 5222是openfire服務器默認的通信端口，你可以登錄http://192.168.8.32:9090/到管理員控制台查看客戶端到服務器端口 */

            config = new ConnectionConfiguration(server, 5222);

            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

            /** 是否啟用壓縮 */ 

            config.setCompressionEnabled(true);

            /** 是否啟用安全驗證 */

            config.setSASLAuthenticationEnabled(true);

            /** 是否啟用調試 */

            config.setDebuggerEnabled(false);

            //config.setReconnectionAllowed(true);

            //config.setRosterLoadedAtLogin(true);

            

            /** 創建connection鏈接 */

            connection = new XMPPConnection(config);

            /** 建立連接 */

            connection.connect();

        } catch (XMPPException e) {

            e.printStackTrace();

        }

        fail(connection);

        fail(connection.getConnectionID());

    }

    

    @After

    public void destory() {

        if (connection != null) {

            connection.disconnect();

            connection = null;

        }

    }

    

    /**

     * <b>function:</b> ConnectionConfiguration 的基本配置相關信息

     * @author hoojo

     * @createDate 2012-6-25 下午04:11:25

     */

    @Test

    public void testConfig() {

        fail("PKCS11Library: " + config.getPKCS11Library());

        fail("ServiceName: {0}", config.getServiceName());

        // ssl證書密碼

        fail("TruststorePassword: {0}", config.getTruststorePassword());

        fail("TruststorePath: {0}", config.getTruststorePath());

        fail("TruststoreType: {0}", config.getTruststoreType());

        

        SocketFactory socketFactory = config.getSocketFactory();

        fail("SocketFactory: {0}", socketFactory);

        /*try {

            fail("createSocket: {0}", socketFactory.createSocket("localhost", 3333));

        } catch (IOException e) {

            e.printStackTrace();

        }*/

    }

    

    /**

     * <b>function:</b> Connection 基本方法信息

     * @author hoojo

     * @createDate 2012-6-25 下午04:12:04

     */

    @Test

    public void testConnection() {

        /** 用戶管理 */

        AccountManager accountManager = connection.getAccountManager();

        for (String attr : accountManager.getAccountAttributes()) {

            fail("AccountAttribute: {0}", attr);

        }

        fail("AccountInstructions: {0}", accountManager.getAccountInstructions());

        /** 是否鏈接 */

        fail("isConnected:", connection.isConnected());

        fail("isAnonymous:", connection.isAnonymous());

        /** 是否有權限 */

        fail("isAuthenticated:", connection.isAuthenticated());

        fail("isSecureConnection:", connection.isSecureConnection());

        /** 是否使用壓縮 */

        fail("isUsingCompression:", connection.isUsingCompression());

    }

    

    /**

     * <b>function:</b> 用戶管理器

     * @author hoojo

     * @createDate 2012-6-25 下午04:22:31

     */

    @Test

    public void testAccountManager() {

        AccountManager accountManager = connection.getAccountManager();

        for (String attr : accountManager.getAccountAttributes()) {

            fail("AccountAttribute: {0}", attr);

        }

        fail("AccountInstructions: {0}", accountManager.getAccountInstructions());

        

        fail("supportsAccountCreation: {0}", accountManager.supportsAccountCreation());

        try {

            /** 創建一個用戶boy，密碼為boy；你可以在管理員控制台頁面http://192.168.8.32:9090/user-summary.jsp查看用戶/組的相關信息，來查看是否成功創建用戶 */

            accountManager.createAccount("boy", "boy");

            /** 修改密碼 */

            accountManager.changePassword("abc");

        } catch (XMPPException e) {

            e.printStackTrace();

        }

    }

    

    @Test

    public void testUser() {

        try {

            /** 用戶登陸，用戶名、密碼 */

            connection.login("admin", "admin");

        } catch (XMPPException e) {

            e.printStackTrace();

        }

        /** 獲取當前登陸用戶 */

        fail("User:", connection.getUser());

        

        /** 所有用戶組 */

        Roster roster = connection.getRoster();

        

        /** 好友用戶組，你可以用Spark添加用戶好友，這樣這裡就可以查詢到相關的數據 */

        Collection<RosterEntry> rosterEntiry = roster.getEntries();

        Iterator<RosterEntry> iter = rosterEntiry.iterator();

        while (iter.hasNext()) {

            RosterEntry entry = iter.next();

            fail("Groups: {0}, Name: {1}, Status: {2}, Type: {3}, User: {4}", entry.getGroups(), entry.getName(), entry.getStatus(), entry.getType(), entry);

        }

        

        fail("-------------------------------");

        /** 未處理、驗證好友，添加過的好友，沒有得到對方同意 */

        Collection<RosterEntry> unfiledEntries = roster.getUnfiledEntries();

        iter = unfiledEntries.iterator();

        while (iter.hasNext()) {

            RosterEntry entry = iter.next();

            fail("Groups: {0}, Name: {1}, Status: {2}, Type: {3}, User: {4}", entry.getGroups(), entry.getName(), entry.getStatus(), entry.getType(), entry);

        }

    }

    

    @Test

    @SuppressWarnings("static-access")

    public void testPacket() {

        try {

            connection.login("admin", "admin");

        } catch (XMPPException e) {

            e.printStackTrace();

        }

        

        //Packet packet = new Data(new DataPacketExtension("jojo@" + server, 2, "this is a message"));

        //connection.sendPacket(packet);

        

        /** 更改用戶狀態，available=true表示在線，false表示離線，status狀態簽名；當你登陸後，在Spark客戶端軟件中就可以看到你登陸的狀態 */

        Presence presence = new Presence(Presence.Type.available);

        presence.setStatus("Q我吧");

        connection.sendPacket(presence);

        

        Session session = new Session();

        String sessid = session.nextID();

        connection.sendPacket(session);

        /** 向jojo@192.168.8.32 發送聊天消息，此時你需要用Spark軟件登陸jojo這個用戶，

         * 這樣代碼就可以向jojo這個用戶發送聊天消息，Spark登陸的jojo用戶就可以接收到消息

         **/

        /** Type.chat 表示聊天，groupchat多人聊天，error錯誤，headline在線用戶； */

        Message message = new Message("34711@" + server, Type.chat);

        //Message message = new Message(sessid, Type.chat);

        message.setBody("h!~ , I'am is admin!");

        connection.sendPacket(message);

        try {

            Thread.sleep(1);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

    }

    

    /**

     * <b>function:</b> 測試聊天消息管理類

     * @author hoojo

     * @createDate 2012-6-25 下午05:03:23

     */

    @Test

    public void testChatManager() {

        /** 設置狀態 */

        try {

            connection.login("admin", "admin");

        } catch (XMPPException e) {

            e.printStackTrace();

        }

        

        /** 設置狀態 */

        Presence presence = new Presence(Presence.Type.available);

        presence.setStatus("Q我吧");

        connection.sendPacket(presence);

        

        /** 獲取當前登陸用戶的聊天管理器 */

        ChatManager chatManager = connection.getChatManager();

        /** 為指定用戶創建一個chat，MyMessageListeners用於監聽對方發過來的消息  */

        Chat chat = chatManager.createChat("34711@" + server, new MyMessageListeners());

        try {

            /** 發送消息 */

            chat.sendMessage("h!~ jojo……");

            

            /** 用message對像發送消息 */

            Message message = new Message();

            message.setBody("message");

            message.setProperty("color", "red");

            chat.sendMessage(message);

        } catch (XMPPException e) {

            e.printStackTrace();

        }

        try {

            Thread.sleep(1000 * 1000);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

    }

    

    /**

     * <b>function:</b> 消息監聽器，用戶監聽對方發送的消息，也可以想對方發送消息

     * @author hoojo

     * @createDate 2012-6-25 下午05:05:31

     * @file SmackXMPPTest.java

     * @package com.hoo.smack

     * @project jwchat

     * @blog http://blog.csdn.net/IBM_hoojo

     * @email hoojo_@126.com

     * @version 1.0

     */

    class MyMessageListeners implements MessageListener {

        public void processMessage(Chat chat, Message message) {

            try {

                /** 發送消息 */

                chat.sendMessage("dingding……" + message.getBody());

            } catch (XMPPException e) {

                e.printStackTrace();

            }

            /** 接收消息 */

            fail("From: {0}, To: {1}, Type: {2}, Sub: {3}", message.getFrom(), message.getTo(), message.getType(), message.toXML());

            /*Collection<Body> bodys =  message.getBodies();

            for (Body body : bodys) {

                fail("bodies[{0}]", body.getMessage());

            }

            //fail(message.getLanguage());

            //fail(message.getThread());

            //fail(message.getXmlns());*/

            fail("body: ", message.getBody());

        }

    }

}