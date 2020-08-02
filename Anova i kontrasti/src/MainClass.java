import java.awt.EventQueue;

public class MainClass {	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm frame = new LoginForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print(e.getMessage());
				}
			}
		});
	}
	
}
