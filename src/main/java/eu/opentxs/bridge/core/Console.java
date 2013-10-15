package eu.opentxs.bridge.core;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import eu.ApplicationProperties;

public class Console extends JTextPane {

	public static abstract class ConsoleApplication {
		public static final char ESCAPE = 27;
		public static final char UP = 11;// ^q
		public static final char DOWN = 12;// ^r
		public static BufferedReader bis = null;

		public static String readLineFromConsole() {
			String line = null;
			try {
				line = bis.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return line;
		}

		public abstract void close();

		public String getTitle() {
			return getClass().getSimpleName();
		}

		public abstract void run(ConsoleFrame console);
	}

	private class ConsoleDocument extends DefaultStyledDocument {
		private static final long serialVersionUID = 1L;

		public ConsoleDocument(StyleContext sc) {
			super(sc);
		}

		@Override
		public void insertString(int offset, String string, AttributeSet arg2) throws BadLocationException {

			if (isInputStream)
				arg2 = styleIn.copyAttributes();

			String name = (String) arg2.getAttribute(StyleConstants.NameAttribute);
			if (!isEscaped && !isKbdOpen && styleInName.equals(name))
				return;
			isEscaped = false;

			if (isPasteIn) {
				paste = Interpreter.escapeNewLines(getClipboardContents());
				string = "^b ";
				isPasteIn = false;
			}
			if (isPasteOn) {
				string = paste = Interpreter.escapeNewLines(getClipboardContents());
				isPasteOn = false;
			}
			if (string.equals("\n"))// lg6
				offset = getLength();
			if (offset >= changeLimit && getLength() >= changeLimit)
				super.insertString(offset, string, arg2);
		}

		@Override
		public void remove(int offset, int length) throws BadLocationException {
			if (offset >= changeLimit && getLength() > changeLimit)
				super.remove(offset, length);
		}
	}

	/**
	 * After the document length is greater than lengthMax, it is truncated at its beginning. Truncated length is lengthTruncated.
	 */
	public static final int lengthMax = 100000;
	public static final int lengthTruncated = (int) (0.75 * lengthMax);

	protected static final char CTR_V = 22;
	protected static final char PASTE_IN = 2;
	protected static final char CR = 13;
	protected static final char NL = 10;
	protected static final char ESCAPE = 27;
	private static final float LEFT_INDENT = 0;
	private static final float RIGHT_INDENT = 0;
	private static final float FIRST_LINE_INDENT = 0;
	private final PipedInputStream inPipe = new PipedInputStream();

	private final PipedInputStream outPipe = new PipedInputStream();
	private final PipedInputStream errPipe = new PipedInputStream();
	private PrintWriter in;
	private String paste;
	private int changeLimit;
	private ConsoleApplication consoleApplication;
	private static final long serialVersionUID = 1L;

	/**
	 * If set, styleInput attributes are activated in the insertString method.
	 */
	private boolean isInputStream = false;
	private Style styleIn;
	private StyledDocument doc;
	private Style styleOut;
	private Style styleErr;
	private String styleInName = "in";
	/**
	 * After sending a line into stdIn stream, the flag is set FALSE blocking the keyboard. The flag is be reset if the ESCAPE sign arrives into the
	 * stdErr stream.
	 */
	private boolean isKbdOpen = false;
	private boolean isEscaped = false;
	private boolean isPasteIn = false;
	private boolean isPasteOn = false;
	private boolean isArrow = false;

