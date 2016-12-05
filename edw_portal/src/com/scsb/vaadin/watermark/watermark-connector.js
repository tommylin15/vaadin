window.com_scsb_vaadin_watermark_WaterMark4Div =
function() {
    // Create the component
    var watermarkComponent =new watermark.WaterMark4Div(this.getElement());
    
    // Handle changes from the server-side
    this.onStateChange = function() {     	
    	watermarkComponent.setValue(this.getState().value);
    	watermarkComponent.setWaterMark(this.getState().WaterMarkName);
    	watermarkComponent.setPrint(this.getState().ynPrint ,this.getState().WaterMarkName ,this.getState().value);
    };
    // Pass user interaction to the server-side
    /*
    var connector = this;
    watermarkComponent.getPrint = function() {
    	connector.onReturn(watermarkComponent.getPrintValue());
    };  
    */
};