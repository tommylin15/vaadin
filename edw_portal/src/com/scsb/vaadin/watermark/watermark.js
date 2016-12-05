// Define the namespace
var watermark = watermark || {};

watermark.WaterMark4Div = function (element) {
    this.element = element;
    this.element.innerHTML ="";
    var defPage ="<div id='PrintPage' style=\"z-index: 0; position :relative;\">" +
	"<div id=\"DataBody\" style=\"z-index: 2; position :relative;\">NO DATA</div>" +
	"</div>";
    // Getter and setter for the value property
    this.getMarkValue = function () {
        return this.element.innerHTML;
    };
    
    this.setValue = function (value) {
    	this.element.innerHTML=defPage;
        //this.element.style.border = "thin solid red";
        //this.element.style.display = "inline-block";
        var Page =this.element.getElementsByTagName("div")[0];
        var DataBody =Page.getElementsByTagName("div")[0];    	
    	DataBody.innerHTML =  value;
    };
    
    this.setWaterMark =function (watermark) {  
      var Page =this.element.getElementsByTagName("div")[0];
      var DataBody =Page.getElementsByTagName("div")[0];
    	
	  var xElement =Page;
	  var direction = "2";
	  var count = 1;
	  var defaultLanguage = "tw";
	  var fSize1 = 16, fSize2 = 10;
	  if (defaultLanguage == "en"){
	    fSize1 = 12, fSize2 = 9;
	  }
	  var xHeight = 0, xDepth = 220, xWidth = 350;
	  var sHeight = xElement.offsetHeight;
	  
	  for(idx=0;idx<count;idx++){  
	    var userName = watermark;
	    if(userName == null || userName.length == 0){
	      xHeight += sHeight; 
	      continue;
	    }   

	    var hCount = Math.floor(sHeight/xDepth)-1;
	    hCount = hCount>0?hCount:1;   
	    for(sub=0;sub<hCount;sub++){		    	
	      var wCount = Math.floor(xElement.offsetWidth/xWidth);
	      var offset = Math.floor((xElement.offsetWidth - wCount * xWidth) / 2);	      
	      for(ix=0;ix<wCount;ix++){
	        xTop = xHeight + xElement.offsetTop + (sub * xDepth + 	0);
	        xLeft = offset + (xWidth * ix) + xElement.offsetLeft -100;
	        var elemInfo = document.createElement("div");
	        elemInfo.style.fontSize = fSize1 + "pt";
	        elemInfo.style.fontWeight = "bold";
	        elemInfo.style.lineHeight = fSize1 + "pt";
	        elemInfo.style.textAlign  = "center";
	        elemInfo.style.color = "#E9DBD8";
	        elemInfo.style.position = "absolute";
	        elemInfo.style.zIndex = "1";
	        elemInfo.style.top  = xTop + "px";
	        elemInfo.style.left  = xLeft + "px";
	        elemInfo.innerHTML = "內部限閱, 請勿外洩" +
	                             "<br><span style='font-size:" + fSize2 + "pt';>" + userName + "</span>";
		     if(window.attachEvent){ //IE
	          var rad = (-45 * Math.PI) / 180.0;  
	          var cos = Math.cos(rad);
	          var sin = Math.sin(rad); 
	          var filter = "progid:DXImageTransform.Microsoft.Matrix(sizingMethod='auto expand', M11 = " + cos + ", M12 = " + (-sin) + ", M21 = " + sin + ", M22 = " + cos + ")"; 
	          elemInfo.style.filter = filter;
	        }else{
	          elemInfo.style.MozTransform = "rotate(-45deg)";
	          elemInfo.style.webkitTransform = "rotate(-45deg)";
	        }
	        xElement.insertBefore(elemInfo, DataBody);
	      }//for  
	    }//for
	    xHeight += sHeight;
	  }//for
	}//function
    
    this.setPrint =function(ynPrint ,watermark ,value){
    	if (ynPrint=="Y"){
        	var printPage = window.open("","printPage","");
        	printPage.document.open();
        	printPage.document.write("<HTML><head></head>");
        	//printPage.document.write("<BODY onload='setWaterMark(\""+watermark+"\");'>");        	
        	printPage.document.write("<BODY>");
        	printPage.document.write("<div id='PrintPage' style=\"z-index: 0; position :relative;\">");
        	printPage.document.write("<div id='DataBody' style=\"z-index: 2; position :relative;\">"+value+"</div>");
        	printPage.document.write("</div>");
        	printPage.document.write("<script Language=\"JavaScript\">");
        	printPage.document.write("function setWaterMark(watermark){");
        	//這個alert 若沒有加,在google chrome 會有無法顯示浮水印的問題,待確認
        	printPage.document.write("	  alert('列印準備完成！');");
        	printPage.document.write("    var Page =document.getElementsByTagName(\"div\")[0];");
        	printPage.document.write("    var DataBody =Page.getElementsByTagName(\"div\")[0];");
        	printPage.document.write("	  var xElement =Page;");
        	printPage.document.write("	  var direction = \"2\";");
        	printPage.document.write("	  var count = 1;");
        	printPage.document.write("	  var defaultLanguage = \"tw\";");
        	printPage.document.write("	  var fSize1 = 16, fSize2 = 10;");
        	printPage.document.write("	  if (defaultLanguage == \"en\"){");
        	printPage.document.write("	    fSize1 = 12, fSize2 = 9;");
        	printPage.document.write("	  }");
        	printPage.document.write("	  var xHeight = 0, xDepth = 220, xWidth = 350;");
        	printPage.document.write("	  var sHeight = xElement.offsetHeight;");
        	printPage.document.write("	  for(idx=0;idx<count;idx++){  ");
        	printPage.document.write("	    var userName = watermark;");
        	printPage.document.write("	    if(userName == null || userName.length == 0){");
        	printPage.document.write("	      xHeight += sHeight; ");
        	printPage.document.write("	      continue;");
        	printPage.document.write("	    }   ");
        	printPage.document.write("	    var hCount = Math.floor(sHeight/xDepth);");
        	printPage.document.write("	    hCount = hCount>0?hCount:1;   ");
        	printPage.document.write("	    for(sub=0;sub<hCount;sub++){		 ");   	
        	printPage.document.write("	      var wCount = Math.floor(xElement.offsetWidth/xWidth);");
        	printPage.document.write("	      var offset = Math.floor((xElement.offsetWidth - wCount * xWidth) / 2);	      ");
        	printPage.document.write("	      for(ix=0;ix<wCount;ix++){");
        	printPage.document.write("	        xTop = xHeight + xElement.offsetTop + (sub * xDepth + 	0);");
        	printPage.document.write("	        xLeft = offset + (xWidth * ix) + xElement.offsetLeft +60;");
        	printPage.document.write("	        var elemInfo = document.createElement(\"div\");");
        	printPage.document.write("	        elemInfo.style.fontSize = fSize1 + \"pt\";");
        	printPage.document.write("	        elemInfo.style.fontWeight = \"bold\";");
        	printPage.document.write("	        elemInfo.style.lineHeight = fSize1 + \"pt\";");
        	printPage.document.write("	        elemInfo.style.textAlign  = \"center\";");
        	printPage.document.write("	        elemInfo.style.color = \"#E9DBD8\";");
        	printPage.document.write("	        elemInfo.style.position = \"absolute\";");
        	printPage.document.write("	        elemInfo.style.zIndex = \"1\";");
        	printPage.document.write("	        elemInfo.style.top  = xTop + \"px\";");
        	printPage.document.write("	        elemInfo.style.left  = xLeft + \"px\";");
        	printPage.document.write("	        elemInfo.innerHTML = \"內部限閱, 請勿外洩\" +");
        	printPage.document.write("	                             \"<br><span style='font-size:\" + fSize2 + \"pt';>\" + userName + \"</span>\";");
        	printPage.document.write("		     if(window.attachEvent){"+"\n");
        	printPage.document.write("	          var rad = (-45 * Math.PI) / 180.0;  ");
        	printPage.document.write("	          var cos = Math.cos(rad);");
        	printPage.document.write("	          var sin = Math.sin(rad); ");
        	printPage.document.write("	          var filter = \"progid:DXImageTransform.Microsoft.Matrix(sizingMethod='auto expand', M11 = \" + cos + \", M12 = \" + (-sin) + \", M21 = \" + sin + \", M22 = \" + cos + \")\"; ");
        	printPage.document.write("	          elemInfo.style.filter = filter;");
        	printPage.document.write("	        }else{");
        	printPage.document.write("	          elemInfo.style.MozTransform = \"rotate(-45deg)\";");
        	printPage.document.write("	          elemInfo.style.webkitTransform = \"rotate(-45deg)\";");
        	printPage.document.write("	        }");
        	printPage.document.write("	        xElement.insertBefore(elemInfo, DataBody);");
        	printPage.document.write("	      }  ");
        	printPage.document.write("	    }");
        	printPage.document.write("	    xHeight += sHeight;");
        	printPage.document.write("	  }");
        	printPage.document.write("	  window.print();");        	
        	//printPage.document.write("	  window.close();");
        	printPage.document.write("	}");
        	printPage.document.write("</script>	");
        	printPage.document.close("</BODY></HTML>");
    	}
    }
    
}//root function