	public Console() {
		super();

		setCaretColor(new Color(ApplicationProperties.get().getInteger("console.color.out")));
		StyleContext sc = new StyleContext();
		Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
		styleIn = sc.addStyle(styleInName, defaultStyle);
		styleOut = sc.addStyle("out", null);
		styleErr = sc.addStyle("err", null);

		doc = new ConsoleDocument(sc);
		setStyledDocument(doc);

		addKeyListener(new KeyAdapter() {

			private void isArrow() {
				if (isArrow) {
					try {
						doc.remove(changeLimit, doc.getLength() - changeLimit);
					} catch (BadLocationException e) {
						throw new RuntimeException(e);
					}
				}
				isArrow = true;
			}

			@Override
			public void keyPressed(KeyEvent e) {

				truncate();

				if (!isKbdOpen)
					e.consume();

				isInputStream = true;
				switch (e.getKeyCode()) {
					case 38 :// ArrowUp
						isArrow();
						in.println(ConsoleApplication.UP);
						e.consume();
						break;
					case 40 :// ArrowDown
						isArrow();
						in.println(ConsoleApplication.DOWN);
						e.consume();
						break;
				}

				switch (e.getKeyChar()) {
					case CTR_V :// ^v
						isPasteOn = true;
						break;
					case PASTE_IN :// ^b
						isPasteIn = true;
						break;
				}
				super.keyPressed(e);
			}

			@Override
			public void keyTyped(KeyEvent e) {
				switch (e.getKeyChar()) {
					case PASTE_IN :// ^b
						in.println(paste);
						isKbdOpen = false;
						isInputStream = false;
						break;
					case CR :// \r
						break;
					case NL :// \n
						try {
							if (isArrow)
								isArrow = false;
							in.println(doc.getText(changeLimit, doc.getLength() - changeLimit).trim());
							isInputStream = false;
							setCaretPosition(getDocument().getLength());
						} catch (BadLocationException e1) {
							throw new RuntimeException(e1);
						}
						isKbdOpen = false;
						;
						break;
					case ESCAPE :
						in.println(ConsoleApplication.ESCAPE);
						isEscaped = true;
						isKbdOpen = false;
						isInputStream = false;
						isArrow = false;
						break;
					default :
				}
			}
		});

	}

	@Override
	public void addNotify() {
		init();
		super.addNotify();
	}

	public void connectStdStreams() {
		System.setIn(inPipe);
		try {
			System.setOut(new PrintStream(new PipedOutputStream(outPipe), true));
			System.setErr(new PrintStream(new PipedOutputStream(errPipe), true));
			in = new PrintWriter(new PipedOutputStream(inPipe), true);

			/** stdOut */
			new SwingWorker<Void, Character>() {

				@Override
				protected Void doInBackground() throws Exception {
					int ch;
					while ((ch = outPipe.read()) != -1)
						if (ch != CR)
							publish(Character.valueOf((char) ch));
					return null;
				}

				@Override
				protected void process(List<Character> chunks) {
					for (Character chunk : chunks)
						try {
							isKbdOpen = true;
							switch (chunk) {
								default :
									doc.insertString(doc.getLength(), String.valueOf(chunk), styleOut);
									if (!isArrow)
										changeLimit = doc.getLength();
							}
						} catch (BadLocationException e) {
							throw new RuntimeException(e);
						}
				}

			}.execute();

			/** stdErr */
			new SwingWorker<Void, Character>() {
				@Override
				protected Void doInBackground() throws Exception {
					int ch;
					while ((ch = errPipe.read()) != -1)
						publish(Character.valueOf((char) ch));
					return null;
				}

				@Override
				protected void process(List<Character> chunks) {
					for (Character chunk : chunks)
						try {
							doc.insertString(doc.getLength(), String.valueOf(chunk), styleErr);
							changeLimit = doc.getLength();
						} catch (BadLocationException e) {
							throw new RuntimeException(e);
						}
				}

			}.execute();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getClipboardContents() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex) {
				// highly unlikely since we are using a standard DataFlavor
				System.out.println(ex);
				ex.printStackTrace();
			} catch (IOException ex) {
				System.out.println(ex);
				ex.printStackTrace();
			}
		}
		return result;
	}

	private void init() {
		StyleConstants.setLeftIndent(styleIn, LEFT_INDENT);
		StyleConstants.setRightIndent(styleIn, RIGHT_INDENT);
		StyleConstants.setFirstLineIndent(styleIn, FIRST_LINE_INDENT);
		StyleConstants.setFontSize(styleIn, getFont().getSize());
		StyleConstants.setBackground(styleIn, getBackground());
		StyleConstants.setForeground(styleIn, getForeground());
		StyleConstants.setBackground(styleOut, getBackground());
		StyleConstants.setBackground(styleErr, getBackground());
	}

	@Override
	public void removeNotify() {
		if (consoleApplication != null)
			consoleApplication.close();
		super.removeNotify();
	}

	public void setConsoleApplication(ConsoleApplication consoleApplication) {
		this.consoleApplication = consoleApplication;
	}

	public void setErrColor(Color color) {
		StyleConstants.setForeground(styleErr, color);
	}

	public void setOutColor(Color color) {
		StyleConstants.setForeground(styleOut, color);
	}

	private void truncate() {
		int changeLimitNew;
		if (doc.getLength() > lengthMax)
			try {
				int length = doc.getLength() - lengthTruncated;
				changeLimitNew = changeLimit - length;
				changeLimit = 0;
				doc.remove(0, length);
				changeLimit = changeLimitNew;
			} catch (BadLocationException e) {
				throw new RuntimeException(e);
			}
	}
}