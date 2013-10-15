package eu.opentxs.bridge.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ApplicationProperties;
import eu.opentxs.bridge.core.Console.ConsoleApplication;

public class ConsoleFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(ConsoleFrame.class);

	public static void main(String[] args) {
		// new ConsoleFrame(Interpreter.class);
		new ConsoleFrame(OpenTransactions.class);
	}

	private Console console;

	public ConsoleFrame(Class<? extends ConsoleApplication> application) {
		super();
		console = new Console();
		console.setForeground(new Color(ApplicationProperties.get().getInteger(
				"console.color.in")));
		console.setBackground(new Color(ApplicationProperties.get().getInteger(
				"console.background")));
		console.setFont(new Font(ApplicationProperties.get().getString(
				"console.font.name"), (ApplicationProperties.get().getBoolean(
				"console.font.bold") ? Font.BOLD : Font.PLAIN), ApplicationProperties
				.get().getInteger("console.font.size")));
		console.setOutColor(new Color(ApplicationProperties.get().getInteger(
				"console.color.out")));
		console.setErrColor(new Color(ApplicationProperties.get().getInteger(
				"console.color.err")));

		JScrollPane scrollPane = new JScrollPane(console);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane);
		add(scrollPane);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(
				ApplicationProperties.get().getInteger("console.inset.horizontal"),
				ApplicationProperties.get().getInteger("console.inset.vertical"),
				screenSize.width
						- ApplicationProperties.get().getInteger(
								"console.inset.horizontal") * 2,
				screenSize.height
						- ApplicationProperties.get().getInteger(
								"console.inset.vertical") * 2);
		setSize(ApplicationProperties.get().getInteger("console.width"),
				ApplicationProperties.get().getInteger("console.height"));

		setVisible(true);

		try {
			logger.info(String.format("Loading %s, console application ...",
					application.getSimpleName()));
			ConsoleApplication instance = application.newInstance();
			logger.info("Console application loaded. Connecting streams, ect.");
			console.connectStdStreams();
			setTitle(instance.getTitle());
			instance.run(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Console getConsole() {
		return console;
	}
}
