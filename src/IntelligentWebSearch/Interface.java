
/**
 * Interface.java
 *
 * Criada em 03-12-2004
 */

package IntelligentWebSearch;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.ListIterator;
import java.net.*;
import java.io.*;

/**
 * Classe que representa a interface gr�fica da aplica��o
 * @author Andr� Barbosa e Filipe Montenegro
 */
public class Interface extends JFrame
{
	private final int PROCESS_END_CHAR = 65535;
	
	private String classPath = this.getClass().getResource("").getPath();
	private String path = classPath.substring(0, classPath.indexOf("bin/IntelligentWebSearch/"));
	private ProgressWindow progressWindow = null;

	private String searchText = null;
	private int initialGoogleResults = 0;
	private int depthLevel = 0;
	private int algorithm = 0;
	private int wordsOrientation = 1;
	private int words = 0;
	private double delta = 0.0;
	private boolean portugal = false;
	private boolean googleSearched = false;

	private Timer timer = null;
	private double agentsTime = 0.0;
	private double wordsTime = 0.0;
	private double algorithmTime = 0.0;

	private String newSearchText = null;
	private boolean newSearch = false;
	private boolean wordsAvailable = false;
	private LinkedList wordsClusters = null;

	private String initialResults[] = {"1","2","3","4","5","6","7","8","9","10"};
	private String depthLevels[] = {"0","1","2","3","4","5"};
	private String wordsOrientations[] = {"Full Page","Sentences"};
	private String algorithms[] = {"1","2"};
	private String wordsCombo[] = {"10","20","50","100","200","500"};
	private String deltasCombo[] = {"0.01","0.03","0.05","0.075","0.1","0.3","0.5","0.75"};

	private boolean fileNew = false;
	private boolean fileOpened = false;
	private boolean fileSaved = false;
	private boolean fileSavedBefore = false;
	private boolean fileChanged = false;

	private String filePath = null;
	private javax.swing.filechooser.FileFilter fileFilter = new javax.swing.filechooser.FileFilter()
	{
		public boolean accept(File file)
		{
			if(file.isDirectory())
				return true;
			else
			{
				String extension = null;
				StringTokenizer strTok = new StringTokenizer(file.toString(),".");
				
				int numTokens = strTok.countTokens();
				for(int i = 0; i < numTokens; i++)
					extension = strTok.nextToken();

				if(extension.equalsIgnoreCase("iws"))
					return true;
				else
					return false;
			}
		}

		public String getDescription()
		{
			return "IntelligentWebSearch Files (*.iws)";
		}
	};

	private JPanel jPanelTitle = new JPanel();
		private JLabel jLabelTitle = new JLabel("IntelligentWebSearch",SwingConstants.CENTER);
	private JPanel jPanelMenu = new JPanel();
		private JPanel jPanelMenuSpace1 = new JPanel();
		private JLabel jLabelFileOptions = new JLabel("FILE OPTIONS",SwingConstants.CENTER);
		private JButton jButtonNew = new JButton("New");
		private JButton jButtonOpen = new JButton("Open");
		private JButton jButtonSave = new JButton("Save");
		private JButton jButtonSaveAs = new JButton("Save As");
		private JPanel jPanelMenuSpace2 = new JPanel();
		private JLabel jLabelInitialResultsOptions = new JLabel("INITIAL RESULTS OPTIONS",SwingConstants.CENTER);
		private JButton jButtonSearchText = new JButton("Search Text");
		private JButton jButtonInitialResultsTable = new JButton("Initial Results Table");
		private JButton jButtonAgentsTable = new JButton("Agents Table");
		private JButton jButtonWordsTable = new JButton("Words Table");
		private JButton jButtonWordsTree = new JButton("Words Tree");
		private JButton jButtonWordsClusterSelection = new JButton("Words Cluster Selection");
		private JPanel jPanelMenuSpace3 = new JPanel();
		private JLabel jLabelFinalResultsOptions = new JLabel("FINAL RESULTS OPTIONS",SwingConstants.CENTER);
		private JButton jButtonFinalResults = new JButton("Final Results");
	private JPanel jPanelContent = new JPanel();
		private JPanel jPanelContentSpace = new JPanel();
		private JTabbedPane jTabbedPaneContent = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
			private JPanel jPanelSearchText = new JPanel();
				private JPanel jPanelSearchTextSpace = new JPanel();
				private JLabel jLabelSearchTextTitle = new JLabel("SEARCH TEXT",SwingConstants.CENTER);
				private JPanel jPanelForm = new JPanel();
					private JPanel jPanelFormSpace = new JPanel();
					private JPanel jPanelFormSearchText = new JPanel();
						private JLabel jLabelSearchText = new JLabel("Search Text:",SwingConstants.CENTER);
						private JTextField jTextFieldSearchText = new JTextField();
					private JPanel jPanelFormInitialResultsDepthLevel = new JPanel();
						private JLabel jLabelInitialResults = new JLabel("Initial Results:",SwingConstants.CENTER);
						private JComboBox jComboBoxInitialResults = new JComboBox(this.initialResults);
						private JPanel jPanelInitialResultsDepthLevelSpace = new JPanel();
						private JLabel jLabelDepthLevel = new JLabel("Depth Level:",SwingConstants.CENTER);
						private JComboBox jComboBoxDepthLevel = new JComboBox(this.depthLevels);
					private JPanel jPanelFormAlgorithms = new JPanel();
						private JLabel jLabelWordsOrientation = new JLabel("Words Orientation",SwingConstants.CENTER);
						private JComboBox jComboBoxWordsOrientation = new JComboBox(this.wordsOrientations);
						private JPanel jPanelAlgorithmsSpace = new JPanel();
						private JLabel jLabelAlgorithm = new JLabel("Algorithm",SwingConstants.CENTER);
						private JComboBox jComboBoxAlgorithm = new JComboBox(this.algorithms);
					private JPanel jPanelWordsDelta = new JPanel();
						private JLabel jLabelWords = new JLabel("Words:",SwingConstants.CENTER);
						private JComboBox jComboBoxWords = new JComboBox(this.wordsCombo);
						private JPanel jPanelWordsDeltaSpace = new JPanel();
						private JLabel jLabelDelta = new JLabel("Delta:",SwingConstants.CENTER);
						private JComboBox jComboBoxDelta = new JComboBox(this.deltasCombo);
					private JPanel jPanelFormSearchDomain = new JPanel();
						private JLabel jLabelSearchDomain = new JLabel("Search:",SwingConstants.CENTER);
						private ButtonGroup buttonGroupSearchDomain = new ButtonGroup();
							private JRadioButton jRadioButtonWeb = new JRadioButton("the Web");
							private JRadioButton jRadioButtonPortugal = new JRadioButton("pages from Portugal");
					private JPanel jPanelFormSubmit = new JPanel();
						private JButton jButtonSubmit = new JButton("Submit");
			private JPanel jPanelInitialResultsTable = new JPanel();
				private JPanel jPanelInitialResultsTableSpace = new JPanel();
				private JPanel jPanelInitialResultsTableContent = new JPanel();
					private JPanel jPanelInitialResultsSpace = new JPanel();
					private JLabel jLabelInitialResultsTable = new JLabel("INITIAL RESULTS TABLE",SwingConstants.CENTER);
					private JScrollPane jScrollPaneInitialResults;
						private JTable jTableInitialResults;
				private JPanel jPanelInitialResultsSearchTime = new JPanel();
					private JLabel jLabelInitialResultsSearchTime = new JLabel("Google Search Time: ",SwingConstants.CENTER);
					private JTextField jTextFieldInitialResultsSearchTime = new JTextField();
			private JPanel jPanelAgentsTable = new JPanel();
				private JPanel jPanelAgentsTableSpace = new JPanel();
				private JPanel jPanelAgentsTableContent = new JPanel();
					private JPanel jPanelAgentsSpace = new JPanel();
					private JLabel jLabelAgentsTable = new JLabel("AGENTS TABLE",SwingConstants.CENTER);
					private JScrollPane jScrollPaneAgents;
						private JTable jTableAgents;
				private JPanel jPanelAgentsTime = new JPanel();
					private JLabel jLabelAgentsTime = new JLabel("Agents Time:",SwingConstants.CENTER);
					private JTextField jTextFieldAgentsTime = new JTextField();
			private JPanel jPanelWordsTable = new JPanel();
				private JPanel jPanelWordsTableSpace = new JPanel();
				private JPanel jPanelWordsTableContent = new JPanel();
					private JPanel jPanelWordsSpace = new JPanel();
					private JLabel jLabelWordsTable = new JLabel("WORDS TABLE",SwingConstants.CENTER);
					private JScrollPane jScrollPaneWords;
						private JTable jTableWords;
				private JPanel jPanelWordsCount = new JPanel();
					private JPanel jPanelTotalWords = new JPanel();
						private JLabel jLabelTotalWords = new JLabel("Total Words:",SwingConstants.CENTER);
						private JTextField jTextFieldTotalWords = new JTextField();
					private JPanel jPanelWordsOcurrances = new JPanel();
						private JLabel jLabelWordsOcurrances = new JLabel("Words Ocurrances Average:",SwingConstants.CENTER);
						private JTextField jTextFieldWordsOcurrances = new JTextField();
					private JPanel jPanelWordsTime = new JPanel();
						private JLabel jLabelWordsTime = new JLabel("Words Update Time:",SwingConstants.CENTER);
						private JTextField jTextFieldWordsTime = new JTextField();
			private JPanel jPanelWordsTree = new JPanel();
				private JPanel jPanelWordsTreeSpace = new JPanel();
				private JPanel jPanelWordsTreeContent = new JPanel();
			private JPanel jPanelWordsClusterSelection = new JPanel();
				private JPanel jPanelWordsClusterSelectionSpace = new JPanel();
				private JPanel jPanelWordsClusterSelectionContent = new JPanel();
					private JPanel jPanelWordsClusterSpace = new JPanel();
					private JLabel jLabelWordsClusterSelection = new JLabel("WORDS CLUSTERS SELECTION",SwingConstants.CENTER);
					private JScrollPane jScrollPaneWordsCluster;
						private JTable jTableWordsCluster;
				private JPanel jPanelAlgorithmTime = new JPanel();
					private JLabel jLabelAlgorithmTime = new JLabel("Algorithm Time:",SwingConstants.CENTER);
					private JTextField jTextFieldAlgorithmTime = new JTextField();
			private JPanel jPanelFinalResults = new JPanel();
				private JPanel jPanelFinalResultsSpace = new JPanel();
				private JPanel jPanelFinalResultsContent = new JPanel();
					private JPanel jPanelFinalResultsSpac = new JPanel();
					private JLabel jLabelFinalResults = new JLabel("FINAL RESULTS",SwingConstants.CENTER);
					private JScrollPane jScrollPaneFinalResults;
						private JTable jTableFinalResults;
				private JPanel jPanelFinalResultsSearchTime = new JPanel();
					private JLabel jLabelFinalResultsSearchTime = new JLabel("Google Search Time: ",SwingConstants.CENTER);
					private JTextField jTextFieldFinalResultsSearchTime = new JTextField();
	private JPanel jPanelSubTitle = new JPanel();
		private JLabel jLabelSubTitle = new JLabel("Andr� Barbosa & Filipe Montenegro (2004/2005)",SwingConstants.CENTER);

