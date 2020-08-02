import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Choice;
import java.awt.SystemColor;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.JTextField;

public class ContrastForm extends JFrame 
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Choice choicePrvaAlternativa;
	private Choice choiceDrugaAlternativa;
	private JButton btnIzracunaj;
	private JLabel lblKontrasti;
	private JLabel lblPrvaAlternativa;
	private JLabel lblDrugaAlternativa;
	private JLabel lblUpozorenje;
	private JLabel lblKontrast;
	private JLabel lblIntervalPovjerenja;
	private JLabel lblZakljucak;
	private JTextField textFieldKontrast;
	private JTextField textFieldIntervalPovjerenja;
	private JTextField textFieldZakljucak;
	private ANOVA proracun;
	
	public ContrastForm(ANOVA proracun)
	{
		this.proracun = proracun;
		initialize();
	}
	
	private void initialize()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 150, 550, 350);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(216, 191, 216));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		lblKontrasti = new JLabel("KONTRASTI");
		lblKontrasti.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblKontrasti.setBounds(10, 11, 71, 22);
		contentPane.add(lblKontrasti);
		
		lblPrvaAlternativa = new JLabel("Prva alternativa:");
		lblPrvaAlternativa.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPrvaAlternativa.setBounds(10, 38, 94, 16);
		contentPane.add(lblPrvaAlternativa);
		
		lblDrugaAlternativa = new JLabel("Druga alternativa:");
		lblDrugaAlternativa.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDrugaAlternativa.setBounds(10, 71, 103, 16);
		contentPane.add(lblDrugaAlternativa);
		
		choicePrvaAlternativa = new Choice();
		choicePrvaAlternativa.setBackground(SystemColor.menu);
		choicePrvaAlternativa.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPrvaAlternativa.setLabelFor(choicePrvaAlternativa);
		choicePrvaAlternativa.setBounds(131, 32, 119, 20);
		contentPane.add(choicePrvaAlternativa);
		
		choiceDrugaAlternativa = new Choice();
		choiceDrugaAlternativa.setBackground(SystemColor.menu);
		choiceDrugaAlternativa.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDrugaAlternativa.setLabelFor(choiceDrugaAlternativa);
		choiceDrugaAlternativa.setBounds(131, 65, 119, 20);
		contentPane.add(choiceDrugaAlternativa);
		
		addAlternatives(proracun.getBrojAlternativa());
		
		lblUpozorenje = new JLabel("* Izaberite dvije razlicite alternative!");
		lblUpozorenje.setForeground(Color.RED);
		lblUpozorenje.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUpozorenje.setBounds(260, 102, 195, 15);
		lblUpozorenje.setVisible(false);
		contentPane.add(lblUpozorenje);
		
		btnIzracunaj = new JButton("IZRACUNAJ");
		btnIzracunaj.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				lblUpozorenje.setVisible(false);
				
				String first = choicePrvaAlternativa.getSelectedItem();
				String second = choiceDrugaAlternativa.getSelectedItem();
				
				if(!first.contentEquals(second))
				{
					prikaziRezultat(first, second);
				}
				else
					lblUpozorenje.setVisible(true);
			}
		});
		btnIzracunaj.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnIzracunaj.setBounds(141, 102, 109, 32);
		btnIzracunaj.setForeground(new Color(139, 0, 139));
		contentPane.add(btnIzracunaj);
		
		lblKontrast = new JLabel("Kontrast:");
		lblKontrast.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblKontrast.setBounds(10, 180, 130, 23);
		lblKontrast.setVisible(false);
		contentPane.add(lblKontrast);
		
		lblIntervalPovjerenja = new JLabel("Interval povjerenja:");
		lblIntervalPovjerenja.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblIntervalPovjerenja.setBounds(10, 220, 130, 23);
		lblIntervalPovjerenja.setVisible(false);
		contentPane.add(lblIntervalPovjerenja);
		
		lblZakljucak = new JLabel("Zakljucak:");
		lblZakljucak.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblZakljucak.setBounds(10, 260, 130, 23);
		lblZakljucak.setVisible(false);
		contentPane.add(lblZakljucak);
	}
	
	private void prikaziRezultat(String prvaAlternativa, String drugaAlternativa)
	{
		
		ANOVA.IntervalPovjerenja ip = proracun.kontrast(getAlternativeId(prvaAlternativa), getAlternativeId(drugaAlternativa));
		DecimalFormat df = new DecimalFormat("#0.000");
		
		lblKontrast.setVisible(true);
		lblIntervalPovjerenja.setVisible(true);
		lblZakljucak.setVisible(true);
		
		textFieldKontrast = new JTextField(df.format(ip.getKontrast()));
		textFieldKontrast.setEditable(false);
		textFieldKontrast.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textFieldKontrast.setBackground(SystemColor.menu);
		textFieldKontrast.setBounds(145, 179, 383, 22);
		contentPane.add(textFieldKontrast);
		textFieldKontrast.setColumns(10);
		
		textFieldIntervalPovjerenja = new JTextField("(c1, c2) = (" + df.format(ip.getC1()) + ", " + df.format(ip.getC2()) + ")");
		textFieldIntervalPovjerenja.setEditable(false);
		textFieldIntervalPovjerenja.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textFieldIntervalPovjerenja.setBackground(SystemColor.menu);
		textFieldIntervalPovjerenja.setBounds(145, 219, 383, 22);
		contentPane.add(textFieldIntervalPovjerenja);
		textFieldIntervalPovjerenja.setColumns(10);
		
		textFieldZakljucak = new JTextField(ip.zakljucak());
		textFieldZakljucak.setEditable(false);
		textFieldZakljucak.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textFieldZakljucak.setBackground(SystemColor.menu);
		textFieldZakljucak.setBounds(145, 259, 383, 22);
		contentPane.add(textFieldZakljucak);
		textFieldZakljucak.setColumns(10);
	}
	
	private void addAlternatives(int brojAlternativa)
	{
		for(int i = 1; i <= brojAlternativa; i++)
		{
			choicePrvaAlternativa.add("Alternativa" + i);
			choiceDrugaAlternativa.add("Alternativa" + i);
		}
	}
	
	private int getAlternativeId(String alternativa)
	{
		String broj = alternativa.substring(alternativa.length() - 1);
		return Integer.parseInt(broj);
	}
}