package com.scsb.util;

import com.itextpdf.text.BaseColor;  
import com.itextpdf.text.Font;  
import com.itextpdf.text.pdf.BaseFont;  
import com.itextpdf.tool.xml.XMLWorkerFontProvider;  
  
public class AsianFontProvider extends XMLWorkerFontProvider {  
	
    public Font getFont(final String fontname, final String encoding,  
            final boolean embedded, final float size, final int style,  
            final BaseColor color) {  
        BaseFont bf = null;  
        try {  
            // 設定字體
      	  	String fontPath =  "Font\\scsbeudc.ttf";
      	  	bf     =BaseFont.createFont(fontPath,BaseFont.IDENTITY_H,true);        	  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        Font font = new Font(bf, size, style, color);  
        font.setColor(color);  
        return font;  
    }  
}  