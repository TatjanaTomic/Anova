import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JTextField;

import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.JButton;
import java.awt.Choice;

public class MainForm extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTable table_1;
	private JButton btnIzracunaj;
	private JButton btnKontrasti;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JTextField txtUkupnaSrednjaVrijednost;
	private JTextField txtSSE;
	private JTextField txtSSA;
	private JTextField txtSST;
	private JTextField txtSSE_SST;
	private JTextField txtSSA_SST;
	private JTextField txtFizracunato;
	private JTextField txtFtabelarno;
	private JTextField txtZakljucak;
	private JLabel lblUnesiteVrijednostiMjerenja;
	private JLabel lblUpozorenje;
	private JLabel lblUkupnaSrednjaVrijednost;
	private JLabel lblSSE;
	private JLabel lblSSA;
	private JLabel lblSST;
	private JLabel lblSSE_SST;
	private JLabel lblSSA_SST;
	private JLabel lblFizracunato;
	private JLabel lblFtabelarno;
	private JLabel lblZakljucak;
	private JLabel lblIzaberiteProcenat;
	private Choice choiceProcenat;
	private ANOVA proracun;
	
	public MainForm(int brojMjerenja, int brojAlternativa) 
	{
		initialize(brojMjerenja, brojAlternativa);
	}
	
	private void initialize(int brojMjerenja, int brojAlternativa)
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(216, 191, 216));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 63, 974, 520);
		scrollPane.setBackground(new Color(216, 191, 216));
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setRowMargin(10);
		table.setRowHeight(26);
		table.setGridColor(SystemColor.scrollbar);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			objectMatrix(brojMjerenja, brojAlternativa),
			columnNames(brojAlternativa)
			) {
			private static final long serialVersionUID = 1L;
			
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = columnTypes(brojAlternativa);
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int columnIndex) 
			{
				return columnTypes[columnIndex];
			}
			
			boolean[] columnEditables = columnEditables(brojAlternativa);
			
			public boolean isCellEditable(int row, int column) 
			{
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.setBackground(SystemColor.menu);
		
		lblUnesiteVrijednostiMjerenja = new JLabel("Unesite vrijednosti mjerenja");
		lblUnesiteVrijednostiMjerenja.setBounds(20, 0, 279, 41);
		lblUnesiteVrijednostiMjerenja.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.add(lblUnesiteVrijednostiMjerenja);
		
		lblUpozorenje = new JLabel("Unesite vrijednosti svih mjerenja!");
		lblUpozorenje.setBounds(20, 38, 201, 17);
		lblUpozorenje.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUpozorenje.setForeground(Color.RED);
		lblUpozorenje.setVisible(false);
		contentPane.add(lblUpozorenje);
		
		btnKontrasti = new JButton("KONTRASTI");
		btnKontrasti.setBounds(1175, 634, 153, 41);
		btnKontrasti.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnKontrasti.addMouseListener(new MouseAdapter() 
		{	
			@Override
			public void mousePressed(MouseEvent e) 
			{
				if(btnKontrasti.isEnabled())
				{
					ContrastForm contrastForm = new ContrastForm(proracun);
					contrastForm.setVisible(true);
				}
			}
		});
		btnKontrasti.setForeground(new Color(139, 0, 139));
		btnKontrasti.setEnabled(false);
		btnKontrasti.setVisible(false);
		contentPane.add(btnKontrasti);
		
		lblIzaberiteProcenat = new JLabel("Izaberite procenat:");
		lblIzaberiteProcenat.setBounds(1150, 20, 110, 20);
		lblIzaberiteProcenat.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(lblIzaberiteProcenat);
		
		choiceProcenat = new Choice();
		choiceProcenat.setBounds(1268, 20, 60, 40);
		choiceProcenat.add("90%");
		choiceProcenat.add("95%");
		choiceProcenat.add("99%");
		contentPane.add(choiceProcenat);
		
		btnIzracunaj = new JButton("IZRACUNAJ");
		btnIzracunaj.setBounds(1175, 63, 153, 41);
		btnIzracunaj.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnIzracunaj.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mousePressed(MouseEvent e) 
			{
				lblUpozorenje.setVisible(false);
				
				boolean flag = HelpMethods.provjeriVrijednosti(table.getModel());
				if(flag)
				{
					proracun = new ANOVA(table.getModel(), procenat(choiceProcenat.getSelectedItem()));
					ispisiRezultat();
					if(proracun.fTest())
						btnKontrasti.setEnabled(true); //Ako se sistemi statisticki razlikuju, onda mozemo preci na kontraste
				}
				else
				{
					lblUpozorenje.setVisible(true);
				}
			}
		});
		btnIzracunaj.setForeground(new Color(139, 0, 139));
		contentPane.add(btnIzracunaj);
	}
	
	private void ispisiRezultat()
	{
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(20, 600, 974, 75);
		contentPane.add(scrollPane_1);
		
		table_1 = new JTable();
		scrollPane_1.setViewportView(table_1);
		table_1.setRowMargin(10);
		table_1.setRowHeight(26);
		table_1.setGridColor(SystemColor.scrollbar);
		table_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table_1.setBackground(SystemColor.menu);
		table_1.setModel(new DefaultTableModel(
				proracun.generisiObjekteTabele(),
				columnNames_1(proracun.getBrojAlternativa())
			) {
				private static final long serialVersionUID = 1L;
			
				@SuppressWarnings("rawtypes")
				Class[] columnTypes = columnTypes_1(proracun.getBrojAlternativa());
				
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public Class getColumnClass(int columnIndex) 
				{
					return columnTypes[columnIndex];
				}
				
				boolean[] columnEditables = columnEditables_1(proracun.getBrojAlternativa());
				
				public boolean isCellEditable(int row, int column) 
				{
					return columnEditables[column];
				}
			
		});
		table_1.getColumnModel().getColumn(0).setResizable(false);
		table_1.getColumnModel().getColumn(0).setPreferredWidth(40);
		
		btnKontrasti.setVisible(true);
		btnKontrasti.setEnabled(false);
		
		lblUkupnaSrednjaVrijednost = new JLabel("Ukupna srednja vrijednost: ");
		lblUkupnaSrednjaVrijednost.setBounds(1070, 130, 200, 23);
		lblUkupnaSrednjaVrijednost.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPane.add(lblUkupnaSrednjaVrijednost);
		
		lblSSE = new JLabel("SSE:");
		lblSSE.setBounds(1070, 160, 200, 23);
		lblSSE.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPane.add(lblSSE);
		
		lblSSA = new JLabel("SSA:");
		lblSSA.setBounds(1070, 190, 200, 23);
		lblSSA.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPane.add(lblSSA);
		
		lblSST = new JLabel("SST:");
		lblSST.setBounds(1070, 220, 200, 23);
		lblSST.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPane.add(lblSST);
	
		lblSSE_SST = new JLabel("SSE/SST:");
		lblSSE_SST.setBounds(1070, 250, 200, 23);
		lblSSE_SST.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPane.add(lblSSE_SST);
		
		lblSSA_SST = new JLabel("SSA/SST:");
		lblSSA_SST.setBounds(1070, 280, 200, 23);
		lblSSA_SST.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPane.add(lblSSA_SST);
		
		lblFizracunato = new JLabel("Fizracunatno:");
		lblFizracunato.setBounds(1070, 310, 200, 23);
		lblFizracunato.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPane.add(lblFizracunato);
		
		lblFtabelarno = new JLabel("Ftabelarno:");
		lblFtabelarno.setBounds(1070, 340, 200, 23);
		lblFtabelarno.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPane.add(lblFtabelarno);
		
		lblZakljucak = new JLabel("Zakljucak:");
		lblZakljucak.setBounds(1070, 370, 200, 23);
		lblZakljucak.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPane.add(lblZakljucak);
		
		DecimalFormat df = new DecimalFormat("#0.000");
		
		txtUkupnaSrednjaVrijednost = new JTextField(df.format(proracun.getUkupnaSrednjaVrijednost()));
		txtUkupnaSrednjaVrijednost.setEditable(false);
		txtUkupnaSrednjaVrijednost.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtUkupnaSrednjaVrijednost.setBounds(1240, 130, 88, 23);
		contentPane.add(txtUkupnaSrednjaVrijednost);
		txtUkupnaSrednjaVrijednost.setBackground(SystemColor.menu);
		
		txtSSE = new JTextField(df.format(proracun.getSSE()));
		txtSSE.setEditable(false);
		txtSSE.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtSSE.setBounds(1240, 160, 88, 23);
		contentPane.add(txtSSE);
		txtSSE.setBackground(SystemColor.menu);
		
		txtSSA = new JTextField(df.format(proracun.getSSA()));
		txtSSA.setEditable(false);
		txtSSA.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtSSA.setBounds(1240, 190, 88, 23);
		contentPane.add(txtSSA);
		txtSSA.setBackground(SystemColor.menu);
	
		txtSST = new JTextField(df.format(proracun.getSST()));
		txtSST.setEditable(false);
		txtSST.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtSST.setBounds(1240, 220, 88, 23);
		contentPane.add(txtSST);
		txtSST.setBackground(SystemColor.menu);
	
		txtSSE_SST = new JTextField(proracun.varijacijeZbogGresaka());
		txtSSE_SST.setEditable(false);
		txtSSE_SST.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtSSE_SST.setBounds(1240, 250, 88, 23);
		contentPane.add(txtSSE_SST);
		txtSSE_SST.setBackground(SystemColor.menu);
	
		txtSSA_SST = new JTextField(proracun.varijacijeZbogRazlika());
		txtSSA_SST.setEditable(false);
		txtSSA_SST.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtSSA_SST.setBounds(1240, 280, 88, 23);
		contentPane.add(txtSSA_SST);
		txtSSA_SST.setBackground(SystemColor.menu);
		
		txtFizracunato = new JTextField(df.format(proracun.getFizracunato()));
		txtFizracunato.setEditable(false);
		txtFizracunato.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtFizracunato.setBounds(1240, 310, 88, 23);
		contentPane.add(txtFizracunato);
		txtFizracunato.setBackground(SystemColor.menu);
	
		txtFtabelarno = new JTextField(df.format(proracun.getFtabelarno()));
		txtFtabelarno.setEditable(false);
		txtFtabelarno.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtFtabelarno.setBounds(1240, 340, 88, 23);
		contentPane.add(txtFtabelarno);
		txtFtabelarno.setBackground(SystemColor.menu);
	
		txtZakljucak = new JTextField(proracun.zakljucak());
		txtZakljucak.setEditable(false);
		txtZakljucak.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtZakljucak.setBounds(1070, 395, 258, 33);
		contentPane.add(txtZakljucak);
		txtZakljucak.setBackground(SystemColor.menu);		
	}

	private Object[][] objectMatrix(int brojMjerenja, int brojAlternativa) 
	{	
		Object[][] objects = new Object[brojMjerenja][brojAlternativa+1];
		
		for(int i = 0; i < brojMjerenja; i++)
		{
			for(int j = 0; j < brojAlternativa+1; j++)
			{
				if(j==0)
					objects[i][j] = new Integer(i+1);
				else
					objects[i][j] = null;
			}
		}
		return objects;
	}
	
	private String[] columnNames(int brojAlternativa) 
	{
		String[] names = new String[brojAlternativa+1];
		names[0] = "Mjerenje";
		for(int i = 1; i <= brojAlternativa ; i++)
		{
			names[i] = "Alternativa" + Integer.toString(i);
		}
		return names;
	}
	
	@SuppressWarnings("rawtypes")
	private Class[] columnTypes(int brojAlternativa) 
	{	
		Class[] types = new Class[brojAlternativa+1];
		types[0] = Integer.class;
		for(int i=1; i<=brojAlternativa; i++)
		{
			types[i] = Double.class;
		}
		return types;
	}
	
	private boolean[] columnEditables(int brojAlternativa)
	{
		boolean[] values = new boolean[brojAlternativa+1];
		values[0] = false;
		for(int i=1; i<=brojAlternativa; i++)
		{
			values[i] = true;
		}
		return values;
	}
	
	private String[] columnNames_1(int brojAlternativa)
	{
		String[] nazivi = new String[brojAlternativa+1];
		nazivi[0] = "";
		for(int i = 1; i <= brojAlternativa; i++)
			nazivi[i] = "Alternativa" + i;
		return nazivi;
	}
	
	@SuppressWarnings("rawtypes")
	private Class[] columnTypes_1(int brojAlternativa) 
	{	
		Class[] types = new Class[brojAlternativa+1];
		types[0] = String.class;
		for(int i=1; i<=brojAlternativa; i++)
		{
			types[i] = Double.class;
		}
		return types;
	}

	private boolean[] columnEditables_1(int brojAlternativa)
	{
		boolean[] values = new boolean[brojAlternativa+1];
		for(int i=0; i<=brojAlternativa; i++)
			values[i] = false;
		return values;
	}
	
	private double procenat(String procenat)
	{
		if(procenat.contentEquals("90%"))
		{
			System.out.println("OK");
			return 0.90;
		}
		else if(procenat.contentEquals("95%"))
			return 0.95;
		else if(procenat.contentEquals("99%"))
			return 0.99;
		return 0;
	}
}