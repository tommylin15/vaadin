package com.scsb.vaadin.r.temp;

import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.scsb.vaadin.r.temp.RVector.Type;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class RContainer {

	public boolean verboseErrors = true;
	private RConnection rc = null;
	private long imageCount = 0;
	private String sessionID = "";
	private Semaphore rSemaphore = new Semaphore(1);
	private Random rand = null;

	/* for generating PDF files with Cairo */
	//private int screen_dpi = 75;
	//private boolean showButtonsInGraph = false;

	/* for generating XLSX files with Apache POI */
	//SpreadSheetFactory ssf = null;

	public RContainer() {

		try {
			rc = new RConnection();
			//rc.parseAndEval("library('Cairo')");

		} catch (Exception e) {
			if (verboseErrors) {
				Notification.show("RVaadin: Could not connect to R.",
						Notification.Type.ERROR_MESSAGE);
			}
			e.printStackTrace();
		}

		/*
		 * Random session identifier for this particular R connection (used to
		 * e.g. tell the browser not to cache the images)
		 */
		rand = new Random((new GregorianCalendar()).getTimeInMillis());
		String chars = "abcdefghijklmnopqrstuvwxyz";
		sessionID = getRandomString(rand, chars, 10);

	}

	/**
	 * <p>
	 * The remote version of Rcontainer. For remote access, you need to have a
	 * remote server running with e.g.<br>
	 * <br>
	 * {@code $ R CMD Rserve --RS-settings}<br>
	 * {@code config file: /etc/Rserv.conf}<br>
	 * {@code port: 6311}<br>
	 * {@code authorization required: yes}<br>
	 * {@code passwords file: /etc/Rserv.pwd}<br>
	 * <br>
	 * where the /etc/Rserv.conf must read:<br>
	 * {@code pwdfile /etc/Rserv.pwd}<br>
	 * {@code remote enable}<br>
	 * {@code auth required}<br>
	 * <br>
	 * and /etc/Rserv.pwd can read e.g. <br>
	 * {@code arho oprwcxpp7043}
	 * </p>
	 * 
	 * @param app
	 *            Vaadin Application reference. Needed in the construction of
	 *            StreamResource objects for R graphics.
	 * @param host
	 *            The full host name Rserve runs (e.g. myhost.mysite.com)
	 * @param port
	 *            Port to the host (e.g. 6311)
	 */
	public RContainer(String host, int port) {

		try {
			rc = new RConnection(host, port);

		} catch (Exception e) {
			if (verboseErrors) {
				Notification.show("RVaadin: Could not connect to R.",
						Notification.Type.ERROR_MESSAGE);
			}
			e.printStackTrace();
		}

		/*
		 * Random session identifier for this particular R connection (used to
		 * e.g. tell the browser not to cache the images)
		 */
		rand = new Random((new GregorianCalendar()).getTimeInMillis());
		String chars = "abcdefghijklmnopqrstuvwxyz";
		sessionID = getRandomString(rand, chars, 10);

	}

	/**
	 * If Rserve is configured to require authentication, the "login" needs to
	 * be the first function to be called for a fresh RContainer.
	 * 
	 * 
	 * @param user
	 * @param passwd
	 * @return boolean result (success = true)
	 */
	public boolean login(String user, String passwd) {

		try {
			rSemaphore.acquire();
			rc.login(user, passwd);

			/* We need Cairo, but can only load that after login */
			//rc.parseAndEval("require('Cairo')");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			rSemaphore.release();
		}

		return true;
	}

	/**
	 * Get the raw Rconnection object and hold it locked. The lock must be
	 * explicitly released with {@link RContainer#releaseRConnection()}.
	 */
	public RConnection getRConnection() {
		try {
			rSemaphore.acquire();
			return rc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Set error messages visible via Vaadin main window.
	 * 
	 * @param verboseErrors
	 */
	public void setVerboseErrors(boolean verboseErrors) {
		this.verboseErrors = verboseErrors;
	}

	/**
	 * Release the RConnection obtained with getRConnection
	 */
	public void releaseRConnection() {
		rSemaphore.release();
	}

	/**
	 * This is a thread-safe version of parsing and evaluating an R expression.
	 * 
	 * @param rs
	 *            R string to be evaluated.
	 * @return The resulting R expression object REXP.
	 */
	public REXP eval(String rs) {

		try {
			return tryEval(rs);

		} catch (Exception e) {
			/*
			 * Something happened. The underlying tryEval will complain and
			 * inform the user about it.
			 */
			return null;

		}
	}

	/**
	 * This is a thread-safe version of parsing and evaluating an R expression
	 * which also throws Exceptions, if something fails.
	 * 
	 * @param rs
	 *            R string to be evaluated.
	 * @return The resulting R expression object REXP.
	 * @throws Exception
	 */
	public REXP tryEval(String rs) throws Exception {
		try {
			rSemaphore.acquire();
			return rc.parseAndEval(rs);

		} catch (RserveException rse) {
			/* RserveException (transport layer, e.g. Rserve is not running) */
			System.err.println(rse);
			if (verboseErrors) {
				Notification.show(
						"RVaadin tryEval Error: Rserve is not running.",
						Notification.Type.TRAY_NOTIFICATION);
			}
			throw rse;

		} catch (REXPMismatchException mme) {
			/* REXP mismatch exception */
			System.err.println(mme);

			if (verboseErrors) {
				Notification.show(
						"RVaadin tryEval Error: REXP mismatch exception.",
						Notification.Type.TRAY_NOTIFICATION);
			}
			throw mme;

		} catch (Exception e) {
			/* something else, including ordinary parse errors */
			System.err.println(e.getMessage());
			e.printStackTrace();

			if (verboseErrors) {
				Notification.show("RVaadin tryEval Error: " + e.getMessage(),
						Notification.Type.TRAY_NOTIFICATION);
			}
			try {
				rc.parseAndEval("cat('RVaadin:', '" + e.getMessage()
						+ "', '\n')");

			} catch (Exception yae) {
				yae.printStackTrace();
			}
			throw e;

		} finally {
			rSemaphore.release();
		}
	}

	/**
	 * Parse and evaluate an R expression and return the results as String, if
	 * possible. In case of any error, the return value will be "null" and
	 * StackTraces are printed to the Server standard error stream.
	 * 
	 * @param rs
	 *            R string to be evaluated.
	 * @return String result
	 */
	public String getString(String rs) {

		String result = null;

		try {
			result = tryEval(rs).asString();
		} catch (REXPMismatchException e) {
			/* The response cannot be presented a string */
			showREXPMismatchMessage();
			e.printStackTrace();

		} catch (Exception e) {
			/* Something else */
			showGeneralRError();
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Parse and evaluate an R expression and return the resulting character
	 * vector as String[], if possible. In case of any error, the return value
	 * will be "null" and StackTraces are printed to the Server standard error
	 * stream.
	 * 
	 * @param rs
	 *            R string to be evaluated.
	 * @return String[] result
	 */
	public String[] getStrings(String rs) {

		String[] result = null;

		try {
			result = tryEval(rs).asStrings();
		} catch (REXPMismatchException e) {
			/* The response cannot be presented a string */
			showREXPMismatchMessage();
			e.printStackTrace();

		} catch (Exception e) {
			/* Something else */
			showGeneralRError();
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * <p>
	 * Parse and evaluate an R expression and return the resulting value as (the
	 * Java primitive) double, if possible.
	 * </p>
	 * 
	 * <p>
	 * Be careful with errors, as they are indicated with the special value
	 * "-1.0", which is indeed also a valid double.
	 * </p>
	 * 
	 * @param rs
	 *            R string to be evaluated.
	 * @return the double result wrapped in a Double class
	 */
	public double getdouble(String rs) {

		double d = -1.0;

		try {
			d = (double) tryEval(rs).asDouble();

		} catch (REXPMismatchException e) {
			showREXPMismatchMessage();
			e.printStackTrace();
		} catch (Exception e) {
			showGeneralRError();
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * Parse and evaluate an R expression and return the resulting value as
	 * Double, if possible. In case of any error, the return value will be
	 * "null" and StackTraces are printed to the Server standard error stream.
	 * 
	 * @param rs
	 *            R string to be evaluated.
	 * @return the double result wrapped in a Double class
	 */
	public Double getDouble(String rs) {

		Double d = null;

		try {
			d = tryEval(rs).asDouble();

		} catch (REXPMismatchException e) {
			showREXPMismatchMessage();
			e.printStackTrace();
		} catch (Exception e) {
			showGeneralRError();
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * Parse and evaluate an R expression and return the resulting vector as
	 * double[], if possible. In case of any error, the return value will be
	 * "null" and StackTraces are printed to the Server standard error stream.
	 * 
	 * @param rs
	 *            R string to be evaluated.
	 * @return double[] result
	 */
	public double[] getDoubles(String rs) {

		double d[] = null;

		try {
			d = tryEval(rs).asDoubles();

		} catch (REXPMismatchException e) {
			showREXPMismatchMessage();
			e.printStackTrace();
		} catch (Exception e) {
			showGeneralRError();
			e.printStackTrace();
		}

		return d;
	}

	/**
	 * <p>
	 * Parse and evaluate an R expression and return the resulting value as
	 * integer, if possible.
	 * </p>
	 * 
	 * <p>
	 * Be careful with errors, as they are indicated with the special value
	 * 'Integer.MIN_VALUE', which is indeed also an integer.
	 * </p>
	 * 
	 * @param rs
	 * @return integer result
	 */
	public int getInt(String rs) {
		int i = Integer.MIN_VALUE;

		try {
			i = tryEval(rs).asInteger();
		} catch (REXPMismatchException e) {
			showREXPMismatchMessage();
			e.printStackTrace();
		} catch (Exception e) {
			showGeneralRError();
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * <p>
	 * Parse and evaluate an R expression and return the resulting value as
	 * int[], if possible. In case of any error, the return value will be "null"
	 * and StackTraces are printed to the Server standard error stream.
	 * </p>
	 * 
	 * @param rs
	 * @return int[] result
	 */
	public int[] getInts(String rs) {
		int[] i = null;

		try {
			i = tryEval(rs).asIntegers();
		} catch (REXPMismatchException e) {
			showREXPMismatchMessage();
			e.printStackTrace();
		} catch (Exception e) {
			showGeneralRError();
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * <p>
	 * Parse and evaluate an R expression and return the resulting data.frame as
	 * ArrayList<RVector>, if possible. The object types in the ArrayList will
	 * be either double[], string[] or int[]. Pure integer vectors are not very
	 * common in R, but one can constructed with as.integer(). Logical vectors
	 * are not supported, since the is no way or representing missing values in
	 * boolean[].
	 * </p>
	 * 
	 * <p>
	 * Missing values ('NA' in R) will be {@code null} for Strings,
	 * {@code Double.NaN} for doubles, and {@code Integer.MIN_VALUE} for the int
	 * type.
	 * </p>
	 * 
	 * <p>
	 * In case of any error, the return value will be "null" and StackTraces are
	 * printed to the Server standard error stream.
	 * </p>
	 * 
	 * @param rs
	 *            R string to be evaluated.
	 * @return ArrayList<Object> result
	 */
	public DataFrame getDataFrame(String rs) {

		DataFrame df = new DataFrame();

		try {

			/*
			 * Evaluate the expression and save it in a new variable. If the
			 * result is not a data frame, stop.
			 */
			tryEval(".RVaadinDf <- " + rs);
			if (!isTRUE("is.data.frame(.RVaadinDf)")) {
				if (verboseErrors) {
					Notification.show("RVaadin: Not a data.frame",
							Notification.Type.ERROR_MESSAGE);
				}
				return null;
			}

			/* Get the dimensions of the data frame */
			int ncol = getInt("ncol(.RVaadinDf)");

			/* Construct the List column-by-column. */
			for (int j = 1; j <= ncol; j++) {

				if (isTRUE("is.integer(.RVaadinDf[[" + j + "]])")) {
					df.add(new RVector(getInts(".RVaadinDf[[" + j + "]]")));

				} else if (isTRUE("is.numeric(.RVaadinDf[[" + j + "]])")) {
					df.add(new RVector(getDoubles(".RVaadinDf[[" + j + "]]")));

				} else if (isTRUE("is.factor(.RVaadinDf[[" + j + "]])")) {
					df.add(new RVector(getStrings("as.character(.RVaadinDf[["
							+ j + "]])")));

				} else if (isTRUE("is.character(.RVaadinDf[[" + j + "]])")) {
					df.add(new RVector(getStrings(".RVaadinDf[[" + j + "]]")));

				} else if (isTRUE("class(.RVaadinDf[[" + j + "]]) == 'Date'")) {
					/*
					 * Also cast R Date objects into character
					 */
					df.add(new RVector(getStrings("as.character(.RVaadinDf[["
							+ j + "]])")));

				} else if (isTRUE("is.logical(.RVaadinDf[[" + j + "]])")) {
					/*
					 * There is no way of representing missing logical values.
					 * Doing an explicit conversion to integers.
					 */
					df.add(new RVector(getInts("as.integer(.RVaadinDf[[" + j
							+ "]])")));

				} else {
					/* Should never get here */
					if (verboseErrors) {
						Notification.show("RVaadin: Could not read column " + j
								+ " from " + rs,
								Notification.Type.ERROR_MESSAGE);
					}
					df.add(null);
				}
			}

		} catch (REXPMismatchException e) {
			showREXPMismatchMessage();
			e.printStackTrace();
		} catch (Exception e) {
			showGeneralRError();
			e.printStackTrace();
		}
		return df;
	}

	/**
	 * <p>
	 * The original RConnection API contains several different functions to
	 * assign bytes, integers, doubles, REXP object and Strings to an R
	 * variable. RVaadin implements thread-safe wrappers for String, String[],
	 * double[], Double, int and int[].</b>
	 * 
	 * <p>
	 * Also boolean and boolean[] objects can assigned, but the corresponding
	 * get-methods are not implemented in RVaadin. The reason for this decision
	 * is that there would not be a straightforward way of distinguishing a
	 * missing value (NA) from a logical false.
	 * </p>
	 * 
	 * @param symbol
	 *            R symbol name
	 * @param str
	 *            the value to be assigned
	 * @return success = true or failure = false
	 */
	public boolean assign(String symbol, String str) {
		try {
			rSemaphore.acquire();
			rc.assign(symbol, str);
			return true;
		} catch (Exception e) {
			showGeneralRError();
			e.printStackTrace();
			return false;
		} finally {
			rSemaphore.release();
		}
	}

	/** See {@link RContainer#assign(String, String)} */
	public boolean assign(String symbol, String[] strArray) {
		try {
			rSemaphore.acquire();
			rc.assign(symbol, strArray);
			return true;
		} catch (Exception e) {
			showGeneralRError();
			e.printStackTrace();
			return false;
		} finally {
			rSemaphore.release();
		}
	}

	/** See {@link RContainer#assign(String, String)} */
	public boolean assign(String symbol, double[] dArray) {
		try {
			rSemaphore.acquire();
			rc.assign(symbol, dArray);
			return true;
		} catch (Exception e) {
			showGeneralRError();
			e.printStackTrace();
			return false;
		} finally {
			rSemaphore.release();
		}
	}

	/** See {@link RContainer#assign(String, String)} */
	public boolean assign(String symbol, Double d) {

		double[] dArray = new double[1];
		dArray[0] = d;
		return assign(symbol, dArray);
	}

	/** See {@link RContainer#assign(String, String)} */
	public boolean assign(String symbol, int i) {
		int[] iArray = new int[1];
		iArray[0] = i;
		return assign(symbol, iArray);
	}

	/** See {@link RContainer#assign(String, String)} */
	public boolean assign(String symbol, int[] intArray) {
		try {
			rSemaphore.acquire();
			rc.assign(symbol, intArray);
			return true;
		} catch (Exception e) {
			showGeneralRError();
			e.printStackTrace();
			return false;
		} finally {
			rSemaphore.release();
		}
	}

	/** See {@link RContainer#assign(String, String)}. */
	public boolean assign(String symbol, boolean b) {
		boolean[] bArray = new boolean[1];
		bArray[0] = b;
		return assign(symbol, bArray);
	}

	/** See {@link RContainer#assign(String, String)} */
	public boolean assign(String symbol, boolean[] bArray) {
		try {
			/* Conversion to integer, then assign */
			int[] iArray = new int[bArray.length];

			for (int i = 0; i < bArray.length; i++) {
				if (bArray[i] == true) {
					iArray[i] = 1;
				} else {
					iArray[i] = 0;
				}
			}
			rSemaphore.acquire();
			rc.assign(symbol, iArray);

			/* Conversion to logical on the R side */
			rc.eval(symbol + " <- as.logical(" + symbol + ")");
			return true;

		} catch (Exception e) {
			showGeneralRError();
			e.printStackTrace();
			return false;
		} finally {
			rSemaphore.release();
		}
	}

	/**
	 * <p>
	 * Store a DataFrame into R data.frame and return true in case of success.
	 * </p>
	 * 
	 * <p>
	 * The list presentation mimics R internal way of storing data.frame objects
	 * as lists with vectors of different type, but same length. The RVector
	 * objects the ArrayList can refer to double[], string[], int[],
	 * boolean[],... but all of them need to be equal in length.
	 * </p>
	 * 
	 * <p>
	 * To assign 'NA' (missing data in R) for a scalar, use {@code null} for
	 * Strings, {@code Double.NaN} for doubles, and {@code Integer.MIN_VALUE}
	 * for the int type.
	 * </p>
	 * 
	 * <p>
	 * In case of any error, the return value will be false and StackTraces are
	 * printed to the Server standard error stream.
	 * </p>
	 * 
	 * @see {@link RContainer#getDataFrame(String)}
	 * @param symbol
	 * @param dataFrame
	 * @param colNames
	 * @return
	 */
	public boolean assign(String symbol, DataFrame dataFrame, String[] colNames) {
		try {

			int ncol = dataFrame.size();
			StringBuilder RCall = new StringBuilder(symbol + " <- data.frame(");

			/*
			 * Construct a bunch of individual variable in the R workspace to be
			 * collected into a data.frame
			 */
			for (int j = 0; j < ncol; j++) {
				String RColName = ".RVaadin_X" + j;

				if (dataFrame.get(j).type() == Type.INTEGER) {
					assign(RColName, dataFrame.get(j).getInts());

				} else if (dataFrame.get(j).type() == Type.NUMERIC) {
					assign(RColName, dataFrame.get(j).getdoubles());

				} else if (dataFrame.get(j).type() == Type.CHARACTER) {
					assign(RColName, dataFrame.get(j).getStrings());

				} else {
					/* Unsupported type! Assign "Unsupported */
					assign(RColName, "Unsupported");
				}

				/* Build the R call */
				if (j < ncol - 1) {
					RCall.append(RColName + ", ");
				} else {
					RCall.append(RColName + ")");
				}
			}

			/*
			 * Evaluate the call and then clean the temporal variables from the
			 * R workspace
			 */
			eval(RCall.toString());

			for (int j = 0; j < ncol; j++) {
				eval("rm(.RVaadin_X" + j + ")");
			}

			/*
			 * Assign column names, if given
			 */
			if (colNames != null) {
				StringBuilder cnc = new StringBuilder("colnames(" + symbol
						+ ") <- c(");

				for (int j = 0; j < colNames.length - 1; j++) {
					cnc.append("'" + colNames[j] + "',");
				}
				cnc.append("'" + colNames[colNames.length - 1] + "')");
				eval(cnc.toString());
			}

			return true;

		} catch (Exception e) {
			showGeneralRError();
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <p>
	 * Store a DataFrame into R data.frame and return true in case of success.
	 * </p>
	 * 
	 * @see {@link RContainer#assign(String, DataFrame, String[])}
	 * @param symbol
	 *            R variable name
	 * @param dataFrame
	 *            DataFrame object
	 * @return
	 */
	public boolean assign(String symbol, DataFrame dataFrame) {
		return (assign(symbol, dataFrame, null));
	}

	/**
	 * Evaluates an R expression, which must return either TRUE of FALSE, and
	 * returns the corresponding Java boolean value (true / false).
	 * 
	 * @param rs
	 *            R string to be evaluated.
	 * @return Java boolean result.
	 */
	public boolean isTRUE(String rs) {

		boolean result = false;
		try {
			REXP res = tryEval(rs);
			if (res.isLogical()) {
				REXPLogical resLog = (REXPLogical) res;
				boolean[] boolVec = resLog.isTRUE();
				if (boolVec[0]) {
					result = true;
				}
			}

		} catch (Exception e) {
			showGeneralRError();
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Get a Vaadin Embedded image object that contains the corresponding R plot
	 * generated by submitting and evaluating the RPlotCall String. The
	 * imageName corresponds to the downloadable .png image name. The full name
	 * of the images shown in the browser are
	 * imageName_ISODate_runningId_[sessionId].png, e.g.
	 * 'Image_2012-08-29_001_[bmgolmfgvk].png' to keep them chronologically
	 * ordered.
	 * 
	 * @param RPlotCall
	 *            the String to be evaluated by R
	 * @param width
	 *            plot width in pixels
	 * @param height
	 *            plot height in pixels
	 * @return The image as Embedded Vaadin object
	 */
	public Embedded getEmbeddedGraph(String RPlotCall, int width, int height,
			String imageName) {

		StreamResource imageresource = getImageResource(RPlotCall, width,
				height, imageName, "png");

		/*
		 * Create an embedded component that gets its contents from the
		 * resource.
		 */
		return new Embedded(null, imageresource);
	}

	/**
	 * This function is the actual workhorse for
	 * {@link RContainer#getEmbeddedGraph}. It gets a Vaadin StreamResource
	 * object that contains the corresponding R plot generated by submitting and
	 * evaluating the RPlotCall String. The imageName corresponds to the
	 * downloadable image name, and device is the format supported by the Cairo
	 * graphics engine. The full name of the images shown in the browser are
	 * imageName_ISODate_runningId_[sessionId].[device], e.g.
	 * 'Image_2012-08-29_001_[bmgolmfgvk].png' to keep them chronologically
	 * ordered.
	 * 
	 * @param RPlotCall
	 *            the String to be evaluated by R
	 * @param width
	 *            plot width in pixels
	 * @param height
	 *            plot height in pixels
	 * @param device
	 *            A plot device supported by Cairo ('png','pdf',...)
	 * @return The image as Embedded Vaadin object
	 */
	public StreamResource getImageResource(String RPlotCall, int width,
			int height, String imageName, String device) {

		StreamSource imagesource = new RImageSource(rc, RPlotCall, width,
				height, rSemaphore, device);

		/* Get a systematic name for the image */
		imageCount++;
		String fileName = imageName + "_" + getDateAndCount(imageCount) + "_["
				+ sessionID + "]." + device;

		/*
		 * Create a resource that uses the stream source and give it a name. The
		 * constructor will automatically register the resource with the
		 * application.
		 */
		StreamResource imageresource = new StreamResource(imagesource, fileName);

		/*
		 * Instruct browser not to cache the image. See the Book of Vaadin 6
		 * page 131.
		 */
		imageresource.setCacheTime(0);
		return imageresource;
	}

	/**
	 * <p>
	 * Evaluate a graph command in R and return the corresponding Scalable
	 * Vector Graphics (svg) image via 'gridSVG' package.
	 * </p>
	 * 
	 * <p>The gridSVG package enables using 'garnish' and other modern SVG options.
	 * The image must be drawn by using the grid graphics engine. For
	 * traditional graphics, use
	 * {@link RContainer#getSvgString(String, int, int, Integer)}.</p>
	 * 
	 * @param RPlotCall
	 *            the String to be evaluated by R
	 * @param width
	 *            the width of the image in inches (as reported by R)
	 * @param height
	 *            the height of the image in inches (as reported by R)
	 * @param res
	 *            Resolution parameter for R. If null, defaults to 72 dpi
	 * @return The svg image as String
	 */
	public String getGridSvgString(String RPlotCall, int width, int height,
			Integer res) {

		String svgString = null;

		if (res == null) {
			/* Default resolution in R */
			res = 72;
		}

		/* The argument name=NULL calls gridSVG to construct the image in memory */
		String openGridCall = "gridsvg( name=NULL, res=" + res + ", width="
				+ width + " ,height=" + height + " );";

		/* Here we need functions from the XML package */
		String getSvgCall = "saveXML(dev.off()$svg)";

		try {
			rSemaphore.acquire();

			int supportInstalled = rc.parseAndEval(
					"as.integer( require('grid') & "
							+ "require('gridSVG') & require('XML') )")
					.asInteger();

			if (supportInstalled != 1) {
				Notification.show("RVaadin: Either 'gridSVG', 'XML' or 'grid' "
						+ "packages cannot be loaded.",
						Notification.Type.TRAY_NOTIFICATION);
				throw new Exception("Missing R packages.");
			}

			rc.parseAndEval(openGridCall);
			rc.parseAndEval(RPlotCall);
			svgString = rc.parseAndEval(getSvgCall).asString();

		} catch (Exception e) {
			if (verboseErrors) {
				Notification.show("RVaadin: "
						+ "Is the graphics based on grid?",
						Notification.Type.TRAY_NOTIFICATION);
				showGeneralRError();
			}
			e.printStackTrace();

		} finally {
			rSemaphore.release();
		}

		return svgString;
	}

	/**
	 * <p>Evaluate a graph command in R and return the corresponding Scalable
	 * Vector Graphics (svg) image via Cairo.</p> 
	 * 
	 * <p>The image is exported via the 'svg' function, which is sometimes faster
	 * than 'gridSVG' and works also with traditional graphics. In turn, modern
	 * svg functionality does not work (and some images that use vectorized grid
	 * commands might not work). To use 'gridSVG', call
	 * {@link RContainer#getGridSvgString(String, int, int, Integer)}.
	 * 
	 * @param RPlotCall
	 *            the String to be evaluated by R
	 * @param width
	 *            the width of the image in inches (as reported by R)
	 * @param height
	 *            the height of the image in inches (as reported by R)
	 * @param res
	 *            Resolution parameter for R. If null, defaults to 72 dpi
	 * @return The svg image as String
	 */
	public String getSvgString(String RPlotCall, int width, int height,
			Integer pointsize) {

		String svgString = null;

		if (pointsize == null) {
			/* Default resolution in R */
			pointsize = 12;
		}

		try {
			rSemaphore.acquire();

			rc.parseAndEval("svg( filename='tmp.svg', width=" + width
					+ ",height=" + height + ", pointsize=" + pointsize + " )");

			/* Plot the image and write it temporarily to disk */
			rc.parseAndEval(RPlotCall);
			rc.parseAndEval("dev.off()");

			svgString = rc.parseAndEval(
					"readChar('tmp.svg', " + "file.info('tmp.svg')$size)")
					.asString();

			rc.parseAndEval("unlink('tmp.svg')");

		} catch (Exception e) {
			if (verboseErrors) {
				showGeneralRError();
			}
			e.printStackTrace();

		} finally {
			rSemaphore.release();
		}

		return svgString;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 */
	public RTable getRTable(String rs) {

		try {
			tryEval(".RVaadinDfToTable <- " + rs);
			String[] colNames = getStrings("colnames(.RVaadinDfToTable)");
			DataFrame df = getDataFrame(".RVaadinDfToTable");

			return new RTable(df, colNames);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get a ready-made RUpload element for submitting arbitrary files to the R
	 * working directory. See also the {@link RUpload} class.
	 * 
	 * @param caption
	 *            Caption text
	 * @return RUpload object extending Vaadin CustomComponent
	 */
	public RUpload getUploadElement(String caption) {
		return new RUpload(caption, this);
	}

	/**
	 * Get a Vaadin Resource object which links to a file in the R working
	 * directory. The Resource object can be associated e.g. with Links.
	 * 
	 * @param filename
	 *            The name of the file stored under R current working directory.
	 * @return Vaadin Resource object
	 */
	public Resource getDownloadResource(String filename) {

		StreamSource filesource = new RDownloadSource(filename, this);
		StreamResource fileresource = new StreamResource(filesource, filename);

		fileresource.setCacheTime(0);
		return fileresource;
	}

	/**
	 * Get a Vaadin Link object associated with a file in the R working
	 * directory.
	 * 
	 * @param linkCaption
	 * @param filename
	 * @return Vaadin link object
	 */
	public Link getDownloadLink(String linkCaption, String filename) {

		Link link = new Link();
		/* Open the link in a new window */
		link.setTargetName("_blank");

		link.setCaption(linkCaption);
		link.setResource(getDownloadResource(filename));
		return link;
	}

	/**
	 * <p>
	 * Returns a Vaadin OptionGroup object with an implicit ValueChangeListener
	 * that writes the selected options directly to the R workspace. See
	 * {@link RContainer#getListSelect} for examples, and how to (re)set the
	 * values and set the listener explicitly.
	 * </p>
	 * 
	 * @param optionsInName
	 *            The R character vector name to contain the different choices.
	 *            Repeated elements will be ignored.
	 * @param selectedOutName
	 *            The R character vector to immediately write the selection to.
	 * @return Vaadin OptionGroup object.
	 */
	public OptionGroup getOptionGroup(String optionsInName,
			final String selectedOutName) {

		final OptionGroup optiongroup = new OptionGroup();
		optiongroup.setNullSelectionAllowed(false);

		buildSelectOptions(optiongroup, optionsInName);
		buildSelectListener(optiongroup, selectedOutName);

		return optiongroup;
	}

	/**
	 * <p>
	 * Returns a Vaadin ComboBox object with an implicit ValueChangeListener
	 * that writes the selected options directly to the R workspace. See
	 * {@link RContainer#getListSelect} for examples, and how to (re)set the
	 * values and set the listener explicitly.
	 * </p>
	 * 
	 * @param optionsInName
	 *            The R character vector name to contain the different choices.
	 *            Repeated elements will be ignored.
	 * @param selectedOutName
	 *            The R character variable to immediately write the selection
	 *            to.
	 * @return Vaadin ComboBox object.
	 */
	public ComboBox getComboBox(String optionsInName,
			final String selectedOutName) {

		final ComboBox combo = new ComboBox();
		combo.setNullSelectionAllowed(false);

		buildSelectOptions(combo, optionsInName);
		buildSelectListener(combo, selectedOutName);

		return combo;
	}

	/**
	 * <p>
	 * Returns a Vaadin TwinColSelect object with an implicit
	 * ValueChangeListener that writes the selected options directly to the R
	 * workspace. See {@link RContainer#getListSelect} for examples, and how to
	 * (re)set the values and set the listener explicitly.
	 * </p>
	 * 
	 * @param optionsInName
	 *            The R character vector name to contain the different choices.
	 *            Repeated elements will be ignored.
	 * @param selectedOutName
	 *            The R character variable to immediately write the selection
	 *            to.
	 * @return Vaadin TwinColSelect object.
	 */
	public TwinColSelect getTwinColSelect(String optionsInName,
			final String selectedOutName) {

		final TwinColSelect tcs = new TwinColSelect();

		buildSelectOptions(tcs, optionsInName);
		buildSelectListener(tcs, selectedOutName);

		return tcs;
	}

	/**
	 * <p>
	 * Returns a Vaadin ListSelect object with an implicit ValueChangeListener
	 * that writes the selected options directly to the R workspace. See also
	 * {@link RContainer#getOptionGroup}.
	 * </p>
	 * 
	 * <p>
	 * For example, if R workspace contains the variable
	 * {@code classes <- c("A", "B", "C")"}, then
	 * {@code getListSelect("classes", "selected")} returns a ListSelect element
	 * which automatically updates the variable {@code "selected"} in R. The
	 * value can be e.g. {@code "A"}, or {@code c("A", "C")}, if we have in
	 * addition chosen {@code setMultiSelect(true)} for the element.
	 * </p>
	 * 
	 * <p>
	 * The input options can be (re)set any time with
	 * {@link RContainer#buildSelectOptions}
	 * </p>
	 * 
	 * <p>
	 * Observe that the separate element can be constructed step by step:<br>
	 * {@code  R.eval("classes <- c('A', 'B', 'C')");} <br>
	 * {@code ListSelect chooseGroups = new ListSelect();} <br>
	 * {@code R.buildSelectOptions(chooseGroups, "classes");}<br>
	 * {@code R.buildSelectListener(chooseGroups, "selected");}
	 * </p>
	 * 
	 * 
	 * @param optionsInName
	 *            The R character vector name to contain the different choices.
	 *            Repeated elements will be ignored.
	 * @param selectedOutName
	 *            The R character vector to immediately write the selection to.
	 * @return Vaadin ListSelect object
	 */
	public ListSelect getListSelect(String optionsInName,
			final String selectedOutName) {

		final ListSelect listselect = new ListSelect();
		listselect.setNullSelectionAllowed(false);

		buildSelectOptions(listselect, optionsInName);
		buildSelectListener(listselect, selectedOutName);

		return listselect;
	}

	public ListSelect getListSelect(final String selectedOutName) {

		final ListSelect listselect = new ListSelect();

		return listselect;

	}

	/**
	 * A workhorse for {@link RContainer#getOptionGroup} and
	 * {@link RContainer#getListSelect} to initiate the selection options by
	 * reading them from an R character vector.
	 * 
	 * @param selectObj
	 *            Selection object
	 * @param optionsInName
	 *            The R character vector to read the different options. Repeated
	 *            values are ignored.
	 */
	public void buildSelectOptions(final AbstractSelect selectObj,
			String optionsInName) {

		/* Clean the possible old values */
		selectObj.removeAllItems();

		String[] optionsIn = getStrings(optionsInName);
		if (optionsIn == null) {
			Notification.show("RVaadin: Cannot find selection element"
					+ " values from " + optionsInName,
					Notification.Type.ERROR_MESSAGE);

		} else {
			for (String str : optionsIn) {
				selectObj.addItem(str);
			}
		}
	}

	/**
	 * A workhorse for {@link RContainer#getOptionGroup} and
	 * {@link RContainer#getListSelect} to add the AbstractSelect objects a
	 * default listener.
	 * 
	 * @param selectObj
	 *            The name of the character vector in R to read the different
	 *            options.
	 * @param selectedOutName
	 *            The character vector name in R to store the results. If the
	 *            variable does not exists in R namespace, it will be created
	 *            with the initial values character(0).
	 */
	public void buildSelectListener(final AbstractSelect selectObj,
			final String selectedOutName) {

		/*
		 * Check whether the given element already exists in the R workspace. If
		 * not, initiate it as character(0) (i.e. vector of length zero).
		 */
		eval("if( !exists('" + selectedOutName + "') ) { " + selectedOutName
				+ " <- character(0) }");

		/*
		 * Listen to the AbstractSelect component, and write the selection to
		 * the chosen R character vector
		 */
		ValueChangeListener optionsChanged = new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {

				if (selectObj.getValue() == null) {
					eval(selectedOutName + " <- character(0)");
				} else {

					/* The action depends on selection mode */
					if (selectObj.isMultiSelect()) {
						String[] selection = StringSetAsList(selectObj
								.getValue());
						assign(selectedOutName, selection);

					} else {
						/* Single selection mode! */
						String selection = selectObj.getValue().toString();
						assign(selectedOutName, selection);
					}
				}
			}
		};

		selectObj.addValueChangeListener(optionsChanged);
		selectObj.setImmediate(true);
	}

	/**
	 * Check whether a selection element is empty independent of the selection
	 * mode. Works both with setMultiSelect(true) and setMultiSelect(false).
	 * 
	 * @param selectObj
	 *            AsbtractSelect object
	 * @return true if nothing is selected
	 */
	public boolean isSelectionEmpty(AbstractSelect selectObj) {

		/* The action depends on selection mode */
		if (selectObj.isMultiSelect()) {

			@SuppressWarnings("unchecked")
			Set<String> strSet = (Set<String>) selectObj.getValue();
			return strSet.isEmpty();

		} else {
			/* Single selection mode: If empty, the value is null. */
			return (null == selectObj.getValue());
		}
	}

	/**
	 * <p>
	 * A function that returns a ready-made HorizontalLayout element to fill in
	 * and submit numerical parameters to R. This element is best used e.g. as
	 * FormLayout row. The caption parameter is any explanatory String, and
	 * parName is the corresponding numeric vector's name in R.
	 * </p>
	 * 
	 * <p>
	 * For example, after the statement {@code p <- c(0,0.4,2)} in R, we can
	 * initialize the layout element with
	 * {@code getParameterLayout("Origin", "p")}, and the parameter values in
	 * {@code p} get automatically updated when the user edits those values in
	 * the UI component. Values that cannot be interpreted as numbers are
	 * considered illegal: The old (and valid) value will be returned in that
	 * case. There is also a variant of this function that accepts a boolean
	 * vector of enabled and disabled components (with the same length as the
	 * parameters).
	 * </p>
	 * 
	 * @param caption
	 *            Parameter name
	 * @param parName
	 *            The numeric vector name (of 1..N elements) in R
	 * @return HorizontalLayout element with implicit ValueChangeListeners to
	 *         update R
	 */
	public HorizontalLayout getParameterLayout(String caption,
			final String parName) {

		return getParameterLayout(caption, parName, null);

	}

	/**
	 * For general description, see
	 * {@link RContainer#getParameterLayout(String, String)}.
	 * 
	 * @param caption
	 *            Parameter name
	 * @param parName
	 *            The numeric vector name (of 1..N elements) in R
	 * @param enabled
	 *            Is the field enabled by default?
	 * @return orizontalLayout element with implicit ValueChangeListeners to
	 *         update R
	 */
	public HorizontalLayout getParameterLayout(String caption,
			final String parName, final boolean[] enabled) {

		/* The horizontal layout to place the parameters [p1, p2, p3,..., pN] */
		final HorizontalLayout hl = new HorizontalLayout();
		hl.setCaption(caption);
		hl.setSpacing(true);

		/* TextFields serve as the input field for the parameters */
		final TextField[] pFields;

		/* Get the default values for the parameter vector */
		double[] p = getDoubles(parName);

		/*
		 * Show the parameter names in the TextField caption, in case that they
		 * were defined in R
		 */
		boolean namedPars = false;
		String[] names = null;
		if (isTRUE("!is.null(names(" + parName + "))")) {
			namedPars = true;
			names = getStrings("names(" + parName + ")");
		}

		if (p != null) {

			/*
			 * Construct TextField(s) for the parameter vector and set the
			 * initial values
			 */
			pFields = new TextField[p.length];
			for (int i = 0; i < pFields.length; i++) {

				pFields[i] = new TextField();
				pFields[i].setValue(Double.toString(p[i]));

				if (enabled != null) {
					pFields[i].setEnabled(enabled[i]);
				}

				/*
				 * Nice default width for a numeric parameter field. This could
				 * actually be a parameter for this call.
				 */
				pFields[i].setWidth("10ex");

				pFields[i].setImmediate(true);

				/*
				 * In case that the parameters are named, show the name before
				 * the text field
				 */
				if (namedPars) {
					pFields[i].setCaption(names[i]);
				}

				hl.addComponent(pFields[i]);
			}

			/*
			 * Set up a listener which can be attached to all the parameter
			 * fields
			 */

			ValueChangeListener parChanged = new ValueChangeListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {

					/* Get the Property which fired the listener */
					TextField tf = ((TextField) event.getProperty());

					/* Find the corresponding parameter index */
					int idx;
					for (idx = 0; idx < pFields.length; idx++) {
						if (tf == pFields[idx]) {
							break;
						}
					}

					/*
					 * Try to update the new value. If it is illegal (cannot be
					 * cast to Double), then stick with the old value and
					 * display it to the user to indicate erroneous input.
					 */

					double newValue;
					try {
						newValue = Double.valueOf(tf.getValue().toString());

						/*
						 * Submit the new value to R, remembering that the
						 * indexing is off by one between Java and R
						 */
						eval(parName + "[" + (idx + 1) + "] <- " + newValue);

					} catch (Exception e) {

						/*
						 * The parameter was illegal. Do nothing for R, and
						 * display the old value to indicate that the new value
						 * was not eligible.
						 */

						Double oldPar = getDouble(parName + "[" + (idx + 1)
								+ "]");
						tf.setValue(Double.toString(oldPar));
					}
				}
			};

			/* Attach this listener to all TextField items */
			for (int i = 0; i < pFields.length; i++) {
				pFields[i].addValueChangeListener(parChanged);
			}

		} else {
			/* The original parameters were not readable as double[] */

			hl.addComponent(new Label("Error reading the parameters"));
			hl.setEnabled(false);
		}

		return hl;
	}

	/**
	 * A function to get the values from a parameter layout generated by
	 * {@link RContainer#getParameterLayout}. This function ban be useful when
	 * e.g. the range of the parameters is only a subset of R^n (say, we must
	 * always have p[0] <= p[1]). The parameters can be forced to match the
	 * domain with {@link RContainer#setParameterLayoutValues}.
	 * 
	 * @param hl
	 *            HorizontalLayout obtained from
	 *            {@link RContainer#getParameterLayout}
	 * @return the parameter values in the layout as double[]
	 */
	public double[] getParameterLayoutValues(HorizontalLayout hl) {

		int n = countParametersInLayout(hl);

		/*
		 * The parameter layout listener takes care that no illegal value can be
		 * added to the list. Hence, it is safe to loop over the values and cast
		 * them to doubles.
		 */
		double p[] = new double[n];

		for (int i = 0; i < n; i++) {
			p[i] = Double.valueOf(((TextField) hl.getComponent(i)).getValue()
					.toString());
		}

		return p;
	}

	public TextField[] getParameterLayoutTextFields(HorizontalLayout hl) {

		if (hl != null) {
			int n = countParametersInLayout(hl);
			TextField[] tft = new TextField[n];

			/* Get and return the TextField elements in a one-dimensional table */
			for (int i = 0; i < n; i++) {
				tft[i] = (TextField) hl.getComponent(i);
			}
			return tft;
		} else {
			return null;
		}
	}

	/**
	 * A helper function to count how many TextFields there are in a parameter
	 * layout.
	 * 
	 * @param hl
	 *            {@link HorizontalLayout} corresponding to a parameter layout.
	 * @return integer count
	 */
	private int countParametersInLayout(HorizontalLayout hl) {

		/* How many parameters we have in this layout? */
		int i;
		for (i = 1; i <= hl.getComponentCount(); i++) {
			if (!(hl.getComponent(i - 1) instanceof TextField)) {
				/* Index i does not any more contain a TextField */
				i--;
				break;
			}
		}

		/*
		 * Now we know that there are i parameters in total (if someone has not
		 * added an extra TextField into the layout once it was constructed).
		 */
		return i;
	}

	/**
	 * A function to set parameter values in the element generated by
	 * {@link RContainer#getParameterLayout}.
	 * 
	 * @param p
	 *            double[] parameter vector
	 * @param hl
	 *            HorizontalLayout obtained from
	 *            {@link RContainer#getParameterLayout}
	 */
	public void setParameterLayoutValues(double[] p, HorizontalLayout hl) {

		for (int i = 0; i < p.length; i++) {
			((TextField) hl.getComponent(i)).setValue(Double.toString(p[i]));
		}
	}

	/**
	 * A function to set parameter values in the element generated by
	 * {@link RContainer#getParameterLayout}.
	 * 
	 * @param parName
	 *            The numeric vector name (of 1..N elements) in R
	 * @param hl
	 *            HorizontalLayout obtained from
	 *            {@link RContainer#getParameterLayout}
	 */
	public void setParameterLayoutValues(String parName, HorizontalLayout hl) {

		double[] p = getDoubles(parName);
		setParameterLayoutValues(p, hl);
	}

	/**
	 * The function returns a slider as defined in {@link com.vaadin.ui.Slider}
	 * 
	 * @param parName
	 *            The R parameter name to bind the slider with
	 * @param min
	 *            The minimum value of the slider
	 * @param max
	 *            The maximum value of the slider
	 * @param resolution
	 *            The number of digits after the decimal point
	 * @return Vaadin {@link com.vaadin.ui.Slider} element
	 */
	public Slider getSlider(final String parName, double min, double max,
			int resolution) {

		Slider slider = new Slider(min, max, resolution);
		buildSlider(parName, slider);

		return slider;
	}

	/**
	 * The function returns a slider as defined in {@link com.vaadin.ui.Slider}
	 * 
	 * @param caption
	 *            The slider caption
	 * @param parName
	 *            The R parameter name to bind the slider with
	 * @param min
	 *            The minimum value of the slider
	 * @param max
	 *            The maximum value of the slider
	 * @param resolution
	 *            The number of digits after the decimal point
	 * @return Vaadin {@link com.vaadin.ui.Slider} element
	 */
	public Slider getSlider(String caption, final String parName, double min,
			double max, int resolution) {

		Slider slider = new Slider(min, max, resolution);
		buildSlider(parName, slider);
		slider.setCaption(caption);

		return slider;
	}

	/**
	 * The function returns a slider as defined in {@link com.vaadin.ui.Slider}
	 * 
	 * @param parName
	 *            The R parameter name to bind the slider with
	 * @param min
	 *            The minimum value of the slider
	 * @param max
	 *            The maximum value of the slider
	 * @return Vaadin {@link com.vaadin.ui.Slider} element
	 */
	public Slider getSlider(final String parName, int min, int max) {

		Slider slider = new Slider(min, max);
		buildSlider(parName, slider);

		return slider;
	}

	/**
	 * The function returns a slider as defined in {@link com.vaadin.ui.Slider}
	 * 
	 * @param caption
	 *            The slider caption
	 * @param parName
	 *            The R parameter name to bind the slider with
	 * @param min
	 *            The minimum value of the slider
	 * @param max
	 *            The maximum value of the slider
	 * @return Vaadin {@link com.vaadin.ui.Slider} element
	 */
	public Slider getSlider(String caption, final String parName, int min,
			int max) {

		Slider slider = new Slider(min, max);
		buildSlider(parName, slider);
		slider.setCaption(caption);

		return slider;
	}

	/**
	 * Internal function to build differently parameterized Sliders for
	 * {@link RContainer#getSlider(String, double, double, int)} and
	 * {@link RContainer#getSlider(String, int, int)}.
	 * 
	 * @param parName
	 * @param slider
	 */
	private void buildSlider(final String parName, Slider slider) {
		Double parValue = getDouble(parName);
		try {
			slider.setValue(parValue);
			assign(parName, parValue);

		} catch (ValueOutOfBoundsException e) {
			/*
			 * The value must be between the min and max, and we indicate that
			 * by disabling the slider
			 */
			slider.setEnabled(false);
		}

		ValueChangeListener sliderChanged = new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Double val = (Double) event.getProperty().getValue();
				assign(parName, val);
			}
		};
		slider.addValueChangeListener(sliderChanged);
		slider.setImmediate(true);
	}

	/**
	 * Random character string. Adapted from:
	 * http://stackoverflow.com/questions/2863852/how
	 * -to-generate-a-random-string-in-java
	 * 
	 * @param rng
	 * @param characters
	 * @param length
	 */
	private static String getRandomString(Random rng, String characters,
			int length) {
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}
		return new String(text);
	}

	/**
	 * Get a nice String presentation of date with an increasing counter, e.g.
	 * "2012-09-03_002"
	 * 
	 * @param counter
	 *            A counter variable of type long
	 * @return The counter string
	 */
	public static String getDateAndCount(long counter) {

		StringBuffer dateAndCount = new StringBuffer();

		/* Date in the ISO format, e.g. 2012-08-21 */
		Calendar calendar = Calendar.getInstance();

		dateAndCount.append(calendar.get(Calendar.YEAR));
		dateAndCount.append("-");
		int month = calendar.get(Calendar.MONTH) + 1;
		if (month < 10) {
			dateAndCount.append("0" + month);
		} else {
			dateAndCount.append(month);
		}
		dateAndCount.append("-");
		int day = calendar.get(Calendar.DATE);
		if (day < 10) {
			dateAndCount.append("0" + day);
		} else {
			dateAndCount.append(day);
		}

		dateAndCount.append("_");

		/* Running image count number to help keeping track of the saved images */
		Formatter fr = new Formatter(dateAndCount);
		fr.format("%1$03d", counter);
		fr.close();

		return dateAndCount.toString();
	}

	/**
	 * A Static Helper function to convert a set of strings from Vaadin multiple
	 * selection into an array of Strings
	 * 
	 * @param obj
	 *            An Object pointing to a Set of Strings
	 * @return value String[]
	 */
	@SuppressWarnings("unchecked")
	public static String[] StringSetAsList(Object obj) {

		Set<String> strSet = (Set<String>) obj;
		String[] values = new String[strSet.size()];
		int i = 0;
		for (String s : strSet) {
			values[i] = s;
			i++;
		}
		return (values);
	}

	/**
	 * Close and finalize the R Session.
	 */
	public void close() {
		rc.close();
	}

	/**
	 * Close and finalize the R Session. This implementation calls
	 * unlink(list.files()) in the current working directory to clean up the
	 * temporary files. The user should not change the working directory during
	 * the session, or otherwise this call will delete whatever it can on the
	 * current location using the rights of the running Rserve instance.
	 */
	public void closeAndDeleteFiles() {
		/*
		 * Manually uploaded elements need to be deleted from the session
		 * directory. The close call only removes directories which are not
		 * empty.
		 */
		try {
			rc.parseAndEval("unlink(list.files())");
		} catch (Exception e) {
			/* The session directory was left hanging */
			e.printStackTrace();
		} finally {
			rc.close();
		}
	}

	private void showREXPMismatchMessage() {
		if (verboseErrors) {
			Notification.show("RVaadin: R <-> Java type conversion error.",
					Notification.Type.WARNING_MESSAGE);
		}
	}

	private void showGeneralRError() {
		if (verboseErrors) {
			Notification.show("RVaadin: R execution error.",
					Notification.Type.WARNING_MESSAGE);
		}
	}
}
