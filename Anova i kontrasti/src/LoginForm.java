import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblPerformanseRaunarskihSistema;
	private JLabel lblTatjanaTomic;
	private JLabel lblNewLabel;
	private JButton btnNewButton;
	
	public LoginForm() 
	{
		initialize();
	}
	
	private void initialize()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 150, 550, 350);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(216, 191, 216));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblPerformanseRaunarskihSistema = new JLabel("Performanse racunarskih sistema");
		lblPerformanseRaunarskihSistema.setFont(new Font("Stencil", Font.PLAIN, 22));
		lblPerformanseRaunarskihSistema.setHorizontalAlignment(SwingConstants.CENTER);
		lblPerformanseRaunarskihSistema.setBounds(10, 94, 514, 58);
		contentPane.add(lblPerformanseRaunarskihSistema);

		lblTatjanaTomic = new JLabel("Tatjana Tomic 1182/16");
		lblTatjanaTomic.setHorizontalAlignment(SwingConstants.CENTER);
		lblTatjanaTomic.setFont(new Font("Stencil", Font.PLAIN, 20));
		lblTatjanaTomic.setBounds(10, 147, 514, 33);
		contentPane.add(lblTatjanaTomic);

		btnNewButton = new JButton("START");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mousePressedStart();
			}
		});
		btnNewButton.setForeground(new Color(139, 0, 139));
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 18));
		btnNewButton.setBounds(207, 218, 118, 44);
		contentPane.add(btnNewButton);
		
		lblNewLabel = new JLabel("ANOVA I KONTRASTI");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Stencil", Font.PLAIN, 42));
		lblNewLabel.setBounds(10, 39, 514, 44);
		contentPane.add(lblNewLabel);
	}
	
	private void mousePressedStart() {
		FirstForm mf = new FirstForm();
		mf.setVisible(true);
		this.dispose();
	}
}
