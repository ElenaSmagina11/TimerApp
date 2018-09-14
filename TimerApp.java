import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;
import javazoom.jl.player.Player;
import javazoom.jl.decoder.JavaLayerException;

/**
 * JFrame SWING-based class, which implements the widely known time
 * management Pomodoro-timer technique <a href
 * https://en.wikipedia.org/wiki/Pomodoro_Technique></a>
 * <p>
 * A user can:
 * <ul>
 * <li>choose the number of cycles. Each cycle contains any jobperiod plus coffeepause
 * and lunchtime;
 * <li>set the duration of each period;
 * <li>pause the timer;
 * </ul>
 * <p>
 * The application gives a musical ringtone for starting of each time interval.
 * 
 * @autor Elena Smagina <a href = http:/xing.com/... /a>
 * @version 1.1
 */

public class TimerApp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9010596014349945721L; // eto
																// i
																		// pisat?
	/**
	 * @value cycle is a current value of cycle
	 */
	public int сycle = 0;
	/**
	 * @value isPaused <code>true</code> if timer is paused; <code>false</code>
	 *        otherwise
	 */
	public boolean isPaused = true;

	/**
	 * <p>
	 * Default timer field values:
	 */
	/**
	 * @value jobTimeSetValue duration of the job period;
	 */
	public int jobTimeSetValue = 7;//default values in secunden jetzt nur für demo!
	//muss man spaeter alle values wie jobTimeSetValue * sec machen (int sec = 60;)
	//int sec = 60;
	//jobTimeSetValue = 7 * sec;
	/**
	 * @value pauseTimeSetValue duration of the coffeepause period;
	 */
	public int pauseTimeSetValue = 5; //pauseTimeSetValue = 5 * sec;
	/**
	 * @value lunchTimeSetValue duration of the lunch;
	 */
	public int lunchTimeSetValue = 10;//lunchTimeSetValue = 10 * sec;
	/**
	 * @value сycleMax the number of job-rest interval.
	 */
	public int сycleMax = 4;

	/**
	 * <p>
	 * Current graphic characteristics of frame
	 */
	final Font font = new Font("Calibri", Font.BOLD, 20);
	final Font font2 = new Font("Verdana", Font.PLAIN, 16);
	Color colorTextField = new Color(224, 234, 244);
	Color colorInputField = new Color(160, 200, 235);

	/**
	 * <p>
	 * Frame fields : numberOfCycles textfield for @see сycle jobTime textfield
	 * for @see jobTimeSetValue pauseTime textfield for @see pauseTimeSetValue
	 * lunchTime textfield for @see lunchTimeSetValuele messagesField textlabel
	 * for messages output timeNowLabel textlabel for elapsed time output
	 */

	JFormattedTextField numberOfCycles = new JFormattedTextField(
			NumberFormat.getIntegerInstance());
	JFormattedTextField jobTime = new JFormattedTextField(
			NumberFormat.getIntegerInstance());
	JFormattedTextField pauseTime = new JFormattedTextField(
			NumberFormat.getIntegerInstance());
	JFormattedTextField lunchTime = new JFormattedTextField(
			NumberFormat.getIntegerInstance());

	JLabel messagesField = new JLabel();
	JLabel timeNowLabel = new JLabel();

	/**
	 * <p>
	 * Default timers ringtones files: jobMp3 ringtone for job period pauseMp3
	 * ringtone for coffeepause period lunchMp3 ringtone for lunchtime
	 **/
	String jobMp3 = "C:/Wohnung/EclipseMySpringProject/Timers/src/resources/satisfaction.mp3";
	String pauseMp3 = "C:/Wohnung/EclipseMySpringProject/Timers/src/resources/reggae.mp3";
	String lunchMp3 = "C:/Wohnung/EclipseMySpringProject/Timers/src/resources/metallica-fuel-live-at-grimey_s-2010-cut.mp3";
	/**
	 * Creates a new class types Timer @see java.util.Timer
	 */
	Timer timer = new Timer();

	/**
	 * Creates the main class of the application. An exception is thrown if the startup
	 * fails
	 */
	public TimerApp() throws IOException {

		super("SuperPomodoroTimer");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("/resources/tomato_96806.png")));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/**
		 * Creates container for display application content
		 */
		Container contentPane = this.getContentPane();
		/**
		 * Creates menu for display application specification; @value readMe
		 * contains a text of specification.
		 */
		JMenuBar menuBar = new JMenuBar();
		JMenuItem info = new JMenuItem("?");
		info.setFont(font2);

		final String readMe = "<html> <style>ul{list-style-type: square} </style> "
				+ "<p><pre><strong>     Die Pomodoro-Technik  ist eine Methode des Zeitmanagements</strong></pre>"
				+ "<pre><strong>     zur effektiven Arbeit-Erholung Balance.</strong></pre>"
				+ "<br><pre>   Der Benutzer kann:</pre></br>"
				+ "<pre>   &mdash  die Anzahl der Jobzyklen (Arbeitsperiod + Kaffepause) definieren.  </pre></p>"
				+ "<pre>   &mdash  Dauer jeder Periode in Minuten definieren </pre>"
				+ "<pre>   &mdash  den Timer anhalten</pre>"
				+ "<pre>   &mdash  mit rotem Kreuz die Applikation schließen.</pre>"
				+ "<br><pre><strong>       Häufige Pausen verbessern die geistige Beweglichkeit!</strong><br></pre></html>";

		menuBar.add(info);
		this.setJMenuBar(menuBar);
		/**
		 * Method implements the display of specification in separate window
		 * infoFrame. resetButton is selected
		 */
		info.addActionListener(new ActionListener() {

			JTextPane infoText = new JTextPane();

			@Override
			public void actionPerformed(ActionEvent evt) {

				JDialog infoFrame = new JDialog();
				infoFrame.setVisible(true);
				infoFrame.setSize(610, 410);
				infoFrame.setTitle("Über Pomodoro-Timer");
				infoFrame.setBackground(colorInputField);
				infoFrame.setLocationRelativeTo(rootPane);
				infoFrame.add(infoText);
				infoText.setContentType("text/html");
				infoText.setEditable(false);
				infoText.setBackground(colorTextField);
				infoText.setText(readMe);
			}
		});

		/**
		 * Creates panel to display GUI
		 */
		JPanel content = new JPanel();
		content.setBackground(colorInputField);
		content.setToolTipText("Es gibt 4 Jobzyklen: 4 mal (Arbeitsperiod + KaffePause)");
		contentPane.add(BorderLayout.CENTER, content);

		/**
		 * Label field for display @see numberOfCycles
		 */
		JLabel numberOfCyclesLabel = new JLabel("Anzahl der Zyklen:",
				SwingConstants.RIGHT);
		numberOfCyclesLabel.setFont(font2);
		numberOfCyclesLabel.setPreferredSize(new Dimension(200, 30));
		numberOfCyclesLabel.setAlignmentX(JLabel.WEST);
		content.add(numberOfCyclesLabel);

		numberOfCycles.setPreferredSize(new Dimension(40, 30));
		numberOfCycles.setBackground(colorTextField);
		numberOfCycles.setToolTipText("Zeit in Minuten!");
		numberOfCycles.setValue(сycleMax);
		numberOfCycles.setFont(font2);
		content.add(numberOfCycles);

		/**
		 * Label field for display @see jobTime
		 */
		JLabel jobTimeLabel = new JLabel("Arbeitsperiod:", SwingConstants.RIGHT);
		jobTimeLabel.setFont(font2);
		jobTimeLabel.setPreferredSize(new Dimension(200, 30));
		jobTimeLabel.setAlignmentX(JLabel.WEST);
		content.add(jobTimeLabel);

		jobTime.setPreferredSize(new Dimension(40, 30));
		jobTime.setBackground(colorTextField);
		jobTime.setToolTipText("Zeit in Minuten! aber für demo-version jetzt in Sekunden!!!");
		jobTime.setValue(new Integer(jobTimeSetValue));
		jobTime.setAlignmentX(0.8F);
		jobTime.setFont(font2);
		content.add(jobTime);

		// try {
		// formatter = new MaskFormatter("##");
		// input = new JFormattedTextField(formatter);
		// input.setValue("7");
		// input.setColumns(2);
		// content.add(input);
		// } catch (ParseException e) {
		// System.err.println("Unable to add SSN");
		// }

		// content Pause
		/**
		 * Label field for display @see pauseTime
		 */
		JLabel pauseLabel = new JLabel("JavaKaffeePause:", SwingConstants.RIGHT);
		pauseLabel.setFont(font2);
		pauseLabel.setPreferredSize(new Dimension(200, 40));
		pauseLabel.setAlignmentX(RIGHT_ALIGNMENT);
		content.add(pauseLabel);

		pauseTime.setPreferredSize(new Dimension(40, 30));
		pauseTime.setBackground(colorTextField);
		pauseTime.setToolTipText("Zeit in Minuten!");
		pauseTime.setValue(new Integer(pauseTimeSetValue));
		pauseTime.setFont(font2);
		content.add(pauseTime);

		// content Mittagsause
		/**
		 * Label field for display @see lunchTime
		 */
		JLabel lunch = new JLabel("Mittagspause:", SwingConstants.RIGHT);
		lunch.setFont(font2);
		lunch.setPreferredSize(new Dimension(200, 30));
		lunch.setAlignmentX(RIGHT_ALIGNMENT);
		content.add(lunch);

		lunchTime.setPreferredSize(new Dimension(40, 30));
		lunchTime.setBackground(colorTextField);
		lunchTime.setToolTipText("Zeit in Minuten!");
		lunchTime.setFont(font2);
		content.add(lunchTime);
		lunchTime.setValue(new Integer(lunchTimeSetValue));

		// buttons
		
		/**
		 * The button startButton to run timercycle
		 */
		final JToggleButton startButton = new JToggleButton(new ImageIcon(
				ImageIO.read(getClass().getResource(
						"resources/play1disabledOK.png"))), false);
		startButton.setFont(font);
		startButton.setToolTipText("Timer starten");
		content.add(startButton);

		/**
		 * The button resetButton to reset default values
		 */
		final JToggleButton resetButton = new JToggleButton(new ImageIcon(
				ImageIO.read(getClass().getResource(
						"resources/stop1disabledOK.png"))), false);
		resetButton.setFont(font);
		resetButton.setToolTipText("Zeit im Timer zurücksetzen");
		content.add(resetButton);

		/**
		 * The button pauseButton to pause timer
		 */
		final JToggleButton pauseButton = new JToggleButton(new ImageIcon(
				ImageIO.read(getClass().getResource(
						"resources/pausedisabledOK.png"))), false);
		pauseButton.setFont(font);
		pauseButton.setToolTipText("Timer zu pausieren");
		content.add(pauseButton);

		// / contetnt timer zeigt

		timeNowLabel.setFont(font2); 
		timeNowLabel.setText("00:00:00");
		content.add(timeNowLabel);

		// content messages

		messagesField.setPreferredSize(new Dimension(280, 40));
		messagesField.setFont(font2);
		messagesField.setBackground(new java.awt.Color(000, 000, 000));
		messagesField.setText("Hallo!");
		messagesField.setBackground(new java.awt.Color(222, 222, 222));
		messagesField.setHorizontalAlignment(SwingConstants.CENTER);
		messagesField.setToolTipText("Was möchten Sie machen?");
		content.add(messagesField);

		// Listeners
		/**
		 * Method triggers the operations cleanInput() when a
		 * resetButton is selected
		 */
		resetButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cleanInput();
			}
		});
		/**
		 * Method triggers the operations to pause the Timer after the
		 * pauseButton is pressed
		 */
		pauseButton.addItemListener(new ItemListener() {
			private String untilPause;

			public void itemStateChanged(ItemEvent e) {
			
				if (!isPaused) {
					isPaused = true;
					resetButton.setEnabled(false);
					untilPause = messagesField.getText();
					messagesField.setText("   Timer ist auf Pause");
					pauseButton.setToolTipText("Timer weiter laufen lassen!");
				} else {
					resetButton.setEnabled(false);
					isPaused = false;
					messagesField.setText(untilPause);
				}
			}
		});
		
		/**
		 * Method triggers the operations mainTimer() after the startButton is
		 * pressed
		 */
		startButton.addItemListener(new ItemListener() {

			private String end = "mit rotem Kreuz die App schließen";

			public void itemStateChanged(ItemEvent e) {
				pauseButton.setEnabled(true);
				System.out.println("Poehali!");
				Toolkit.getDefaultToolkit().beep();

				// checkup not null 
				if (checkInput()) {
					mainTimer();
					сycle = 1;
					isPaused = false;
					resetButton.setEnabled(false);
					resetButton.setToolTipText(end);
					startButton.setToolTipText(end);
					startButton.setEnabled(false);
				}
			}
		});

		
		// show content
		getContentPane().add(content);
		setSize(280, 330);
		setResizable(false);
		setLocationRelativeTo(null);
	}

	/**
	 * This is the main method of  the application.
	 * 
	 * @param args
	 * Unused.
	 * @return Nothing.
	 * @exception IOException
	 * On input error.
	 * @see IOException
	 */
	public static void main(String[] args) throws IOException {

		new TimerApp().setVisible(true);
	}

	/**
	 * Method to set all input GUI-fields to empty strings
	 */
	public void cleanInput() {
		jobTime.setText("");
		pauseTime.setText("");
		lunchTime.setText("");
		numberOfCycles.setText("");
	}

	/**
	 * Method to generate sounds signal
	 */
	public void beep2Sound() {
		Toolkit.getDefaultToolkit().beep();
		try {
			Thread.sleep(600); // Замораживает весь поток на 0.6 секунд
		} catch (Exception e) {
			System.out.println(e);
		}
		Toolkit.getDefaultToolkit().beep();
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}

	/**
	 * Method for validating user-entered values. Field values are valid only
	 * for numbers not equal to zero.
	 * 
	 * @return <code>true</code> if operation is successful, <code>false</code>
	 *         otherwise
	 */
	public boolean checkInput() {

		while ((jobTime.getText().isEmpty() || pauseTime.getText().isEmpty()
				|| lunchTime.getText().isEmpty() || numberOfCycles.getText()
				.isEmpty())
				||

				(Integer.valueOf(jobTime.getText()) == 0
						|| Integer.valueOf(pauseTime.getText()) == 0
						|| Integer.valueOf(numberOfCycles.getText()) == 0 || Integer
						.valueOf(lunchTime.getText()) == 0)) {

			messagesField.setText("Stellen Sie Zeit ein");
			JOptionPane.showMessageDialog(null,
					"Stellen Sie die Zeit ein und drücken Sie auf START!");
			System.out.println("Time is not on");
			cleanInput();

			return false;
		}
		return true;
	}

	/**
	 * This method is used to play ringtones.
	 * 
	 * @param musicName
	 * specifies the path to the ringtone file
	 */
	public void play(final String musicName) {

		musicFora();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			Player jlPlayer = null;

			@Override
			public void run() {
				FileInputStream inputStream = null;
				try {
					inputStream = new FileInputStream(musicName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				try {
					jlPlayer = new Player(inputStream);
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
				try {
					jlPlayer.play();
					jlPlayer.notify();
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);
		isPaused = false;
	}

	/**
	 * This method is used to format elapsed Time in type "00.00.00"
	 * 
	 * @param time
	 * specifies the start time
	 */
	public void timeSet(int time) // function to display the timer
	{
		long timeNow = 1000 * time;
		long second = (timeNow / 1000) % 60;
		long minute = (timeNow / (1000 * 60)) % 60;
		long hour = (timeNow / (1000 * 60 * 60)) % 24;
		timeNowLabel.setText(String.format("%02d:%02d:%02d", hour, minute,
				second));
	}

	/**
	 * This method gives the time delay for 2 seconds to play ringtone
	 */
	public void musicFora() {
		try {
			Thread.sleep(2 * 1000);
		} catch (final InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		}
	}

	/**
	* This method runs timertask and countS the jobcycle of  the application. TimerTask @see
	 * java.util.TimerTask implements Runnable interface so must override run
	 * method within mainTimer code block.
	 */
	public void mainTimer() // runs for timer
	{
		// read amount of cycles from GUI
		// constructor for Timer
		timer = new Timer();
		// set a new timertask duration @value counterPeriod
		timer.schedule(new TimerTask() {
			int counter = 0;
			int counterPeriod = Integer.valueOf(jobTime.getText());

			// start task if timer is not paused
			@Override
			public void run() {
				if (!isPaused) {
					messagesField.setText("Zeit zu arbeiten");
					++counter;
					timeSet(counter);
					if (counter == counterPeriod && сycle < сycleMax) // if less
																		// than
																		// x
																		// jobcycles
																		// are
																		// complete,
																		// take
																		// a
																		// coffeepause

					{
						// timertask is completed
						timer.cancel();
						// message to the user about the end of the job period
						System.out.println("Kaffeepause ist gestartet");
						messagesField.setText("Zeit sich zu erholen");
						play(pauseMp3);
						// Toolkit.getDefaultToolkit().beep(); oder ohne musik, nur beep
						pauseTimer();
						//  periodTimer(Integer.valueOf(pauseTime.getText()),
						// jobMp3, 0);
						сycle++;
					} else if (counter == counterPeriod && сycle == сycleMax) // if
																				// x
					// jobcycles
					// are
					// complete,
					// take
					// a lunch

					{ // timertask of full jobcycle is completed
						timer.cancel();
						// message to the user about the start of lunch
						// JOptionPane.showMessageDialog(null,"You have completed 4 pomodoros! Take a long break!");
						System.out.println("Mittagspause ist gestartet");
						messagesField.setText("Gehen wir mittagessen!");
						play(lunchMp3);
						// Toolkit.getDefaultToolkit().beep(); ohne musik
						// / periodTimer(Integer.valueOf(lunchTime.getText()),
						// jobMp3, 1);
						lunchTimer();
					}
				}
			}
		}, 0, 1000);
	}

	/**
	* Method runs timertask during the duration of the coffeepause of the
	 * application. TimerTask @see java.util.TimerTask implements Runnable
	 * interface so  IT must override run method within pauseTimer code block.
	 */
	public void pauseTimer() // runs for short-break time
	{
		timer = new Timer();
		timer.schedule(new TimerTask() {
			int counter = 0;

			@Override
			public void run() {
				int counterLimit = Integer.valueOf(pauseTime.getText());
				if (!isPaused) {
					++counter;
					timeSet(counter);
					if (counter == counterLimit) {
						timer.cancel();
						// Toolkit.getDefaultToolkit().beep();
						// JOptionPane.showMessageDialog(null,"Zeit zur Arbeit");
						System.out.println("Arbeitszeit");
						messagesField.setText("   Zeit zur Arbeit!");
						play(jobMp3);
						//musicFora();
						mainTimer();
					}
				}
			}
		}, 0, 1000);
	}

	/**
	 * Method runs timertask during the duration of the lunch. TimerTask @see
	 * java.util.TimerTask implements Runnable interface so must override run
	 * method within lunchTimer code block.
	 */
	public void lunchTimer() // runs for long break time
	{
		timer = new Timer();
		timer.schedule(new TimerTask() {
			int counter = 0;

			@Override
			public void run() {
				int counterLimit = Integer.valueOf(lunchTime.getText());
				if (!isPaused) {
					++counter;
					timeSet(counter);
					if (counter == counterLimit) {
						timer.cancel();
						// Toolkit.getDefaultToolkit().beep();
						// JOptionPane.showMessageDialog(null,"Zeit zur Arbeit!");
						System.out.println("Arbeitszeit");
						play(jobMp3);
						//musicFora();  
						сycle = 1;
						mainTimer();
					}
				}
			}
		}, 0, 1000);
	}

}
