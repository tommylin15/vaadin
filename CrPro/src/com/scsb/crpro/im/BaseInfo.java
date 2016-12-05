package com.scsb.crpro.im;

import org.jivesoftware.smack.packet.PacketExtension;  
  
public class BaseInfo implements PacketExtension{  
  
    private String sendName;  
    private String recvName;  
      
    public String getRecvName() {  
        return recvName;  
    }  
  
    public void setRecvName(String recvName) {  
        this.recvName = recvName;  
    }  
  
    public String getSendName() {  
        return sendName;  
    }  
  
    public void setSendName(String sendName) {  
        this.sendName = sendName;  
    }  
  
    public String getElementName() {  
         return "base_info";  
    }  
  
    public String getNamespace() {  
        return "uc:staff_name";  
    }  
    //<span style="white-space:pre">    </span>//重寫xml方法  
    public String toXML() {  
        StringBuilder buf = new StringBuilder();  
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append(  
            "\">");  
        if (getSendName() != null) {  
            buf.append("<send_name>").append(getSendName()).append("</send_name>");  
        }  
        if (getRecvName() != null) {  
            buf.append("<rec_name>").append(getRecvName()).append("</rec_name>");  
        }  
        buf.append("</").append(getElementName()).append(">");  
        return buf.toString();  
    }  
  
} 