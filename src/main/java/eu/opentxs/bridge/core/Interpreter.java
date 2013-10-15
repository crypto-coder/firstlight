package eu.opentxs.bridge.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import eu.ApplicationProperties;
import eu.opentxs.bridge.Text;
import eu.opentxs.bridge.Localizer;
import eu.opentxs.bridge.core.Console.ConsoleApplication;
import eu.opentxs.bridge.core.commands.Command;
import eu.opentxs.bridge.core.commands.Commands;

public class Interpreter extends ConsoleApplication {

	private enum Signal {
		Next,
	}

	public enum Status {
		CommandExpected, NextArgumentExpected,
	}

	public static String escapeNewLines(String s) {
		s = s.replaceAll("\\n", "\\\\n");
		return s;
	}

	public static String restoreNewLines(String s) {
		s = s.replaceAll("\\\\n", "\n");
		return s;
	}

	public static void main(String[] args) {
		new Interpreter().run(null);
		System.exit(0);
	}

	private static final String promptCommand = ">>";
	private static final String promptArgument = "=";
	private static final Localizer local = Localizer.get();

	private Status status;
	private String line;
	private String[] arguments;
	private int argumentIndex;
	private Command command;
	private ConsoleFrame console;
	private List<String> history = new ArrayList<>();
	private int historyIndex;

	@Override
	public void run(ConsoleFrame console) {
		this.console = console;
		if (console != null)
			console.getConsole().setConsoleApplication(this);
		bis = new BufferedReader(new InputStreamReader(System.in));
		status = Status.CommandExpected;
		printPrompt(promptCommand);
		readLine();
	}

	private void manageHistory() {
		String commandName = ApplicationProperties.get().getBoolean("console.history.fullNames") ? command.getNameLocal() : command.getPseudoLocal();
		if (history.size() == 0 || !history.contains(commandName)) {
			history.add(0, commandName);
		} else {
			history.remove(commandName);
			history.add(0, commandName);
		}
		historyIndex = -1;
	}

	private String getFromHistoryUp(boolean up) {
		if (history.size() == 0)
			return "";
		if (up)
			historyIndex++;
		else
			historyIndex--;
		if (up && historyIndex >= history.size())
			historyIndex = 0;
		if (!up && historyIndex < 0)
			historyIndex = history.size() - 1;
		String retval = history.get(historyIndex);
		return retval;
	}

	private void readLine() {
		try {
			while (true) {
				this.line = bis.readLine();
				setSignal(Signal.Next);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void printArrorPrompt(String command) {
		if (console != null) {
			Object lock = new Object();
			synchronized (lock) {
				try {
					lock.wait(200);
				} catch (InterruptedException e) {
				}
			}
		}
		System.out.print(command);
	}

	private void printPrompt(String prompt) {
		if (console != null) {
			Object lock = new Object();
			synchronized (lock) {
				try {
					lock.wait(200);
				} catch (InterruptedException e) {
				}
			}
		}
		System.out.print(prompt);
	}

	private void setSignal(Signal signal) {
		switch (status) {
			case CommandExpected : {
				arguments = null;
				if (signal.equals(Signal.Next)) {
					if (line.indexOf(ESCAPE) != -1) {
						System.err.println();
						System.err.println(Text.COMMAND_ABORTED);
						status = Status.CommandExpected;
						printPrompt(promptCommand);
						break;
					}
					if (line.indexOf(UP) != -1) {
						status = Status.CommandExpected;
						printArrorPrompt(getFromHistoryUp(true));
						break;
					}
					if (line.indexOf(DOWN) != -1) {
						status = Status.CommandExpected;
						printArrorPrompt(getFromHistoryUp(false));
						break;
					}
					if (line.equals("")) {
						printPrompt(promptCommand);
						break;
					}

					String commandName = local.getKeyForValue(line);
					if (commandName != null) {
						int pseudo = commandName.indexOf(".pseudo");
						if (pseudo != -1)
							commandName = commandName.substring(0, pseudo);
						command = Commands.commands.get(commandName);
					} else
						command = Commands.commands.get(line);

					if (command == null) {
						System.err.println(Text.COMMAND_UNKNOWN);
						status = Status.CommandExpected;
						printPrompt(promptCommand);
						break;
					}
					manageHistory();
					if (!command.sanityCheck()) {
						status = Status.CommandExpected;
						printPrompt(promptCommand);
						break;
					}
					if (arguments != null) {
						command.actionResult(arguments);
						status = Status.CommandExpected;
						printPrompt(promptCommand);
						break;
					}
					arguments = command.getArguments();
					if (arguments == null) {
						command.actionResult(arguments);
						status = Status.CommandExpected;
						printPrompt(promptCommand);
					} else {
						argumentIndex = 0;
						status = Status.NextArgumentExpected;
						while (argumentIndex < arguments.length && !command.introduceArgument(argumentIndex)) {
							arguments[argumentIndex++] = null;
						}
						if (argumentIndex < arguments.length) {
							printPrompt(getArgumentPrompt());
						} else {
							command.actionResult(arguments);
							status = Status.CommandExpected;
							printPrompt(promptCommand);
						}
					}
				}
				break;
			}
			case NextArgumentExpected : {
				if (signal.equals(Signal.Next)) {
					if (line.indexOf(ESCAPE) != -1) {
						System.err.println();
						System.err.println(Text.COMMAND_ABORTED);
						status = Status.CommandExpected;
						printPrompt(promptCommand);
						break;
					}
					while (!command.validate(argumentIndex, line)) {
						printPrompt(getArgumentPrompt());
						readLine();
					}
					arguments[argumentIndex++] = line;
					if (argumentIndex < arguments.length) {
						while (argumentIndex < arguments.length && !command.introduceArgument(argumentIndex)) {
							arguments[argumentIndex++] = null;
						}
						if (argumentIndex < arguments.length) {
							printPrompt(getArgumentPrompt());
							readLine();
						} else {
							command.actionResult(arguments);
							status = Status.CommandExpected;
							printPrompt(promptCommand);
						}
					} else {
						command.actionResult(arguments);
						status = Status.CommandExpected;
						printPrompt(promptCommand);
					}
				}
				break;
			}
		}
	}

	private String getArgumentPrompt() {
		return String.format("%s%s", arguments[argumentIndex].trim(), promptArgument);
	}

	@Override
	public void close() {
		System.exit(0);
	}
}
