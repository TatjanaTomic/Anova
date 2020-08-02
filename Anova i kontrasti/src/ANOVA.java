import java.text.DecimalFormat;

import javax.swing.table.TableModel;
import jdistlib.F;
import jdistlib.T;

public class ANOVA {
	
	public class IntervalPovjerenja
	{
		private double kontrast;
		private double c1;
		private double c2;
		
		IntervalPovjerenja(double c)
		{
			kontrast = c;
			double sC = sumarnaDevijacija();
			
			double koeficijent = T.quantile(alfa/2, dfSSE , false, false);
			
			c1 = kontrast - koeficijent * sC;
			c2 = kontrast + koeficijent * sC;
		}
		
		private double sumarnaDevijacija()
		{
			return Math.sqrt( varijansaSSE * 2 / (brojAlternativa * brojMjerenja));
		}
		
		private boolean sadrziNulu()
		{
			if(c1 < 0 && c2 > 0)
				return true;
			return false;
		}
		
		public String zakljucak()
		{
			boolean flag = sadrziNulu();
			
			return "Interval povjerenja " + (flag ? "" : "ne ") + "obuhvata nulu, sistemi se " + (flag ? "ne " : "") + "razlikuju.";
		}
		
		public double getKontrast()
		{
			return kontrast;
		}
		
		public double getC1()
		{
			return c1;
		}
		
		public double getC2()
		{
			return c2;
		}
	}
	
	private int brojAlternativa;
	private int brojMjerenja;
	private double procenat;
	
	private Double[][] vrijednosti;
	
	private static double[] srednjeVrijednostiAlternativa;
	private double ukupnaSrednjaVrijednost;
	private static double[] efektiAlternativa; //udaljenost alternative od ukupne srednje vrijednosti
	
	private double SSE = 0; //sum of squares errors - suma kvadrata svih gresaka pojedinacnih mjerenja
	private double SSA = 0; //sum of squares alternatives
	private double SST = 0; //sum of squares total
	private double dfSSA; //df = degrees of freedom
	private double dfSSE;
	private double varijansaSSA;
	private double varijansaSSE;
	
	private double alfa;
	private double fTabelarno;
	private double fIzracunato;
	
	ANOVA(TableModel model, double p)
	{
		brojMjerenja=model.getRowCount();
		brojAlternativa=model.getColumnCount()-1;
		vrijednosti = new Double[brojMjerenja][brojAlternativa];
		
		for (int i = 0; i < brojMjerenja; i++)
		{
			for (int j = 1; j <= brojAlternativa; j++)
			{
				Object tmp = model.getValueAt(i, j);
				vrijednosti[i][j-1] = (Double) tmp;
			}
		}
		
		procenat = p;
		analizaPodataka();
	}
	
	private void analizaPodataka()
	{
		double sume[] = new double[brojAlternativa];
		double ukupnaSuma = 0;
		
		for (int i = 0; i < brojAlternativa; i++)
			sume[i] = 0;
	
		for (int i = 0; i < brojMjerenja; i++)
		{
			for (int j = 0; j < brojAlternativa; j++)
			{
				sume[j] += vrijednosti[i][j];
				ukupnaSuma += vrijednosti[i][j];
			}
		}
		
		srednjeVrijednostiAlternativa = new double[brojAlternativa];
		for (int i = 0; i < brojAlternativa; i++)
			srednjeVrijednostiAlternativa[i] = sume[i] / brojMjerenja;
	
		ukupnaSrednjaVrijednost = ukupnaSuma / (brojMjerenja * brojAlternativa);
	
		efektiAlternativa = new double[brojAlternativa];
		for(int i = 0; i < brojAlternativa; i++)
			efektiAlternativa[i] = srednjeVrijednostiAlternativa[i] - ukupnaSrednjaVrijednost;
		
		for(int i = 0; i < brojMjerenja; i++)
		{
			for(int j = 0; j < brojAlternativa; j++)
			{
				SSE += (vrijednosti[i][j] - srednjeVrijednostiAlternativa[j]) * (vrijednosti[i][j] - srednjeVrijednostiAlternativa[j]);
			}
		}
		for(int i = 0; i < brojAlternativa; i++)
			SSA += efektiAlternativa[i] * efektiAlternativa[i];
		SSA *= brojMjerenja;
		SST = SSA + SSE;
		
		dfSSE = brojAlternativa * (brojMjerenja - 1);
		dfSSA = brojAlternativa - 1;
		varijansaSSA = SSA / dfSSA;
		varijansaSSE = SSE / dfSSE;
		
		fIzracunato = varijansaSSA / varijansaSSE;
		
		alfa = 1 - procenat;
		fTabelarno = F.quantile(alfa, dfSSA, dfSSE, false, false);
	}
	
	public Object[][] generisiObjekteTabele()
	{
		Object[][] obj = new Object[2][brojAlternativa+1];
		obj[0][0] = "Srednje vrijednosti kolona";
		obj[1][0] = "Efekti";
		for(int i = 1; i <= brojAlternativa; i++)
		{
			obj[0][i] = srednjeVrijednostiAlternativa[i-1];
			obj[1][i] = efektiAlternativa[i-1];
		} 
		return obj;
	}
	
	public String varijacijeZbogGresaka()
	{
		double procenat = SSE/SST * 100;
		DecimalFormat df = new DecimalFormat("#0.000");
		return df.format(procenat) + "%";
	}
	
	public String varijacijeZbogRazlika()
	{
		double procenat = SSA/SST * 100;
		DecimalFormat df = new DecimalFormat("#0.000");
		return df.format(procenat) + "%";
	}
	
	public boolean fTest()
	{
		return fIzracunato > fTabelarno;
	}
	
	public String zakljucak()
	{
		return "Sistemi se statisticki znacajno " + (fTest() ? "" : "ne ") + "razlikuju.";
	}
	
	public IntervalPovjerenja kontrast(int alternativa1, int alternativa2)
	{
		double c = efektiAlternativa[alternativa1-1] - efektiAlternativa[alternativa2-1];
		
		return new IntervalPovjerenja(c);
	}
	
	public int getBrojAlternativa()
	{
		return brojAlternativa;
	}
	
	public int getBrojMjerenja()
	{
		return brojMjerenja;
	}

	public double getUkupnaSrednjaVrijednost() 
	{
		return ukupnaSrednjaVrijednost;
	}
	
	public double getSSE()
	{
		return SSE;
	}
	
	public double getSSA()
	{
		return SSA;
	}
	
	public double getSST()
	{
		return SST;
	}

	public double getFizracunato()
	{
		return fIzracunato;
	}
	
	public double getFtabelarno()
	{
		return fTabelarno;
	}
}