	public Interface()
	{
		String iconPath = classPath.substring(0,classPath.indexOf("bin/IntelligentWebSearch/")) + "pictures/02.png";
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		Dimension screenSize = new Dimension(1024, 768);

		this.setSize(screenSize);
		this.setResizable(false);
		this.setTitle("IntelligentWebSearch");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(iconPath));

			this.jPanelTitle.setPreferredSize(new Dimension(0,50));
			this.jPanelTitle.setBorder(new LineBorder(Color.blue));
			this.jPanelTitle.setBackground(Color.white);
				this.jLabelTitle.setFont(new Font("Comic Sans MS",Font.PLAIN,20));
				this.jLabelTitle.setPreferredSize(new Dimension(1000,40));
				this.jLabelTitle.setVerticalTextPosition(SwingConstants.CENTER);
			this.jPanelTitle.add(this.jLabelTitle,BorderLayout.CENTER);
		this.getContentPane().add(this.jPanelTitle,BorderLayout.NORTH);

		this.initializeMenu();
		this.initializeContent();

			this.jPanelSubTitle.setPreferredSize(new Dimension(0,50));
			this.jPanelSubTitle.setBorder(new LineBorder(Color.blue));
			this.jPanelSubTitle.setBackground(Color.white);
				this.jLabelSubTitle.setFont(new Font("Comic Sans MS",Font.PLAIN,16));
				this.jLabelSubTitle.setPreferredSize(new Dimension(1000,40));
				this.jLabelSubTitle.setVerticalTextPosition(SwingConstants.CENTER);
			this.jPanelSubTitle.add(this.jLabelSubTitle,BorderLayout.CENTER);
		this.getContentPane().add(this.jPanelSubTitle,BorderLayout.SOUTH);

		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent windowEvent)
			{
				exit();
			}
		});

		System.out.println("Running on " + screenSize.width + " x " + screenSize.height);
	}

	private void initializeMenu()
	{
			this.jPanelMenu.setPreferredSize(new Dimension(200,0));
			this.jPanelMenu.setBorder(new LineBorder(Color.blue));
			this.jPanelMenu.setBackground(Color.white);
				this.jPanelMenuSpace1.setPreferredSize(new Dimension(150,4));
				this.jPanelMenuSpace1.setBackground(Color.white);
			this.jPanelMenu.add(this.jPanelMenuSpace1,BorderLayout.CENTER);
				this.jLabelFileOptions.setPreferredSize(new Dimension(175,25));
				this.jLabelFileOptions.setBorder(new LineBorder(Color.black));
			this.jPanelMenu.add(this.jLabelFileOptions,BorderLayout.CENTER);
				this.jButtonNew.setPreferredSize(new Dimension(175,25));
				this.jButtonNew.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent actionEvent)
					{
						jButtonNewClick();
					}
				});
			this.jPanelMenu.add(this.jButtonNew,BorderLayout.CENTER);
				this.jButtonOpen.setPreferredSize(new Dimension(175,25));
				this.jButtonOpen.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent actionEvent)
					{
						jButtonOpenClick();
					}
				});
			this.jPanelMenu.add(this.jButtonOpen,BorderLayout.CENTER);
				this.jButtonSave.setPreferredSize(new Dimension(175,25));
				this.jButtonSave.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent actionEvent)
					{
						jButtonSaveClick();
					}
				});
			this.jPanelMenu.add(this.jButtonSave,BorderLayout.CENTER);
				this.jButtonSaveAs.setPreferredSize(new Dimension(175,25));
				this.jButtonSaveAs.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent actionEvent)
					{
						jButtonSaveAsClick();
					}
				});
			this.jPanelMenu.add(this.jButtonSaveAs,BorderLayout.CENTER);
				this.jPanelMenuSpace2.setPreferredSize(new Dimension(175,50));
				this.jPanelMenuSpace2.setBackground(Color.white);
			this.jPanelMenu.add(this.jPanelMenuSpace2,BorderLayout.CENTER);
				this.jLabelInitialResultsOptions.setPreferredSize(new Dimension(175,25));
				this.jLabelInitialResultsOptions.setBorder(new LineBorder(Color.black));
			this.jPanelMenu.add(this.jLabelInitialResultsOptions,BorderLayout.CENTER);
				this.jButtonSearchText.setPreferredSize(new Dimension(175,25));
				this.jButtonSearchText.setEnabled(false);
				this.jButtonSearchText.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent actionEvent)
					{
						jButtonSearchTextClick();
					}
				});
			this.jPanelMenu.add(this.jButtonSearchText,BorderLayout.CENTER);
				this.jButtonInitialResultsTable.setPreferredSize(new Dimension(175,25));
				this.jButtonInitialResultsTable.setEnabled(false);
				this.jButtonInitialResultsTable.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent actionEvent)
					{
						jButtonInitialResultsTableClick();
					}
				});
			this.jPanelMenu.add(this.jButtonInitialResultsTable,BorderLayout.CENTER);
				this.jButtonAgentsTable.setPreferredSize(new Dimension(175,25));
				this.jButtonAgentsTable.setEnabled(false);
				this.jButtonAgentsTable.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent actionEvent)
					{
						jButtonAgentsTableClick();
					}
				});
			this.jPanelMenu.add(this.jButtonAgentsTable,BorderLayout.CENTER);
				this.jButtonWordsTable.setPreferredSize(new Dimension(175,25));
				this.jButtonWordsTable.setEnabled(false);
				this.jButtonWordsTable.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent actionEvent)
					{
						jButtonWordsTableClick();
					}
				});
			this.jPanelMenu.add(this.jButtonWordsTable,BorderLayout.CENTER);
				this.jButtonWordsTree.setPreferredSize(new Dimension(175,25));
				this.jButtonWordsTree.setEnabled(false);
				this.jButtonWordsTree.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent actionEvent)
					{
						jButtonWordsTreeClick();
					}
				});
			this.jPanelMenu.add(this.jButtonWordsTree,BorderLayout.CENTER);
				this.jButtonWordsClusterSelection.setPreferredSize(new Dimension(175,25));
				this.jButtonWordsClusterSelection.setEnabled(false);
				this.jButtonWordsClusterSelection.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent actionEvent)
					{
						jButtonWordsClusterSelectionClick();
					}
				});
			this.jPanelMenu.add(this.jButtonWordsClusterSelection,BorderLayout.CENTER);
				this.jPanelMenuSpace3.setPreferredSize(new Dimension(175,50));
				this.jPanelMenuSpace3.setBackground(Color.white);
			this.jPanelMenu.add(this.jPanelMenuSpace3,BorderLayout.CENTER);
				this.jLabelFinalResultsOptions.setPreferredSize(new Dimension(175,25));
				this.jLabelFinalResultsOptions.setBorder(new LineBorder(Color.black));
			this.jPanelMenu.add(this.jLabelFinalResultsOptions,BorderLayout.CENTER);
				this.jButtonFinalResults.setPreferredSize(new Dimension(175,25));
				this.jButtonFinalResults.setEnabled(false);
				this.jButtonFinalResults.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent actionEvent)
					{
						jButtonFinalResultsClick();
					}
				});
			this.jPanelMenu.add(this.jButtonFinalResults,BorderLayout.CENTER);
		this.getContentPane().add(this.jPanelMenu,BorderLayout.WEST);
	}

	private void initializeContent()
	{
			this.jPanelContent.setBorder(new LineBorder(Color.blue));
			this.jPanelContent.setBackground(Color.white);
				this.jPanelContentSpace.setPreferredSize(new Dimension(700,40));
				this.jPanelContentSpace.setBackground(Color.white);
			this.jPanelContent.add(this.jPanelContentSpace,BorderLayout.NORTH);
				this.jTabbedPaneContent.setPreferredSize(new Dimension(700,530));
				initializeSearchText();
				this.jTabbedPaneContent.addTab("Search Text",this.jPanelSearchText);
				this.jTabbedPaneContent.addTab("Initial Results Table",this.jPanelInitialResultsTable);
				this.jTabbedPaneContent.addTab("Agents Table",this.jPanelAgentsTable);
				this.jTabbedPaneContent.addTab("Words Table",this.jPanelWordsTable);
				this.jTabbedPaneContent.addTab("Words Tree",this.jPanelWordsTree);
				this.jTabbedPaneContent.addTab("Words Cluster Selection",this.jPanelWordsClusterSelection);
				this.jTabbedPaneContent.addTab("Final Results",this.jPanelFinalResults);
				this.jTabbedPaneContent.setEnabledAt(1,false);
				this.jTabbedPaneContent.setEnabledAt(2,false);
				this.jTabbedPaneContent.setEnabledAt(3,false);
				this.jTabbedPaneContent.setEnabledAt(4,false);
				this.jTabbedPaneContent.setEnabledAt(5,false);
				this.jTabbedPaneContent.setEnabledAt(6,false);
			this.jPanelContent.add(this.jTabbedPaneContent,BorderLayout.CENTER);
		this.getContentPane().add(this.jPanelContent,BorderLayout.CENTER);
	}

	private void initializeSearchText()
	{
		this.jPanelSearchText.setBackground(Color.blue);
			this.jPanelSearchTextSpace.setPreferredSize(new Dimension(700,100));
			this.jPanelSearchTextSpace.setBackground(Color.blue);
		this.jPanelSearchText.add(this.jPanelSearchTextSpace,BorderLayout.NORTH);
			this.jLabelSearchTextTitle.setPreferredSize(new Dimension(400,25));
			this.jLabelSearchTextTitle.setOpaque(true);
			this.jLabelSearchTextTitle.setBackground(Color.blue);
			this.jLabelSearchTextTitle.setForeground(Color.white);
			this.jLabelSearchTextTitle.setBorder(new LineBorder(Color.white));
		this.jPanelSearchText.add(this.jLabelSearchTextTitle,BorderLayout.CENTER);
		this.jPanelSearchText.setPreferredSize(new Dimension(700,530));
			this.jPanelForm.setPreferredSize(new Dimension(400,250));
			this.jPanelForm.setBorder(new EtchedBorder(Color.white,Color.white));
			this.jPanelForm.setBackground(Color.blue);
				this.jPanelFormSpace.setPreferredSize(new Dimension(350,10));
				this.jPanelFormSpace.setBackground(Color.blue);
			this.jPanelForm.add(this.jPanelFormSpace,BorderLayout.NORTH);
				this.jPanelFormSearchText.setPreferredSize(new Dimension(350,25));
				this.jPanelFormSearchText.setBackground(Color.blue);
					this.jLabelSearchText.setForeground(Color.white);
				this.jPanelFormSearchText.add(this.jLabelSearchText,BorderLayout.WEST);
					this.jTextFieldSearchText.setPreferredSize(new Dimension(250,20));
				this.jPanelFormSearchText.add(this.jTextFieldSearchText,BorderLayout.EAST);
			this.jPanelForm.add(this.jPanelFormSearchText,BorderLayout.NORTH);
				this.jPanelFormInitialResultsDepthLevel.setPreferredSize(new Dimension(350,30));
				this.jPanelFormInitialResultsDepthLevel.setBackground(Color.blue);
					this.jLabelInitialResults.setForeground(Color.white);
				this.jPanelFormInitialResultsDepthLevel.add(this.jLabelInitialResults,BorderLayout.WEST);
					this.jComboBoxInitialResults.setBackground(Color.white);
				this.jPanelFormInitialResultsDepthLevel.add(this.jComboBoxInitialResults,BorderLayout.WEST);
					this.jPanelInitialResultsDepthLevelSpace.setPreferredSize(new Dimension(95,25));
					this.jPanelInitialResultsDepthLevelSpace.setBackground(Color.blue);
				this.jPanelFormInitialResultsDepthLevel.add(this.jPanelInitialResultsDepthLevelSpace,BorderLayout.CENTER);
					this.jLabelDepthLevel.setForeground(Color.white);
				this.jPanelFormInitialResultsDepthLevel.add(this.jLabelDepthLevel,BorderLayout.EAST);
					this.jComboBoxDepthLevel.setBackground(Color.white);
				this.jPanelFormInitialResultsDepthLevel.add(this.jComboBoxDepthLevel,BorderLayout.EAST);
			this.jPanelForm.add(this.jPanelFormInitialResultsDepthLevel,BorderLayout.CENTER);
				this.jPanelFormAlgorithms.setPreferredSize(new Dimension(350,30));
				this.jPanelFormAlgorithms.setBackground(Color.blue);
					this.jLabelWordsOrientation.setForeground(Color.white);
				this.jPanelFormAlgorithms.add(this.jLabelWordsOrientation,BorderLayout.WEST);
					this.jComboBoxWordsOrientation.setBackground(Color.white);
				this.jPanelFormAlgorithms.add(this.jComboBoxWordsOrientation,BorderLayout.WEST);
					this.jPanelAlgorithmsSpace.setPreferredSize(new Dimension(37,25));
					this.jPanelAlgorithmsSpace.setBackground(Color.blue);
				this.jPanelFormAlgorithms.add(this.jPanelAlgorithmsSpace,BorderLayout.CENTER);
					this.jLabelAlgorithm.setForeground(Color.white);
				this.jPanelFormAlgorithms.add(this.jLabelAlgorithm,BorderLayout.EAST);
					this.jComboBoxAlgorithm.setBackground(Color.white);
				this.jPanelFormAlgorithms.add(this.jComboBoxAlgorithm,BorderLayout.EAST);
			this.jPanelForm.add(this.jPanelFormAlgorithms,BorderLayout.CENTER);
				this.jPanelWordsDelta.setPreferredSize(new Dimension(350,30));
				this.jPanelWordsDelta.setBackground(Color.blue);
					this.jLabelWords.setForeground(Color.white);
				this.jPanelWordsDelta.add(this.jLabelWords,BorderLayout.WEST);
					this.jComboBoxWords.setBackground(Color.white);
				this.jPanelWordsDelta.add(this.jComboBoxWords,BorderLayout.WEST);
					this.jPanelWordsDeltaSpace.setPreferredSize(new Dimension(138,25));
					this.jPanelWordsDeltaSpace.setBackground(Color.blue);
				this.jPanelWordsDelta.add(this.jPanelWordsDeltaSpace,BorderLayout.CENTER);
					this.jLabelDelta.setForeground(Color.white);
				this.jPanelWordsDelta.add(this.jLabelDelta,BorderLayout.EAST);
					this.jComboBoxDelta.setBackground(Color.white);
				this.jPanelWordsDelta.add(this.jComboBoxDelta,BorderLayout.EAST);
			this.jPanelForm.add(this.jPanelWordsDelta,BorderLayout.CENTER);
				this.jPanelFormSearchDomain.setPreferredSize(new Dimension(350,30));
				this.jPanelFormSearchDomain.setBackground(Color.blue);
					this.jLabelSearchDomain.setForeground(Color.white);
				this.jPanelFormSearchDomain.add(this.jLabelSearchDomain,BorderLayout.WEST);
						this.jRadioButtonWeb.setForeground(Color.white);
						this.jRadioButtonWeb.setBackground(Color.blue);
					this.buttonGroupSearchDomain.add(this.jRadioButtonWeb);
						this.jRadioButtonPortugal.setForeground(Color.white);
						this.jRadioButtonPortugal.setBackground(Color.blue);
					this.buttonGroupSearchDomain.add(this.jRadioButtonPortugal);
					this.jRadioButtonPortugal.setSelected(true);
				this.jPanelFormSearchDomain.add(this.jRadioButtonWeb,BorderLayout.CENTER);
				this.jPanelFormSearchDomain.add(this.jRadioButtonPortugal,BorderLayout.EAST);
			this.jPanelForm.add(this.jPanelFormSearchDomain,BorderLayout.CENTER);
				this.jPanelFormSubmit.setPreferredSize(new Dimension(350,30));
				this.jPanelFormSubmit.setBackground(Color.blue);
					this.jButtonSubmit.setPreferredSize(new Dimension(343,25));
					this.jButtonSubmit.setForeground(Color.blue);
					this.jButtonSubmit.setBackground(Color.white);
					this.jButtonSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
					this.jButtonSubmit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent actionEvent)
						{
							if(checkSearchFields())
								runSearch();
						}
					});
				this.jPanelFormSubmit.add(this.jButtonSubmit,BorderLayout.CENTER);
			this.jPanelForm.add(this.jPanelFormSubmit,BorderLayout.SOUTH);
		this.jPanelSearchText.add(this.jPanelForm,BorderLayout.CENTER);

		this.jButtonSearchText.setEnabled(true);
	}

	private void initializeInitialResultsTable()
	{
		Database.connect("jdbc:mysql://paranhos.fe.up.pt:3306/","ei01043","ax",this.getClass().getResource("").getPath());
		LinkedList linksList = Database.getLinks(0);
		Database.disconnect();

		CustomTableModel customTableModel = new CustomTableModel(linksList.size(),2);

		customTableModel.setColumnName(0,"URL");
		customTableModel.setColumnName(1,"State");

		for(int i = 0; i < linksList.size(); i++)
		{
			customTableModel.setValueAt((String) linksList.get(i),i,0);

			try
			{
				URL url = new URL((String) linksList.get(i));
				customTableModel.setValueAt("Enabled",i,1);
			}
			catch(Exception e)
			{
				customTableModel.setValueAt("Disabled",i,1);
			}
		}

		this.jTableInitialResults = new JTable(customTableModel);
		this.jTableInitialResults.getColumnModel().getColumn(0).setPreferredWidth(400);
		this.jTableInitialResults.getColumnModel().getColumn(1).setPreferredWidth(100);

        try
        {
            this.jTableInitialResults.setDefaultRenderer(Class.forName("java.lang.String"),new CustomTableCellRenderer());
        }
        catch(Exception e)
        {
        }

		this.jPanelInitialResultsTable.setBackground(Color.blue);
			this.jPanelInitialResultsTableSpace.setPreferredSize(new Dimension(700,90));
			this.jPanelInitialResultsTableSpace.setBackground(Color.blue);
		this.jPanelInitialResultsTable.add(this.jPanelInitialResultsTableSpace,BorderLayout.NORTH);
			this.jPanelInitialResultsTableContent.setPreferredSize(new Dimension(550,250));
			this.jPanelInitialResultsTableContent.setBorder(new EtchedBorder(Color.white,Color.white));
			this.jPanelInitialResultsTableContent.setBackground(Color.blue);
				this.jPanelInitialResultsSpace.setPreferredSize(new Dimension(500,10));
				this.jPanelInitialResultsSpace.setBackground(Color.blue);
			this.jPanelInitialResultsTableContent.add(jPanelInitialResultsSpace,BorderLayout.NORTH);
				this.jLabelInitialResultsTable.setPreferredSize(new Dimension(500,25));
				this.jLabelInitialResultsTable.setBorder(new LineBorder(Color.white));
				this.jLabelInitialResultsTable.setOpaque(true);
				this.jLabelInitialResultsTable.setBackground(Color.blue);
				this.jLabelInitialResultsTable.setForeground(Color.white);
			this.jPanelInitialResultsTableContent.add(this.jLabelInitialResultsTable,BorderLayout.NORTH);
				this.jScrollPaneInitialResults = new JScrollPane(this.jTableInitialResults);
				this.jScrollPaneInitialResults.setPreferredSize(new Dimension(500,180));
			this.jPanelInitialResultsTableContent.add(this.jScrollPaneInitialResults,BorderLayout.CENTER);
		this.jPanelInitialResultsTable.add(this.jPanelInitialResultsTableContent,BorderLayout.CENTER);
			this.jPanelInitialResultsSearchTime.setPreferredSize(new Dimension(550,35));
			this.jPanelInitialResultsSearchTime.setBorder(new EtchedBorder(Color.white,Color.white));
			this.jPanelInitialResultsSearchTime.setBackground(Color.blue);
					this.jLabelInitialResultsSearchTime.setPreferredSize(new Dimension(200,20));
					this.jLabelInitialResultsSearchTime.setBorder(new LineBorder(Color.white));
					this.jLabelInitialResultsSearchTime.setOpaque(true);
					this.jLabelInitialResultsSearchTime.setBackground(Color.blue);
					this.jLabelInitialResultsSearchTime.setForeground(Color.white);
				this.jPanelInitialResultsSearchTime.add(this.jLabelInitialResultsSearchTime,BorderLayout.WEST);
					this.jTextFieldInitialResultsSearchTime.setPreferredSize(new Dimension(200,20));
					this.jTextFieldInitialResultsSearchTime.setHorizontalAlignment(SwingConstants.CENTER);
					String time = new Double(Google.getSearchTime()).toString();
					if(time.length() > 7)
						time = time.substring(0,7);
					this.jTextFieldInitialResultsSearchTime.setText(time + " segundos");
				this.jPanelInitialResultsSearchTime.add(this.jTextFieldInitialResultsSearchTime,BorderLayout.WEST);
		this.jPanelInitialResultsTable.add(this.jPanelInitialResultsSearchTime,BorderLayout.SOUTH);

		this.jTabbedPaneContent.setEnabledAt(1,true);
		this.jButtonInitialResultsTable.setEnabled(true);
	}

	private void initializeAgentsTable()
	{
		Database.connect("jdbc:mysql://paranhos.fe.up.pt:3306/","ei01043","ax",this.getClass().getResource("").getPath());
		int numAgents = Database.getNumAgents();

		CustomTableModel customTableModel = new CustomTableModel(numAgents,4);

		customTableModel.setColumnName(0,"Agent");
		customTableModel.setColumnName(1,"Initial URL");
		customTableModel.setColumnName(2,"Links");
		customTableModel.setColumnName(3,"Words");

		for(int i = 0; i < numAgents; i++)
		{
			customTableModel.setValueAt(Database.getAgentName(i + 1),i,0);
			customTableModel.setValueAt(Database.getAgentLink(i + 1),i,1);
			customTableModel.setValueAt(new Integer(0),i,2);
			customTableModel.setValueAt(new Integer(Database.getAgentNumWords(i + 1)),i,3);
		}

		Database.disconnect();

		this.jTableAgents = new JTable(customTableModel);
		this.jTableAgents.getColumnModel().getColumn(0).setPreferredWidth(220);
		this.jTableAgents.getColumnModel().getColumn(1).setPreferredWidth(300);
		this.jTableAgents.getColumnModel().getColumn(2).setPreferredWidth(40);
		this.jTableAgents.getColumnModel().getColumn(3).setPreferredWidth(40);

        try
        {
            this.jTableAgents.setDefaultRenderer(Class.forName("java.lang.String"),new CustomTableCellRenderer());
        }
        catch(Exception e)
        {
        }

		this.jPanelAgentsTable.setBackground(Color.blue);
			this.jPanelAgentsTableSpace.setPreferredSize(new Dimension(700,90));
			this.jPanelAgentsTableSpace.setBackground(Color.blue);
		this.jPanelAgentsTable.add(this.jPanelAgentsTableSpace,BorderLayout.NORTH);
			this.jPanelAgentsTableContent.setPreferredSize(new Dimension(650,250));
			this.jPanelAgentsTableContent.setBorder(new EtchedBorder(Color.white,Color.white));
			this.jPanelAgentsTableContent.setBackground(Color.blue);
				this.jPanelAgentsSpace.setPreferredSize(new Dimension(600,10));
				this.jPanelAgentsSpace.setBackground(Color.blue);
			this.jPanelAgentsTableContent.add(this.jPanelAgentsSpace,BorderLayout.NORTH);
				this.jLabelAgentsTable.setPreferredSize(new Dimension(600,25));
				this.jLabelAgentsTable.setBorder(new LineBorder(Color.white));
				this.jLabelAgentsTable.setOpaque(true);
				this.jLabelAgentsTable.setBackground(Color.blue);
				this.jLabelAgentsTable.setForeground(Color.white);
			this.jPanelAgentsTableContent.add(this.jLabelAgentsTable,BorderLayout.NORTH);
				this.jScrollPaneAgents = new JScrollPane(this.jTableAgents);
				this.jScrollPaneAgents.setPreferredSize(new Dimension(600,180));
			this.jPanelAgentsTableContent.add(this.jScrollPaneAgents,BorderLayout.CENTER);
		this.jPanelAgentsTable.add(this.jPanelAgentsTableContent,BorderLayout.CENTER);
			this.jPanelAgentsTime.setPreferredSize(new Dimension(650,35));
			this.jPanelAgentsTime.setBackground(Color.blue);
			this.jPanelAgentsTime.setBorder(new EtchedBorder(Color.white,Color.white));
				this.jLabelAgentsTime.setPreferredSize(new Dimension(200,20));
				this.jLabelAgentsTime.setBorder(new LineBorder(Color.white));
				this.jLabelAgentsTime.setOpaque(true);
				this.jLabelAgentsTime.setBackground(Color.blue);
				this.jLabelAgentsTime.setForeground(Color.white);
			this.jPanelAgentsTime.add(this.jLabelAgentsTime,BorderLayout.CENTER);
				this.jTextFieldAgentsTime.setPreferredSize(new Dimension(200,20));
				this.jTextFieldAgentsTime.setHorizontalAlignment(SwingConstants.CENTER);
				String time = new Double(agentsTime).toString();
					if(time.length() > 7)
						time = time.substring(0,7);
				this.jTextFieldAgentsTime.setText(time + " segundos");
			this.jPanelAgentsTime.add(this.jTextFieldAgentsTime,BorderLayout.CENTER);
		this.jPanelAgentsTable.add(this.jPanelAgentsTime,BorderLayout.SOUTH);

		this.jTabbedPaneContent.setEnabledAt(2,true);
		this.jButtonAgentsTable.setEnabled(true);
	}

	private void initializeWordsTable()
	{
		Database.connect("jdbc:mysql://paranhos.fe.up.pt:3306/","ei01043","ax",this.getClass().getResource("").getPath());
		int numWords = Database.getNumWords();
		int numOcurrances = Database.getWordOcurrances();

		CustomTableModel customTableModel = new CustomTableModel(numWords,2);

		customTableModel.setColumnName(0,"Word");
		customTableModel.setColumnName(1,"Ocurrances");

		for(int i = 0; i < numWords; i++)
		{
			customTableModel.setValueAt(Database.getWord(i + 1),i,0);
			customTableModel.setValueAt(new Integer(Database.getWordOcurrances(i + 1)),i,1);
		}

		Database.disconnect();

		this.jTableWords = new JTable(customTableModel);
		this.jTableWords.getColumnModel().getColumn(0).setPreferredWidth(250);
		this.jTableWords.getColumnModel().getColumn(1).setPreferredWidth(150);

        try
        {
            this.jTableWords.setDefaultRenderer(Class.forName("java.lang.String"),new CustomTableCellRenderer());
        }
        catch(Exception e)
        {
        }

		this.jPanelWordsTable.setBackground(Color.blue);
			this.jPanelWordsTableSpace.setPreferredSize(new Dimension(700,50));
			this.jPanelWordsTableSpace.setBackground(Color.blue);
		this.jPanelWordsTable.add(this.jPanelWordsTableSpace,BorderLayout.NORTH);
			this.jPanelWordsTableContent.setPreferredSize(new Dimension(450,250));
			this.jPanelWordsTableContent.setBorder(new EtchedBorder(Color.white,Color.white));
			this.jPanelWordsTableContent.setBackground(Color.blue);
				this.jPanelWordsSpace.setPreferredSize(new Dimension(400,10));
				this.jPanelWordsSpace.setBackground(Color.blue);
			this.jPanelWordsTableContent.add(jPanelWordsSpace,BorderLayout.NORTH);
				this.jLabelWordsTable.setPreferredSize(new Dimension(400,25));
				this.jLabelWordsTable.setBorder(new LineBorder(Color.white));
				this.jLabelWordsTable.setOpaque(true);
				this.jLabelWordsTable.setBackground(Color.blue);
				this.jLabelWordsTable.setForeground(Color.white);
			this.jPanelWordsTableContent.add(this.jLabelWordsTable,BorderLayout.NORTH);
				this.jScrollPaneWords = new JScrollPane(this.jTableWords);
				this.jScrollPaneWords.setPreferredSize(new Dimension(400,180));
			this.jPanelWordsTableContent.add(this.jScrollPaneWords,BorderLayout.CENTER);
		this.jPanelWordsTable.add(this.jPanelWordsTableContent,BorderLayout.CENTER);
			this.jPanelWordsCount.setPreferredSize(new Dimension(450,110));
			this.jPanelWordsCount.setBorder(new EtchedBorder(Color.white,Color.white));
			this.jPanelWordsCount.setBackground(Color.blue);
				this.jPanelTotalWords.setPreferredSize(new Dimension(400,25));
				this.jPanelTotalWords.setBackground(Color.blue);
					this.jLabelTotalWords.setPreferredSize(new Dimension(190,20));
					this.jLabelTotalWords.setForeground(Color.white);
				this.jPanelTotalWords.add(this.jLabelTotalWords,BorderLayout.CENTER);
					this.jTextFieldTotalWords.setPreferredSize(new Dimension(100,20));
					this.jTextFieldTotalWords.setHorizontalAlignment(SwingConstants.CENTER);
					this.jTextFieldTotalWords.setText("" + numWords);
				this.jPanelTotalWords.add(this.jTextFieldTotalWords,BorderLayout.CENTER);
			this.jPanelWordsCount.add(this.jPanelTotalWords,BorderLayout.NORTH);
				this.jPanelWordsOcurrances.setPreferredSize(new Dimension(400,25));
				this.jPanelWordsOcurrances.setBackground(Color.blue);
					this.jLabelWordsOcurrances.setPreferredSize(new Dimension(190,20));
					this.jLabelWordsOcurrances.setForeground(Color.white);
				this.jPanelWordsOcurrances.add(this.jLabelWordsOcurrances,BorderLayout.CENTER);
					this.jTextFieldWordsOcurrances.setPreferredSize(new Dimension(100,20));
					this.jTextFieldWordsOcurrances.setHorizontalAlignment(SwingConstants.CENTER);
					String ocurs = new Double((double) numOcurrances / (double) numWords).toString();
					if(ocurs.length() > 7)
						ocurs = ocurs.substring(0,7);
					this.jTextFieldWordsOcurrances.setText(ocurs);
				this.jPanelWordsOcurrances.add(this.jTextFieldWordsOcurrances,BorderLayout.CENTER);
			this.jPanelWordsCount.add(this.jPanelWordsOcurrances,BorderLayout.CENTER);
				this.jPanelWordsTime.setPreferredSize(new Dimension(400,25));
				this.jPanelWordsTime.setBackground(Color.blue);
					this.jLabelWordsTime.setPreferredSize(new Dimension(190,20));
					this.jLabelWordsTime.setBorder(new LineBorder(Color.white));
					this.jLabelWordsTime.setOpaque(true);
					this.jLabelWordsTime.setBackground(Color.blue);
					this.jLabelWordsTime.setForeground(Color.white);
				this.jPanelWordsTime.add(this.jLabelWordsTime,BorderLayout.CENTER);
					this.jTextFieldWordsTime.setPreferredSize(new Dimension(190,20));
					this.jTextFieldWordsTime.setHorizontalAlignment(SwingConstants.CENTER);
					String time = new Double(wordsTime).toString();
					if(time.length() > 7)
						time = time.substring(0,7);
					this.jTextFieldWordsTime.setText(time + " segundos");
				this.jPanelWordsTime.add(this.jTextFieldWordsTime,BorderLayout.CENTER);
			this.jPanelWordsCount.add(this.jPanelWordsTime,BorderLayout.SOUTH);
		this.jPanelWordsTable.add(this.jPanelWordsCount,BorderLayout.SOUTH);

		this.jTabbedPaneContent.setEnabledAt(3,true);
		this.jButtonWordsTable.setEnabled(true);
	}

	private void initializeWordsTree()
	{
	}

	private void initializeWordsClusterSelection()
	{
		CustomTableModel customTableModel = new CustomTableModel(wordsClusters.size(),2);

		customTableModel.setColumnName(0,"Cluster");
		customTableModel.setColumnName(1,"Words");

		for(int i = 0; i < wordsClusters.size(); i++)
		{
			ListIterator clusterIt = ((LinkedList) wordsClusters.get(i)).listIterator();
			String wordCluster = "";

			while(clusterIt.hasNext())
				wordCluster += (String) clusterIt.next() + ", ";
			wordCluster = wordCluster.substring(0,wordCluster.length() - 2);

			customTableModel.setValueAt("Cluster " + (i + 1),i,0);
			customTableModel.setValueAt(wordCluster,i,1);
		}

		this.jTableWordsCluster = new JTable(customTableModel);
		this.jTableWordsCluster.getColumnModel().getColumn(0).setPreferredWidth(50);
		this.jTableWordsCluster.getColumnModel().getColumn(1).setPreferredWidth(450);
		this.jTableWordsCluster.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.jTableWordsCluster.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent mouseEvent)
			{
				int numWords = 0;
				newSearchText = "";
				String cluster = (String) jTableWordsCluster.getValueAt(jTableWordsCluster.rowAtPoint(mouseEvent.getPoint()),1);

				StringTokenizer strTok = new StringTokenizer(searchText," ");
				while(strTok.hasMoreTokens())
				{
					newSearchText += strTok.nextToken() + " ";
					numWords++;
				}

				strTok = new StringTokenizer(cluster,", ");

				if(wordsOrientation == 1)
				{
					for(int i = numWords; i < 10; i++)
					{
						if(!strTok.hasMoreTokens())
							break;

						newSearchText += strTok.nextToken() + " ";
					}
				}
				else
				{
					for(int i = numWords; i < 11; i++)
					{
						if(!strTok.hasMoreTokens())
							break;

						if(i != numWords)
							newSearchText += strTok.nextToken() + " ";
						else
							strTok.nextToken();
					}
				}
				newSearchText = newSearchText.substring(0,newSearchText.length() - 1);

				if(JOptionPane.showConfirmDialog(null,"Fazer nova pesquisa com \"" + newSearchText + "\"?","Nova pesquisa?",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{
					newSearch = true;
					initialGoogleResults = 10;
					runSearch();
				}
			}

			public void mouseEntered(MouseEvent mouseEvent){}
			public void mouseExited(MouseEvent mouseEvent){}
			public void mousePressed(MouseEvent mouseEvent){}
			public void mouseReleased(MouseEvent mouseEvent){}
		});

        try
        {
            this.jTableWordsCluster.setDefaultRenderer(Class.forName("java.lang.String"),new CustomTableCellRenderer());
        }
        catch(Exception e)
        {
        }

		this.jPanelWordsClusterSelection.setBackground(Color.blue);
			this.jPanelWordsClusterSelectionSpace.setPreferredSize(new Dimension(700,90));
			this.jPanelWordsClusterSelectionSpace.setBackground(Color.blue);
		this.jPanelWordsClusterSelection.add(this.jPanelWordsClusterSelectionSpace,BorderLayout.NORTH);
			this.jPanelWordsClusterSelectionContent.setPreferredSize(new Dimension(650,250));
			this.jPanelWordsClusterSelectionContent.setBorder(new EtchedBorder(Color.white,Color.white));
			this.jPanelWordsClusterSelectionContent.setBackground(Color.blue);
				this.jPanelWordsClusterSpace.setPreferredSize(new Dimension(600,10));
				this.jPanelWordsClusterSpace.setBackground(Color.blue);
			this.jPanelWordsClusterSelectionContent.add(jPanelWordsClusterSpace,BorderLayout.NORTH);
				this.jLabelWordsClusterSelection.setPreferredSize(new Dimension(600,25));
				this.jLabelWordsClusterSelection.setBorder(new LineBorder(Color.white));
				this.jLabelWordsClusterSelection.setOpaque(true);
				this.jLabelWordsClusterSelection.setBackground(Color.blue);
				this.jLabelWordsClusterSelection.setForeground(Color.white);
			this.jPanelWordsClusterSelectionContent.add(this.jLabelWordsClusterSelection,BorderLayout.NORTH);
				this.jScrollPaneWordsCluster = new JScrollPane(this.jTableWordsCluster);
				this.jScrollPaneWordsCluster.setPreferredSize(new Dimension(600,180));
			this.jPanelWordsClusterSelectionContent.add(this.jScrollPaneWordsCluster,BorderLayout.CENTER);
		this.jPanelWordsClusterSelection.add(this.jPanelWordsClusterSelectionContent,BorderLayout.CENTER);
			this.jPanelAlgorithmTime.setPreferredSize(new Dimension(650,35));
			this.jPanelAlgorithmTime.setBackground(Color.blue);
			this.jPanelAlgorithmTime.setBorder(new EtchedBorder(Color.white,Color.white));
				this.jLabelAlgorithmTime.setPreferredSize(new Dimension(200,20));
				this.jLabelAlgorithmTime.setBorder(new LineBorder(Color.white));
				this.jLabelAlgorithmTime.setOpaque(true);
				this.jLabelAlgorithmTime.setBackground(Color.blue);
				this.jLabelAlgorithmTime.setForeground(Color.white);
			this.jPanelAlgorithmTime.add(this.jLabelAlgorithmTime,BorderLayout.CENTER);
				this.jTextFieldAlgorithmTime.setPreferredSize(new Dimension(200,20));
				this.jTextFieldAlgorithmTime.setHorizontalAlignment(SwingConstants.CENTER);
				String time = new Double(algorithmTime).toString();
					if(time.length() > 7)
						time = time.substring(0,7);
				this.jTextFieldAlgorithmTime.setText(time + " segundos");
			this.jPanelAlgorithmTime.add(this.jTextFieldAlgorithmTime,BorderLayout.CENTER);
		this.jPanelWordsClusterSelection.add(this.jPanelAlgorithmTime,BorderLayout.SOUTH);

		this.jTabbedPaneContent.setEnabledAt(5,true);
		this.jTabbedPaneContent.setSelectedIndex(5);
		this.jButtonWordsClusterSelection.setEnabled(true);
	}

	private void initializeFinalResults()
	{
		String links[] = Google.getSearchResults();

		CustomTableModel customTableModel = new CustomTableModel(links.length,2);

		customTableModel.setColumnName(0,"URL");
		customTableModel.setColumnName(1,"State");

		for(int i = 0; i < links.length; i++)
		{
			customTableModel.setValueAt(links[i],i,0);

			try
			{
				URL url = new URL(links[i]);
				customTableModel.setValueAt("Enabled",i,1);
			}
			catch(Exception e)
			{
				customTableModel.setValueAt("Disabled",i,1);
			}
		}

		this.jTableFinalResults = new JTable(customTableModel);
		this.jTableFinalResults.getColumnModel().getColumn(0).setPreferredWidth(400);
		this.jTableFinalResults.getColumnModel().getColumn(1).setPreferredWidth(100);
		this.jTableFinalResults.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.jTableFinalResults.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent mouseEvent)
			{
				try
				{
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + (String) jTableFinalResults.getValueAt(jTableFinalResults.rowAtPoint(mouseEvent.getPoint()),0));
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(null,"System can't open a browser in your computer","Error opening browser!",JOptionPane.ERROR_MESSAGE);
				}
			}

			public void mouseEntered(MouseEvent mouseEvent){}
			public void mouseExited(MouseEvent mouseEvent){}
			public void mousePressed(MouseEvent mouseEvent){}
			public void mouseReleased(MouseEvent mouseEvent){}
		});

        try
        {
            this.jTableFinalResults.setDefaultRenderer(Class.forName("java.lang.String"),new CustomTableCellRenderer());
        }
        catch(Exception e)
        {
        }

		this.jPanelFinalResultsContent.removeAll();
		this.jPanelFinalResultsSearchTime.removeAll();
		this.jPanelFinalResults.removeAll();

		this.jPanelFinalResults.setBackground(Color.blue);
			this.jPanelFinalResultsSpace.setPreferredSize(new Dimension(700,90));
			this.jPanelFinalResultsSpace.setBackground(Color.blue);
		this.jPanelFinalResults.add(this.jPanelFinalResultsSpace,BorderLayout.NORTH);
			this.jPanelFinalResultsContent.setPreferredSize(new Dimension(550,250));
			this.jPanelFinalResultsContent.setBorder(new EtchedBorder(Color.white,Color.white));
			this.jPanelFinalResultsContent.setBackground(Color.blue);
				this.jPanelFinalResultsSpac.setPreferredSize(new Dimension(500,10));
				this.jPanelFinalResultsSpac.setBackground(Color.blue);
			this.jPanelFinalResultsContent.add(this.jPanelFinalResultsSpac,BorderLayout.NORTH);
				this.jLabelFinalResults.setPreferredSize(new Dimension(500,25));
				this.jLabelFinalResults.setBorder(new LineBorder(Color.white));
				this.jLabelFinalResults.setOpaque(true);
				this.jLabelFinalResults.setBackground(Color.blue);
				this.jLabelFinalResults.setForeground(Color.white);
			this.jPanelFinalResultsContent.add(this.jLabelFinalResults,BorderLayout.NORTH);
				this.jScrollPaneFinalResults = new JScrollPane(this.jTableFinalResults);
				this.jScrollPaneFinalResults.setPreferredSize(new Dimension(500,180));
			this.jPanelFinalResultsContent.add(this.jScrollPaneFinalResults,BorderLayout.CENTER);
		this.jPanelFinalResults.add(this.jPanelFinalResultsContent,BorderLayout.CENTER);
			this.jPanelFinalResultsSearchTime.setPreferredSize(new Dimension(550,35));
			this.jPanelFinalResultsSearchTime.setBorder(new EtchedBorder(Color.white,Color.white));
			this.jPanelFinalResultsSearchTime.setBackground(Color.blue);
					this.jLabelFinalResultsSearchTime.setPreferredSize(new Dimension(200,20));
					this.jLabelFinalResultsSearchTime.setBorder(new LineBorder(Color.white));
					this.jLabelFinalResultsSearchTime.setOpaque(true);
					this.jLabelFinalResultsSearchTime.setBackground(Color.blue);
					this.jLabelFinalResultsSearchTime.setForeground(Color.white);
				this.jPanelFinalResultsSearchTime.add(this.jLabelFinalResultsSearchTime,BorderLayout.WEST);
					this.jTextFieldFinalResultsSearchTime.setPreferredSize(new Dimension(200,20));
					this.jTextFieldFinalResultsSearchTime.setHorizontalAlignment(SwingConstants.CENTER);
					String time = new Double(Google.getSearchTime()).toString();
					if(time.length() > 7)
						time = time.substring(0,7);
					this.jTextFieldFinalResultsSearchTime.setText(time + " segundos");
				this.jPanelFinalResultsSearchTime.add(this.jTextFieldFinalResultsSearchTime,BorderLayout.WEST);
		this.jPanelFinalResults.add(this.jPanelFinalResultsSearchTime,BorderLayout.SOUTH);

		this.jTabbedPaneContent.setEnabledAt(6,true);
		this.jTabbedPaneContent.setSelectedIndex(6);
		this.jButtonFinalResults.setEnabled(true);
	}

	private void jButtonNewClick()
	{
		if(this.fileChanged && !this.fileSaved)
			JOptionPane.showMessageDialog(null,"Guardar Pesquisa?");
		else
		{
			this.fileNew = true;
			this.fileChanged = false;
			this.fileSaved = false;
			this.fileSavedBefore = false;
			this.fileOpened = false;
		}
	}

	private void jButtonOpenClick()
	{
		if(this.fileChanged && !this.fileSaved)
		{
			JOptionPane.showMessageDialog(null,"Guardar Pesquisa?");
		}
		else
		{
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileFilter(fileFilter);

			if(jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			{
				try
				{
					BufferedReader bufReader = new BufferedReader(new FileReader(jFileChooser.getSelectedFile().toString()));

					if(!Utils.checkFile(bufReader))
						JOptionPane.showMessageDialog(null,"Selected file does not have the right format","Error analysing file",JOptionPane.ERROR_MESSAGE);
					else
					{
						this.fileOpened = true;
						this.fileNew = false;
						this.fileChanged = false;
						this.fileSaved = false;
						this.fileSavedBefore = false;
					}

					bufReader.close();
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(null,"Selected file does not exists","Error opening file",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void jButtonSaveClick()
	{
		if(this.fileNew || this.fileOpened)
		{
			if(this.fileChanged && !this.fileSaved)
			{
				if(this.fileSavedBefore)
				{
					this.fileSaved = true;
					this.fileChanged = false;
				}
				else
				{
					JFileChooser jFileChooser = new JFileChooser();
					jFileChooser.setFileFilter(fileFilter);
					
					if(jFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
					{
						try
						{
							BufferedWriter bufWriter = new BufferedWriter(new FileWriter(jFileChooser.getSelectedFile().toString()));
							bufWriter.close();

							filePath = jFileChooser.getSelectedFile().toString();

							this.fileSaved = true;
							this.fileChanged = false;
							this.fileSavedBefore = true;
						}
						catch(Exception e)
						{
							JOptionPane.showMessageDialog(null,"System cannot save file in specified path","Error saving file",JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		}
	}

	private void jButtonSaveAsClick()
	{
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileFilter(fileFilter);
		
		if(this.fileSavedBefore)
			jFileChooser.setSelectedFile(new File(filePath));
		
		if(jFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				BufferedWriter bufWriter = new BufferedWriter(new FileWriter(jFileChooser.getSelectedFile().toString()));
				bufWriter.close();

				filePath = jFileChooser.getSelectedFile().toString();

				this.fileSaved = true;
				this.fileChanged = false;
				this.fileSavedBefore = true;
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(null,"System cannot save file in specified path","Error saving file",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void jButtonSearchTextClick()
	{
		System.out.println("SEARCH TEXT");
	}

	private void jButtonInitialResultsTableClick()
	{
		System.out.println("INITIAL RESULTS TABLE");
	}

	private void jButtonAgentsTableClick()
	{
		System.out.println("AGENTS TABLE");
	}

	private void jButtonWordsTableClick()
	{
		System.out.println("WORDS TABLE");
	}

	private void jButtonWordsTreeClick()
	{
		System.out.println("WORDS TREE");
	}

	private void jButtonWordsClusterSelectionClick()
	{
		System.out.println("WORD CLUSTER SELECTION");
	}

	private void jButtonFinalResultsClick()
	{
		System.out.println("FINAL RESULTS");
	}

	private boolean checkSearchFields()
	{
		if(this.jTextFieldSearchText.getText().equals(""))
		{
			//JOptionPane.showMessageDialog();
			return false;
		}

		return true;
	}

	private void runSearch()
	{
		if(newSearch)
			searchText = newSearchText;
		else
			searchText = this.jTextFieldSearchText.getText();
		StringTokenizer strTok = new StringTokenizer(searchText," ");

		if(strTok.countTokens() > 10)
		{
			JOptionPane.showMessageDialog(null,"System cannot search Google with more than 10 words.","Error connecting to Google",JOptionPane.ERROR_MESSAGE);
			this.jTextFieldSearchText.setText("");
		}
		else
		{
			initialGoogleResults = Integer.parseInt(this.jComboBoxInitialResults.getSelectedItem().toString());
			depthLevel = Integer.parseInt(this.jComboBoxDepthLevel.getSelectedItem().toString());
			algorithm = Integer.parseInt(this.jComboBoxAlgorithm.getSelectedItem().toString());
			words = Integer.parseInt(this.jComboBoxWords.getSelectedItem().toString());
			delta = Double.parseDouble(this.jComboBoxDelta.getSelectedItem().toString());

			if(this.jComboBoxWordsOrientation.getSelectedItem().toString().equals("Sentences"))
				wordsOrientation = 2;

			if(this.jRadioButtonPortugal.isSelected())
				portugal = true;

			Thread progress = new Thread(new Progress());
			progress.start();

			/*initializeInitialResultsTable();
			initializeAgentsTable();
			initializeWordsTable();*/
		}
	}

	private void exit()
	{
		Database.connect("jdbc:mysql://paranhos.fe.up.pt:3306/","ei01043","ax",this.getClass().getResource("").getPath());
		Database.clean();
		Database.disconnect();
		System.exit(0);
	}

	public static void main(String args[])
	{
		new Interface().setVisible(true);
	}

	private class Progress implements Runnable
	{
		public void run()
		{
			progressWindow = new ProgressWindow();
			progressWindow.show();

			progressWindow.changeStatus("Searching Google","Working...",true);

			Thread google = new Thread(new SearchGoogle());
			google.start();
			try{google.join();}	catch(Exception e){}

			if(googleSearched)
			{
				progressWindow.changeStatus("Searching Google","Done!",false);
				try{google.currentThread().sleep(1000);} catch(Exception e){}

				if(!newSearch)
				{
					progressWindow.changeStatus("Searching Links and Sentences","Working...",true);

					Thread agents = new Thread(new Agents());
					timer = new Timer(100,new ActionListener()
					{
						public void actionPerformed(ActionEvent evt){ agentsTime += 0.1; }
					});
					timer.start();
					agents.start();
					try{agents.join();}	catch(Exception e){}
					timer.stop();

					progressWindow.changeStatus("Searching Links and Sentences","Done!",false);
					try{agents.currentThread().sleep(1000);} catch(Exception e){}
					progressWindow.changeStatus("Updating Words","Working...",true);

					Thread wordsUpdate = new Thread(new WordsUpdate());
					timer = new Timer(100,new ActionListener()
					{
						public void actionPerformed(ActionEvent evt){ wordsTime += 0.1; }
					});
					timer.start();
					wordsUpdate.start();
					try{wordsUpdate.join();} catch(Exception e){}
					timer.stop();

					progressWindow.changeStatus("Updating Words","Done!",false);
					try{wordsUpdate.currentThread().sleep(1000);} catch(Exception e){}

					if(!wordsAvailable)
						JOptionPane.showMessageDialog(null,"System can't find valid sentences in all pages.","Error finding sentences",JOptionPane.ERROR_MESSAGE);
					else
					{
						progressWindow.changeStatus("Aplying Algorithm","Working...",true);

						Thread algorithm = new Thread(new Algorithm());
						timer = new Timer(100,new ActionListener()
						{
							public void actionPerformed(ActionEvent evt){ algorithmTime += 0.1; }
						});
						timer.start();
						algorithm.start();
						try{algorithm.join();} catch(Exception e){}
						timer.stop();

						progressWindow.changeStatus("Aplying Algorithm","Done!",false);
						try{algorithm.currentThread().sleep(1000);} catch(Exception e){}

						initializeInitialResultsTable();
						initializeAgentsTable();
						initializeWordsTable();
						initializeWordsClusterSelection();
					}
				}
				else
					initializeFinalResults();
			}

			progressWindow.hide();
		}
	}

	private class SearchGoogle implements Runnable
	{
		public void run()
		{
			if(newSearch)
				initialGoogleResults = 10;

			if(!Google.defineSearch(searchText,initialGoogleResults,portugal))
			{
				googleSearched = false;
				JOptionPane.showMessageDialog(null,"System can't connect to Google this moment. Please check your network connection.","Error connecting to Google",JOptionPane.ERROR_MESSAGE);
			}
			else
				googleSearched = true;

			searchText = jTextFieldSearchText.getText();
		}
	}

	private class Agents implements Runnable
	{
		public void run()
		{
			Database.connect("jdbc:mysql://paranhos.fe.up.pt:3306/","ei01043","ax",classPath);
			String googleResults[] = Google.getSearchResults();
			for(int i = 0; i < googleResults.length; i++)
				Database.insertLinkDatabase(googleResults[i],0);
			Database.disconnect();

			try
			{
				BufferedWriter buf = new BufferedWriter(new FileWriter(path + "agents.bat"));
				String filePath = "java -cp build/classes;build/classes/com/jade/jade.jar;build/classes/com/jade/http.jar; jade.Boot mainAgent:IntelligentWebSearch.MainAgent(" + algorithm + " " + depthLevel + " " + googleResults.length + " " + searchText.replaceAll(" ",",") + " " + wordsOrientation + ")";
				buf.write(filePath,0,filePath.length());
				buf.close();

				Process process = Runtime.getRuntime().exec("cmd.exe /C agents.bat",null,new File(path));
				InputStream processInputStream = process.getInputStream();

				char c = (char) processInputStream.read();	
				while(c != PROCESS_END_CHAR)
				{
					System.out.print(c);
					c = (char) processInputStream.read();
				}

				System.out.println("Agents OK");
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
		}
	}

	private class WordsUpdate implements Runnable
	{
		public void run()
		{
			Database.connect("jdbc:mysql://paranhos.fe.up.pt:3306/","ei01043","ax",classPath);

			try
			{
				BufferedWriter sentences = new BufferedWriter(new FileWriter(classPath + "sentences/" + "sentences.txt"));

				for(int i = 0; i < initialGoogleResults; i++)
				{
					try
					{
						BufferedReader buf = new BufferedReader(new FileReader(classPath + "sentences/" + "searchAgent" + (i + 1) + ".txt"));

						String line = buf.readLine();

						while(line != null)
						{
							if(!line.equals("\n") && !line.equals(""))
								Database.insertWord(line);
							sentences.write(line,0,line.length());

							if(!line.equals("\n"))
								sentences.newLine();

							line = buf.readLine();
						}

						buf.close();
					}
					catch(Exception e2)
					{
						System.out.println(e2.toString());
					}
				}

				sentences.close();
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}

			Database.updateWords();
			int numWords = Database.getNumWords();
			Database.disconnect();

			if(numWords == 0)
				wordsAvailable = false;
			else
				wordsAvailable = true;
		}
	}

	private class Algorithm implements Runnable
	{
		public void run()
		{
			Database.connect("jdbc:mysql://paranhos.fe.up.pt:3306/","ei01043","ax",classPath);
			Words wordsOrganizer = new Words(depthLevel,wordsOrientation,words,delta);
			wordsClusters = wordsOrganizer.getWordsClusters();
			Database.disconnect();
		}
	}
}
