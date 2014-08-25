
package IntelligentWebSearch;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ProgressWindow extends JFrame
{
	private JPanel jPanelSpace = new JPanel();
	private JPanel jPanelContent = new JPanel();
		private JLabel jLabel = new JLabel("Searching Google",SwingConstants.CENTER);
		private JProgressBar jProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);

	public ProgressWindow()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setSize(new Dimension(400,300));
		this.setLocation((screenSize.width - this.getWidth()) / 2,(screenSize.height - this.getHeight()) / 2);
		this.setResizable(false);
		this.setTitle("IntelligentWebSearch Progress Window");

			this.jPanelSpace.setPreferredSize(new Dimension(350,90));
		this.getContentPane().add(this.jPanelSpace,BorderLayout.NORTH);
			this.jPanelContent.setPreferredSize(new Dimension(350,100));
				this.jLabel.setPreferredSize(new Dimension(300,20));
				this.jLabel.setOpaque(true);
				this.jLabel.setBackground(Color.blue);
				this.jLabel.setForeground(Color.white);
			this.jPanelContent.add(this.jLabel,BorderLayout.CENTER);
				this.jProgressBar.setPreferredSize(new Dimension(300,20));
				this.jProgressBar.setIndeterminate(false);
			this.jPanelContent.add(this.jProgressBar,BorderLayout.CENTER);
		this.getContentPane().add(this.jPanelContent,BorderLayout.CENTER);		

		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent windowEvent)
			{
				hide();
			}
		});
	}

	public void changeStatus(String titleLabel, String label, boolean start)
	{
		this.jLabel.setText(titleLabel);

		this.jProgressBar.setString(label);
		this.jProgressBar.setStringPainted(true);
		this.jProgressBar.setIndeterminate(start);
	}
}
