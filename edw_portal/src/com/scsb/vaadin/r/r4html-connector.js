mylib=this.com_scsb_vaadin_r_R4Html =function() {	
	    // Create the component
	    var setComponent = new buHtml.setComponent(this.getElement());
	    
	    // Handle changes from the server-side
	    this.onStateChange = function() {
	    	setComponent.setValue(this.getState().value ,this.getState().tagName);
	    	setComponent.setPrint(this.getState().ynPrint ,this.getState().WaterMarkName ,this.getState().value);
	    	
	    };
	    // Pass user interaction to the server-side
	    var connector = this;
	    setComponent.click = function() {
	    	connector.onClick(setComponent.getValue());
	    };
	    
};
/*******************************************************************************************/
//Define the namespace
var buHtml = buHtml || {};

buHtml.setComponent = function (element) {
    this.element = element;
    this.element.innerHTML ="";
	
	var defPage ="<div id='PrintPage' style=\"z-index: 0; position :relative;\">" +
	"<div id=\"DataBody\" style=\"z-index: 2; position :relative;\">NO DATA</div>" +
	"</div>";	
	
	var arr_name =new Array();
	var arr_value =new Array();
	// Getter and setter for the value property
	this.getValue = function () {
		var arr =new Array(arr_name ,arr_value);
		return arr
	};

	this.setValue = function (value ,tagName) {
		this.element.innerHTML=defPage;
        var Page =this.element.getElementsByTagName("div")[0];
        var DataBody =Page.getElementsByTagName("div")[0];    	
    	DataBody.innerHTML =  value;		
		
		//this.element.innerHTML=value;
		var els = DataBody.getElementsByTagName(tagName);
		var els_length = els.length;
		for (var i = 0, l = els_length; i < l; i++) {
		    var el = els[i];
		    el.onclick=function(){
		    	var arrFlag=0;
		    	for (var x = 1, y =99 ; x<y; x++) {
		    		//if (el.hasAttribute("name"+x)){
		    		var  elName=el.getAttribute("name"+x);
		    		if (elName != null){
		    			arr_name[arrFlag]=elName;
		    			arr_value[arrFlag]=el.getAttribute("value"+x);
		    			arrFlag++;
		    		}//if
		    	}//for
		    	self.click();
		    };//onclick
		}//for	
	};//setValue

	// Default implementation of the click handler
	this.click = function () {
		alert("Error: Must implement click() method");
	};

	var self = this; // Can't use this inside the function
	
	 this.setPrint =function(ynPrint ,watermark ,value){
	    	if (ynPrint=="Y"){
	        	var printPage = window.open("","printPage","");
	        	printPage.document.open();
	        	printPage.document.write("<HTML><head></head>");
	        	printPage.document.write("<BODY onload='setWaterMark(\""+watermark+"\");'>");        	
	        	//printPage.document.write("<BODY>");
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
	        	//printPage.document.write("	  window.print();");        	
	        	//printPage.document.write("	  window.close();");
	        	printPage.document.write("	}");
	        	printPage.document.write("</script>	");
	        	printPage.document.close("</BODY></HTML>");
	    	}
	    }		 
};


