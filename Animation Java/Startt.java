import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Startt extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JButton slower, faster;
	private JLabel l1,l2;
	private JPanel mp;
	private JPanel p;
	private Timer tm = new Timer(100, this);
	private int speed = 1, frame = 0;
	private static int numberOfBalls, numberOfParts;
	private static JFileChooser fc = new JFileChooser();
	private static List<Double> l = new ArrayList<>();
	private static Map<Integer, List<Double>> positions = new HashMap<>();

	private JPanel panel() {
		mp = new JPanel();
		mp.setLayout(new BorderLayout());
		mp.setBackground(Color.LIGHT_GRAY);
		p = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;
				int j = 0;
				for (int i = 0; i < numberOfBalls; i++) {
					if(positions.get(frame).size() > (numberOfBalls/2)) {
						double x = (positions.get(frame).get(j))/(l.get(frame)) * (this.getWidth()-11);
						double y = (this.getHeight()-11) - (positions.get(frame).get(j + 1))/(l.get(frame)) * (this.getHeight()-11);				
						j = j + 2;
						Ellipse2D circle = new Ellipse2D.Double(x, y, 10, 10);
						g2.draw(circle);
					}
					l2.setText("Frame: "+frame+"   | skipped frame: "+(speed-1)+"    ");
				}
				tm.start();
			};
		};
		p.setBackground(Color.WHITE);
		l1 = new JLabel();
		l1.setText("SpeedUp: " + speed);
		l1.setHorizontalAlignment(SwingConstants.LEFT);
		l2 = new JLabel();
		l2.setText("Frame: "+frame+"   | skipped frame: "+(speed-1)+"    ");
		l2.setHorizontalAlignment(SwingConstants.RIGHT);
		slower = new JButton("<< Slower");
		slower.addActionListener(a -> {
			if (speed > 0) {
				speed--;
				l1.setText("SpeedUp: " + speed);
			}
		});
		faster = new JButton("Faster >>");
		faster.addActionListener(a -> {
			if (speed == 0) {
				speed++;
				l1.setText("SpeedUp: " + speed);
				tm.start();
				p.revalidate();
				p.repaint();
			} else if (speed < numberOfParts) {
				speed++;
				l1.setText("SpeedUp: " + speed);
			}
		});
		mp.add(l1, BorderLayout.PAGE_START);
		mp.add(l2, BorderLayout.PAGE_END);
		mp.add(slower, BorderLayout.LINE_START);
		mp.add(p, BorderLayout.CENTER);
		mp.add(faster, BorderLayout.LINE_END);
		return mp;
	}

	public Startt() {
		super();
		this.getContentPane().add(this.panel());
	}

	public static void okno() {
		Startt st = new Startt();
		st.setVisible(true);
		st.setDefaultCloseOperation(EXIT_ON_CLOSE);
		st.pack();
		st.setLocation(0, 0);
		st.setTitle("Aniamcja | Mateusz Sienkiewicz");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		st.setSize((int) width / 2, (int) height / 2);
		st.setResizable(true);
		st.setVisible(true);
	}

	public static void oknoStart() {
		JFrame jf = new JFrame();
		jf.setVisible(true);
		jf.setLocation(0, 0);
		jf.setTitle("Aniamcja | Mateusz Sienkiewicz");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		jf.setSize((int) width / 2, (int) height / 2);
		jf.setResizable(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Pliki .gz", "gz");
		fc.setFileFilter(filter);
		fc.setVisible(true);
		jf.add(fc);
		if (fc.showOpenDialog(fc.getParent()) == JFileChooser.APPROVE_OPTION) {
			try {
				InputStreamReader in = new InputStreamReader(
						new GZIPInputStream(new FileInputStream(fc.getSelectedFile())));
				StringWriter sw = new StringWriter();
				char[] chars = new char[1024];
				for (int length; (length = in.read(chars)) > 0;) {
					sw.write(chars, 0, length);
				}
				Scanner s = new Scanner(sw.toString());
				s.useLocale(new Locale("us"));
				numberOfParts = 0;
				numberOfBalls = s.nextInt();
				int k = numberOfBalls * 2;
				s.nextInt();
				int j = 0;
				while (s.hasNextLine()) {
					numberOfParts++;
					s.nextLine();
					if (!s.hasNextInt())
						break;
					s.nextInt();
					positions.put(j, new ArrayList<>());
					for (int i = 0; i < k; i++) {
						positions.get(j).add(s.nextDouble());
					}
					j++;
					System.out.println("Wczytano: "+j+" klatek animacji.");
				}
				s.close();
				in.close();
				for(int i = 0; i< positions.size(); i++) {
					double max = Collections.max(positions.get(i));
					l.add(i, max);
					}
			} catch (Exception e) {
				e.getMessage();
			}
			jf.addWindowListener(new WindowAdapter() {
				public void windowActivated(WindowEvent e) {
					okno();
					jf.setVisible(false);
				}
			});
		} else {
			jf.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				oknoStart();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((frame + speed) < numberOfParts) {
			frame++;
			frame += speed - 1;
			if (speed > 0 && tm.isRunning()) {
				p.repaint();
			} else if (speed == 0 && !tm.isRunning()) {
				tm.start();
				p.revalidate();
				p.repaint();
			} else
				tm.stop();
		} else {
			frame = 0;
			speed = 1;
			l1.setText("SpeedUp: " + speed);
			p.revalidate();
			p.repaint();
		}
	}
}
