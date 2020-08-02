import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FirstForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_brojAlternativa;
	private JTextField textField_brojMjerenja;
	private JLabel lblAnovaIKontrasti;
	private JLabel lblBrojAlternativa;
	private JLabel lblBrojMjerenja;
	private JLabel lblUnesiteBrojAlternativa;
	private JLabel lblUnesiteBrojMjerenja;
	private JLabel lblUpozorenje;
	private JButton btnDalje;
	
	public FirstForm() 
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
		
		lblAnovaIKontrasti = new JLabel("ANOVA I KONTRASTI");
		lblAnovaIKontrasti.setVerticalAlignment(SwingConstants.TOP);
		lblAnovaIKontrasti.setFont(new Font("Stencil", Font.PLAIN, 28));
		lblAnovaIKontrasti.setHorizontalAlignment(SwingConstants.LEFT);
		lblAnovaIKontrasti.setBounds(10, 11, 331, 45);
		contentPane.add(lblAnovaIKontrasti);
		
		lblBrojAlternativa = new JLabel("Broj alternativa:");
		lblBrojAlternativa.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblBrojAlternativa.setBounds(10, 67, 142, 31);
		contentPane.add(lblBrojAlternativa);
		
		lblBrojMjerenja = new JLabel("Broj mjerenja:");
		lblBrojMjerenja.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblBrojMjerenja.setBounds(10, 122, 142, 31);
		contentPane.add(lblBrojMjerenja);
		
		textField_brojAlternativa = new JTextField();
		lblBrojAlternativa.setLabelFor(textField_brojAlternativa);
		textField_brojAlternativa.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_brojAlternativa.setBounds(162, 67, 96, 31);
		contentPane.add(textField_brojAlternativa);
		textField_brojAlternativa.setColumns(10);
		
		textField_brojMjerenja = new JTextField();
		lblBrojMjerenja.setLabelFor(textField_brojMjerenja);
		textField_brojMjerenja.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_brojMjerenja.setBounds(162, 122, 96, 31);
		contentPane.add(textField_brojMjerenja);
		textField_brojMjerenja.setColumns(10);
		
		lblUnesiteBrojAlternativa = new JLabel("* Obavezno polje");
		lblUnesiteBrojAlternativa.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblUnesiteBrojAlternativa.setVisible(false);
		lblUnesiteBrojAlternativa.setForeground(Color.RED);
		lblUnesiteBrojAlternativa.setBounds(260, 65, 215, 31);
		contentPane.add(lblUnesiteBrojAlternativa);
		
		lblUnesiteBrojMjerenja = new JLabel("* Obavezno polje");
		lblUnesiteBrojMjerenja.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblUnesiteBrojMjerenja.setVisible(false);
		lblUnesiteBrojMjerenja.setForeground(Color.RED);
		lblUnesiteBrojMjerenja.setBounds(260, 120, 215, 31);
		contentPane.add(lblUnesiteBrojMjerenja);
		
		lblUpozorenje = new JLabel("Broj alternativa i broj mjerenja moraju biti pozitivni cijeli brojevi veci od 1!");
		lblUpozorenje.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblUpozorenje.setVisible(false);
		lblUpozorenje.setForeground(Color.RED);
		lblUpozorenje.setBounds(10, 164, 550, 27);
		contentPane.add(lblUpozorenje);
		
		btnDalje = new JButton("DALJE");
		btnDalje.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				lblUnesiteBrojMjerenja.setVisible(false);
				lblUnesiteBrojAlternativa.setVisible(false);
				lblUpozorenje.setVisible(false);
				
				String brojMjerenja = textField_brojMjerenja.getText();
				String brojAlternativa = textField_brojAlternativa.getText();
				
				if(brojMjerenja.contentEquals("")) {
					lblUnesiteBrojMjerenja.setVisible(true);
				}
				
				if(brojAlternativa.contentEquals("")) {
					lblUnesiteBrojAlternativa.setVisible(true);
				}
				
				if( !HelpMethods.checkIsNumber(brojMjerenja) || !HelpMethods.checkIsNumber(brojAlternativa) ) {
					lblUpozorenje.setVisible(true);
				}
				else
					createMatrix(brojMjerenja, brojAlternativa);
					
			}
		});
		btnDalje.setForeground(new Color(139, 0, 139));
		btnDalje.setFont(new Font("Dialog", Font.BOLD, 18));
		btnDalje.setBounds(207, 218, 118, 44);
		contentPane.add(btnDalje);
	}
	
	private void createMatrix(String brojMjerenja, String brojAlternativa) {
		MainForm mf = new MainForm(Integer.parseInt(brojMjerenja), Integer.parseInt(brojAlternativa));
		mf.setVisible(true);
		this.dispose();
	}
}
