package com.scsb.crpro.im;  
  
 
import org.xmpp.component.Component;  
import org.xmpp.component.ComponentException;  
import org.xmpp.component.ComponentManager;  
import org.xmpp.packet.JID;  
import org.xmpp.packet.Packet;  
  
public class ChatComponent implements Component{  
    private static final String name="chat";  
    private static final String description="testChat";  
    private JID jid;  
    private ComponentManager comMgr;  
      
    public String getName() {  
        return name;  
    }  
  
    public String getDescription() {  
        return description;  
    }  
  
    public void processPacket(Packet packet) {  
        //Log.info(packet.toXML());  
          
    }  
  
    public void initialize(JID jid, ComponentManager componentManager)  
            throws ComponentException {  
        this.jid=jid;  
        this.comMgr=componentManager;  
    }  
  
    public void start() {  
        //Log.info("component start");  
          
    }  
  
    public void shutdown() {  
        //Log.info("component shutdown");  
          
    }  
  
} 