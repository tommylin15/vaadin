
package com.scsb.vaadin.r.temp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;

import com.vaadin.server.StreamResource.StreamSource;

public class RDownloadSource implements StreamSource {

	private static final long serialVersionUID = 1L;

	private RContainer R;
	private RConnection rc;
	private String filename = null;

	public RDownloadSource(String filename, RContainer R) {
		this.R = R;
		this.filename = filename;
	}

	@Override
	public InputStream getStream() {
		try {
			rc = R.getRConnection();

			/* We use R to turn the file into a raw binary stream. First we need
			 * to ask how large the file is, and then read it with 'readBin' */
			long fileSizeBytes = rc.parseAndEval(
					"file.info('" + filename + "')$size").asInteger();
			REXP xp = rc.parseAndEval("r <- readBin('" + filename + "','raw',"
					+ fileSizeBytes + "); r");

			return new ByteArrayInputStream(xp.asBytes());

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			/* Finally release the connection-specific semaphore in all possible
			 * cases */
			R.releaseRConnection();
		}
	}
}
