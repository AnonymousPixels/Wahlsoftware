package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * A simple class for a simple log
 * 
 * @version 1.0
 * @author Maximilian von Gaisberg
 * @author Felix Beutter
 *
 */
public class Log {

	static File file;
	static boolean timestamp;
	SimpleDateFormat dateFormat;

	/**
	 * Constructor of 'Log' class, which creates the log file
	 * 
	 * @param file
	 *            The File where the Log should be saved to
	 * @param useSameFile
	 *            If false, there will be a new file for every launch
	 * @param timestamp
	 *            If true, there will be a timestamp in front of every entry
	 * @throws IOException
	 */
	public Log(File file, boolean useSameFile, boolean useTimestamp) {

		dateFormat = new SimpleDateFormat("dd.MM.yyyy_hh:mm:ss");
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Log.timestamp = useTimestamp;

		if (!useSameFile)
			Log.file = new File(file.getPath() + "_" + dateFormat.format(time) + ".txt");
		else
			Log.file = file;

		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Couldn't log to file.");
				e.printStackTrace();
			}
	}

	/**
	 * Add a new entry to the logfile
	 * 
	 * @param text
	 *            The String, that will be written into the log file
	 * @return False, if an IOException has occurred
	 */
	public boolean log(String text) {

		try {

			Timestamp time = new Timestamp(System.currentTimeMillis());
			FileWriter out = new FileWriter(file, true);
			if (timestamp) {
				out.append((CharSequence) dateFormat.format(time) + " ");
				System.out.println(dateFormat.format(time) + " ");
			}

			out.append(text + "\n");
			out.close();
			System.out.println(text);

		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Add a new debug entry to the logfile
	 * 
	 * @param text
	 *            The String, that will be written into the log file
	 * @return False, if an IOException has occurred
	 */
	public boolean debug(String text) {

		text = "DEBUG: " + text;

		try {

			Timestamp time = new Timestamp(System.currentTimeMillis());
			FileWriter out = new FileWriter(file, true);
			if (timestamp) {
				out.append((CharSequence) dateFormat.format(time) + " ");
				System.out.println(dateFormat.format(time) + " ");
			}

			out.append(text + "\n");
			out.close();
			System.out.println(text);

		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Add a new info entry to the logfile
	 * 
	 * @param text
	 *            The String, that will be written into the log file
	 * @return False, if an IOException has occurred
	 */
	public boolean info(String text) {

		text = "INFO: " + text;

		try {

			Timestamp time = new Timestamp(System.currentTimeMillis());
			FileWriter out = new FileWriter(file, true);
			if (timestamp) {
				out.append((CharSequence) dateFormat.format(time) + " ");
				System.out.println(dateFormat.format(time) + " ");
			}

			out.append(text + "\n");
			out.close();
			System.out.println(text);

		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Add a new warning entry to the logfile
	 * 
	 * @param text
	 *            The String, that will be written into the log file
	 * @return False, if an IOException has occurred
	 */
	public boolean warning(String text) {

		text = "WARNING: " + text;

		try {

			Timestamp time = new Timestamp(System.currentTimeMillis());
			FileWriter out = new FileWriter(file, true);
			if (timestamp) {
				out.append((CharSequence) dateFormat.format(time) + " ");
				System.out.println(dateFormat.format(time) + " ");
			}

			out.append(text + "\n");
			out.close();
			System.out.println(text);

		} catch (IOException e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}

	/**
	 * Add a new StackTrace to the logfile
	 * 
	 * @param e
	 *            The Exception, whose StackTrace should be logged
	 * @return False, if an IOException has occurred
	 */
	public boolean logStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		try {
			Timestamp time = new Timestamp(System.currentTimeMillis());

			FileWriter out = new FileWriter(file, true);
			if (timestamp) {
				out.append((CharSequence) dateFormat.format(time) + " ");
				System.out.println(dateFormat.format(time) + "  ");
			}
			String s = sw.toString();
			out.append(s + "\n");
			out.close();
			System.err.println(s);

		} catch (IOException f) {
			f.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Add a new error entry to the logfile
	 * 
	 * @param text
	 *            The String, that will be written into the log file
	 * @return False, if an IOException has occurred
	 */
	public boolean error(String text) {

		text = "ERROR: " + text;

		try {

			Timestamp time = new Timestamp(System.currentTimeMillis());
			FileWriter out = new FileWriter(file, true);
			if (timestamp) {
				out.append((CharSequence) dateFormat.format(time) + " ");
				System.out.println(dateFormat.format(time) + " ");
			}

			out.append(text + "\n");
			out.close();
			System.out.println(text);

		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public boolean fatal(String text) {

		text = "FATAL ERROR: " + text;

		try {

			Timestamp time = new Timestamp(System.currentTimeMillis());
			FileWriter out = new FileWriter(file, true);
			if (timestamp) {
				out.append((CharSequence) dateFormat.format(time) + " ");
				System.out.println(dateFormat.format(time) + " ");
			}

			out.append(text + "\n");
			out.close();
			System.out.println(text);

		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
