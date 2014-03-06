package src;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Vizu80GUI
{
    /* --------- BEGIN STATIC-FINAL MEMBERS --------- */
    
    /* --------- STATIC-FINAL STRINGS --------- */
    private static final String PROJECT_TITLE = "Vizu-80 - The Visual z80 Learning Emulator";
    private static final String INITIAL_STATUS_MESSAGE = "Hello! Welcome to Vizu-80!";
    private static final String FILE_ACCESSIBILITY_STRING = "File Menu -- Learn about or exit the application";
    private static final String OPTIONS_ACCESSIBILITY_STRING = "Options Menu -- Change options in the application";
    private static final String ANIM_STATUS_MESSAGE = "Animation Status:    ";
    private static final String CPU_STATUS_MESSAGE = "CPU Status:    ";
    private static final String ANIMATION_TITLE = "CPU Animation:";
    private static final String OPCODE_TITLE = "Opcode Interpreter:";
    private static final String INTERNALS_TITLE = "CPU Internal State:";
    private static final String MEMORY_TITLE = "System RAM State:";
    
    /* --------- STATIC-FINAL INTEGERS --------- */
    private static final int CONTENT_WIDTH = 1280;  // Pixels
    private static final int CONTENT_HEIGHT = 768;  // Pixels
    private static final int BAR_HEIGHT = 22; // Pixels
    private static final int STATUS_PADDING = 30; // Pixels
    private static final int UPDATE_SPEED = 5000; // Milliseconds ... 5 Seconds
    private static final int FRAME_MARGIN = 10; // Pixels
    private static final int PANEL_WIDTH = CONTENT_WIDTH / 2; // Pixels
    private static final int PANEL_HEIGHT = (CONTENT_HEIGHT / 4) * 3; // Pixels
    private static final int SUB_PANEL_HEIGHT = (CONTENT_HEIGHT / 4); // Pixels
    
    /* --------- STATIC-FINAL COLORS --------- */
    private static final Color COLOR_MENU_BAR = new Color(242, 238, 230);
    private static final Color COLOR_FRAME_BORDER = new Color(180, 180, 180);
    private static final Color COLOR_CONTENT_BACKGROUND = new Color(232, 228, 220);
    private static final Color COLOR_PANEL_BACKGROUND = new Color(232, 228, 220);
    
    private static final Font TITLE_FONT = new Font("Tahoma", Font.PLAIN, 16);  
    
    /* --------- END STATIC-FINAL MEMBERS --------- */
    
    
    /* --------- BEGIN EXTRA IMPORTS --------- */
    
    // Timer to update CPU Status & Animation Status Texts (all JLabels...?)
    private static Timer updateTimer;
    
    /* --------- END EXTRA IMPORTS --------- */
    
    /* --------- BEGIN GUI COMPONENTS --------- */
    // The main - "High Level" - container, holds all other components
    // Top of the GUI Scene Graph / Tree
    private static JFrame mainFrame;
    
    // JMenu, first component inserted into the frame
    // Within the 1st layer of the Scene Graph
    private static BackgroundMenuBar menuBar;
    private static JMenu fileMenu, optionsMenu;
    private static JMenuItem aboutItem, exitItem, settingsItem;
    
    // JStatusBar, second component added into the frame
    // Also within the 1st layer of the Scene Graph
    private static JStatusBar statusBar;
    private static JLabel messageLabel, animationStatusLabel,cpuStatusLabel;
    
    // Main Content Container - JPanel ... Will display all controls and GUI components
    // Final component of the 1st layer of the Scene Graph
    private static JPanel contentPanel;
    
    // CPU / Animation Status Panel - Panel of the GUI, shows current status of CPU components / Animation
    // 2nd layer of the Scene Graph - Under contentPanel
    private static JPanel cpuPanel, animPanel, cpuPanelExtra, animPanelExtra;
    
    // Title Labels for each of the Status Panels and Sub Panels.
    // Within the 3rd layer of the Scene Graph - One into the respective containers
    private static JLabel cpuTitle, animTitle, cpuExtraTitle, animExtraTitle;
    
    /* --------- END GUI COMPONENTS --------- */
    
    public static void main(String[] args)
    {
        try
        {  
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
               createAndShowGUI();
            }
        });
    }
    
    private static void createAndShowGUI()
    {
        JFrame.setDefaultLookAndFeelDecorated(true);
        mainFrame = new JFrame(PROJECT_TITLE);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //mainFrame.setBorder(new LineBorder(COLOR_FRAME_BORDER));
        
        ActionListener updateLabels = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                animationStatusLabel.setText(ANIM_STATUS_MESSAGE + "Animation Status Ok!");
                cpuStatusLabel.setText(CPU_STATUS_MESSAGE + "CPU/Emulation Status Ok!");
            }
        };
        
        updateTimer = new Timer(UPDATE_SPEED, updateLabels);
        updateTimer.start();
        
        addMenuComponents(mainFrame);
        addStatusBarComponents(mainFrame);
        addContentPanel(mainFrame);
        addPanelTitles();
        
        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
    
    private static void addMenuComponents(JFrame theFrame)
    {
        menuBar = new BackgroundMenuBar();
        menuBar.setColor(COLOR_MENU_BAR);
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(theFrame.getWidth(), BAR_HEIGHT));
        
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getAccessibleContext().setAccessibleDescription(FILE_ACCESSIBILITY_STRING);
        fileMenu.setBackground(COLOR_MENU_BAR);
        
        aboutItem = new JMenuItem("About");
        aboutItem.setBackground(COLOR_MENU_BAR);
        aboutItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                
            }
        });
        
        exitItem = new JMenuItem("Exit");
        exitItem.setBackground(COLOR_MENU_BAR);
        exitItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int picked = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Are you sure?",
                                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                                                
                if(picked == 0)
                    shutdown();
            }
        });
        
        fileMenu.add(aboutItem);
        fileMenu.add(exitItem);
        
        optionsMenu = new JMenu("Options");
        optionsMenu.setBackground(COLOR_MENU_BAR);
        
        settingsItem = new JMenuItem("Settings"); 
        settingsItem.setBackground(COLOR_MENU_BAR);
        
        optionsMenu.add(settingsItem);
               
        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);
        
        theFrame.setJMenuBar(menuBar);
    }
    
    private static void addStatusBarComponents(JFrame theFrame)
    {
        statusBar = new JStatusBar(BAR_HEIGHT, COLOR_MENU_BAR);
        
        messageLabel = new JLabel(INITIAL_STATUS_MESSAGE); 
        messageLabel.setBorder(new EmptyBorder(2, 0, 0, 0));
        statusBar.setLeftComponent(messageLabel);
        
        animationStatusLabel = new JLabel(ANIM_STATUS_MESSAGE);
        animationStatusLabel.setHorizontalAlignment(JLabel.LEFT);
        animationStatusLabel.setBorder(new EmptyBorder(2, 0, 0, STATUS_PADDING));
        statusBar.addRightComponent(animationStatusLabel);
        
        cpuStatusLabel = new JLabel(CPU_STATUS_MESSAGE);
        cpuStatusLabel.setHorizontalAlignment(JLabel.CENTER);
        cpuStatusLabel.setBorder(new EmptyBorder(2, 0, 0, STATUS_PADDING));
        statusBar.addRightComponent(cpuStatusLabel);
        
        theFrame.add(statusBar, BorderLayout.SOUTH);
    }
    
    private static void addContentPanel(JFrame theFrame)
    {
        contentPanel = new JPanel();
        contentPanel.setOpaque(true);
        contentPanel.setBackground(COLOR_CONTENT_BACKGROUND);
        contentPanel.setPreferredSize(new Dimension(CONTENT_WIDTH, CONTENT_HEIGHT));
        contentPanel.setBorder(new EmptyBorder(FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN));
        
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        addSplitPanels(contentPanel, con);
        
        theFrame.add(contentPanel, BorderLayout.CENTER);
    }
    
    private static void addSplitPanels(JPanel thePanel, GridBagConstraints c)
    {
        animPanel = new JPanel();
        animTitle = new JLabel(ANIMATION_TITLE);        
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1;
        c.weighty = 0.1;
        c.anchor = GridBagConstraints.PAGE_START;
        c.ipadx = PANEL_WIDTH;
        c.ipady = PANEL_HEIGHT;      
        c.insets = new Insets(5, 5, 5, 5);        
        thePanel.add(animPanel, c);
        
        cpuPanel = new JPanel();
        cpuTitle = new JLabel(INTERNALS_TITLE);        
        c.gridx = 1;
        c.gridy = 0;
        thePanel.add(cpuPanel, c);
        
        animPanelExtra = new JPanel();
        animExtraTitle = new JLabel(OPCODE_TITLE);        
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = SUB_PANEL_HEIGHT;               
        thePanel.add(animPanelExtra, c);
        
        cpuPanelExtra = new JPanel();
        cpuExtraTitle = new JLabel(MEMORY_TITLE);        
        c.gridx = 1;
        c.gridy = 1;        
        thePanel.add(cpuPanelExtra, c);        
    }
    
    private static void addPanelTitles()
    {             
        insertTitle(animPanel, animTitle);  
        insertTitle(cpuPanel, cpuTitle);
        insertTitle(animPanelExtra, animExtraTitle);
        insertTitle(cpuPanelExtra, cpuExtraTitle);
    }
    
    private static void insertTitle(JPanel thePanel, JLabel theTitle)
    {
        thePanel.setLayout(new BorderLayout());
        thePanel.setBackground(COLOR_PANEL_BACKGROUND);
        thePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        
        
        theTitle.setFont(TITLE_FONT);
        theTitle.setHorizontalAlignment(JLabel.CENTER);        
        
        thePanel.setOpaque(true);
        thePanel.add(theTitle, BorderLayout.NORTH);

    }
    
    private static void shutdown()
    {
        mainFrame.dispose();
        System.exit(0);
    }
}