package com.scsb.crpro.fileio;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.vaadin.server.DownloadStream;
import com.vaadin.server.StreamResource;

public class DynamicStreamResource extends StreamResource {

  private static final long serialVersionUID = -4304057799149311779L;
 
  public static final String MIME_TYPE_BINARY_DATA = "application/octet-stream";
  public static final String MIME_TYPE_PDF = "application/pdf";


  private final byte[] binaryData;

  private final String filename;

  public DynamicStreamResource(final byte[] binaryData, final String filename,  final String mimeType) {
    super(new StreamSource() {
      public InputStream getStream() {
        return new ByteArrayInputStream(binaryData);
      }
    }, filename);
   
    this.binaryData = binaryData;
    this.filename = filename;
   
    setMIMEType(mimeType);
  }
 
  @Override
  public DownloadStream getStream() {
    final DownloadStream downloadStream = super.getStream();
    // Set the "attachment" to force save-dialog. Important for IE7 (and probably IE8)
    try {
		downloadStream.setParameter("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(filename, "utf-8") + "\"");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    // Enable deterministic progressbar for download
    downloadStream.setParameter("Content-Length", Integer.toString(binaryData.length));
    return downloadStream;
  }

}