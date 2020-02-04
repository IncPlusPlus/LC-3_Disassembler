import java.awt.GridBagConstraints;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class LC3GUI implements java.awt.event.ActionListener, javax.swing.event.TableModelListener
{
  private final Machine mac;
  public static String LOOKANDFEEL = "Metal";
  
  private final JFrame frame = new JFrame("LC3 Simulator");
  
  private final javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser(".");
  private final javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
  private final javax.swing.JMenu fileMenu = new javax.swing.JMenu("File");
  private final javax.swing.JMenu aboutMenu = new javax.swing.JMenu("About");
  private final JMenuItem openItem = new JMenuItem("Open .obj File");
  private final JMenuItem quitItem = new JMenuItem("Quit");
  private final JMenuItem commandItem = new JMenuItem("Open Command Output Window");
  private final JMenuItem versionItem = new JMenuItem("Simulator Version");
  private final String openActionCommand = "Open";
  private final String quitActionCommand = "Quit";
  private final String openCOWActionCommand = "OutputWindow";
  private final String versionActionCommand = "Version";
  private final JPanel leftPanel = new JPanel();
  

  private final JPanel controlPanel = new JPanel();
  
  private final JButton nextButton = new JButton("Next");
  private final String nextButtonCommand = "Next";
  private final JButton stepButton = new JButton("Step");
  private final String stepButtonCommand = "Step";
  private final JButton continueButton = new JButton("Continue");
  private final String continueButtonCommand = "Continue";
  private final JButton stopButton = new JButton("Stop");
  private final String stopButtonCommand = "Stop";
  private final String statusLabelRunning = "    Running ";
  private final String statusLabelSuspended = "Suspended ";
  private final String statusLabelHalted = "       Halted ";
  private final javax.swing.JLabel statusLabel = new javax.swing.JLabel("");
  private final java.awt.Color runningColor = new java.awt.Color(43, 129, 51);
  private final java.awt.Color suspendedColor = new java.awt.Color(209, 205, 93);
  private final java.awt.Color haltedColor = new java.awt.Color(161, 37, 40);
  
  private final JTable regTable;
  
  private final CommandLinePanel commandPanel;
  
  private final CommandOutputWindow commandOutputWindow;
  
  private final JPanel memoryPanel = new JPanel(new java.awt.BorderLayout());
  

  private final JTable memTable;
  
  private final javax.swing.JScrollPane memScrollPane;
  
  private final JPanel devicePanel = new JPanel();
  private final JPanel registerPanel = new JPanel();
  private final TextConsolePanel ioPanel;
  private final VideoConsole video;
  
  private void setupMemoryPanel()
  {
    memoryPanel.add(memScrollPane, "Center");
    
    memoryPanel.setMinimumSize(new java.awt.Dimension(400, 100));
    memoryPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Memory"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    
    memTable.getModel().addTableModelListener(this);
    memTable.getModel().addTableModelListener(video);
    
    memTable.setPreferredScrollableViewportSize(new java.awt.Dimension(400, 460));
  }
  
  private void setupControlPanel() {
    int i = 4;
    controlPanel.setLayout(new java.awt.GridBagLayout());
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    fill = 2;
    
    nextButton.setActionCommand("Next");
    nextButton.addActionListener(this);
    weightx = 1.0D;
    gridx = 0;
    gridy = 0;
    controlPanel.add(nextButton, localGridBagConstraints);
    
    stepButton.setActionCommand("Step");
    stepButton.addActionListener(this);
    gridx = 1;
    gridy = 0;
    controlPanel.add(stepButton, localGridBagConstraints);
    
    continueButton.setActionCommand("Continue");
    continueButton.addActionListener(this);
    gridx = 2;
    gridy = 0;
    controlPanel.add(continueButton, localGridBagConstraints);
    
    stopButton.setActionCommand("Stop");
    stopButton.addActionListener(this);
    gridx = 3;
    gridy = 0;
    controlPanel.add(stopButton, localGridBagConstraints);
    









    gridx = 4;
    gridy = 0;
    fill = 0;
    anchor = 22;
    setStatusLabelSuspended();
    controlPanel.add(statusLabel, localGridBagConstraints);
    

    localGridBagConstraints = new GridBagConstraints();
    gridx = 0;
    gridy = 1;
    gridwidth = 6;
    controlPanel.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(5, 5)), localGridBagConstraints);
    
    localGridBagConstraints = new GridBagConstraints();
    gridx = 0;
    gridy = 2;
    gridwidth = 6;
    gridheight = 1;
    ipady = 100;
    weightx = 1.0D;
    weighty = 1.0D;
    fill = 1;
    controlPanel.add(commandPanel, localGridBagConstraints);
    
    controlPanel.setMinimumSize(new java.awt.Dimension(100, 150));
    controlPanel.setPreferredSize(new java.awt.Dimension(100, 150));
    controlPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Controls"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    
    controlPanel.setVisible(true);
  }
  
  private void setupRegisterPanel() {
    registerPanel.setLayout(new java.awt.GridBagLayout());
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    gridx = 0;
    gridy = 0;
    weightx = 1.0D;
    fill = 2;
    registerPanel.add(regTable, localGridBagConstraints);
    
    registerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Registers"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    
    registerPanel.setVisible(true);
  }
  
  private void setupDevicePanel()
  {
    devicePanel.setLayout(new java.awt.GridBagLayout());
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    fill = 10;
    
    gridx = 0;
    gridy = 0;
    weightx = 1.0D;
    devicePanel.add(video, localGridBagConstraints);
    
    localGridBagConstraints = new GridBagConstraints();
    gridx = 0;
    gridy = 1;
    weightx = 1.0D;
    fill = 0;
    devicePanel.add(ioPanel, localGridBagConstraints);
    devicePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Devices"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    
    devicePanel.setVisible(true);
  }
  
  public LC3GUI(Machine paramMachine) {
    mac = paramMachine;
    RegisterFile localRegisterFile = paramMachine.getRegisterFile();
    regTable = new JTable(localRegisterFile);
    TableColumn localTableColumn = regTable.getColumnModel().getColumn(0);
    localTableColumn.setMaxWidth(30);
    localTableColumn.setMinWidth(30);
    localTableColumn = regTable.getColumnModel().getColumn(2);
    localTableColumn.setMaxWidth(30);
    localTableColumn.setMinWidth(30);
    Memory localMemory = paramMachine.getMemory();
    memTable = new JTable(localMemory) {
      private static final long serialVersionUID = 100L;
      private final Machine val$mac;
      
      public java.awt.Component prepareRenderer(javax.swing.table.TableCellRenderer paramAnonymousTableCellRenderer, int paramAnonymousInt1, int paramAnonymousInt2) { java.awt.Component localComponent = super.prepareRenderer(paramAnonymousTableCellRenderer, paramAnonymousInt1, paramAnonymousInt2);
        
        if (paramAnonymousInt2 == 0) {
          javax.swing.JCheckBox localJCheckBox = new javax.swing.JCheckBox();
          if (paramAnonymousInt1 < 65024) {
            if (mac.isBreakPoint(paramAnonymousInt1)) {
              localJCheckBox.setSelected(true);
              localJCheckBox.setBackground(java.awt.Color.red);
              localJCheckBox.setForeground(java.awt.Color.red);
            } else {
              localJCheckBox.setSelected(false);
              localJCheckBox.setBackground(getBackground());
            }
          } else {
            localJCheckBox.setEnabled(false);
            localJCheckBox.setBackground(java.awt.Color.lightGray);
          }
          return localJCheckBox;
        }
        if (paramAnonymousInt1 == mac.getPC()) {
          localComponent.setBackground(java.awt.Color.yellow);
        }
        else {
          localComponent.setBackground(getBackground());
        }
        return localComponent;
      }
      




      public void tableChanged(javax.swing.event.TableModelEvent paramAnonymousTableModelEvent)
      {
        try
        {
          if (!val$mac.isContinueMode()) {
            super.tableChanged(paramAnonymousTableModelEvent);
          }
        } catch (NullPointerException localNullPointerException) {
          super.tableChanged(paramAnonymousTableModelEvent);
        }
        
      }
    };
    memScrollPane = new javax.swing.JScrollPane(memTable);
    localTableColumn = memTable.getColumnModel().getColumn(0);
    localTableColumn.setMaxWidth(20);
    localTableColumn.setMinWidth(20);
    localTableColumn.setCellEditor(new javax.swing.DefaultCellEditor(new javax.swing.JCheckBox()));
    localTableColumn = memTable.getColumnModel().getColumn(2);
    localTableColumn.setMinWidth(50);
    localTableColumn.setMaxWidth(50);
    
    commandPanel = new CommandLinePanel(paramMachine);
    
    commandOutputWindow = new CommandOutputWindow("Command Output");
    java.awt.event.WindowListener local2 = new java.awt.event.WindowListener() { public void windowActivated(WindowEvent paramAnonymousWindowEvent) {}
      
      public void windowClosed(WindowEvent paramAnonymousWindowEvent) {}
      
      public void windowClosing(WindowEvent paramAnonymousWindowEvent) { commandOutputWindow.setVisible(false); }
      
      public void windowDeactivated(WindowEvent paramAnonymousWindowEvent) {}
      
      public void windowDeiconified(WindowEvent paramAnonymousWindowEvent) {}
      public void windowIconified(WindowEvent paramAnonymousWindowEvent) {}
      public void windowOpened(WindowEvent paramAnonymousWindowEvent) {} };
    commandOutputWindow.addWindowListener(local2);
    commandOutputWindow.setSize(700, 600);
    
    ioPanel = new TextConsolePanel(paramMachine.getKeyBoardDevice(), paramMachine.getMonitorDevice());
    ioPanel.setMinimumSize(new java.awt.Dimension(256, 85));
    video = new VideoConsole(paramMachine);
    commandPanel.setGUI(this);
  }
  
  public void setUpGUI()
  {
    initLookAndFeel();
    
    JFrame.setDefaultLookAndFeelDecorated(true);
    






















    mac.setStoppedListener(commandPanel);
    
    fileChooser.setFileSelectionMode(2);
    fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
      public boolean accept(java.io.File paramAnonymousFile) {
        if (paramAnonymousFile.isDirectory()) return true;
        String str = paramAnonymousFile.getName();
        if ((str != null) && (str.toLowerCase().endsWith(".obj"))) {
          return true;
        }
        return false;
      }
      
      public String getDescription() { return "*.obj";
      }
    });
    openItem.setActionCommand("Open");
    openItem.addActionListener(this);
    fileMenu.add(openItem);
    commandItem.setActionCommand("OutputWindow");
    commandItem.addActionListener(this);
    fileMenu.add(commandItem);
    fileMenu.addSeparator();
    quitItem.setActionCommand("Quit");
    quitItem.addActionListener(this);
    fileMenu.add(quitItem);
    
    versionItem.setActionCommand("Version");
    versionItem.addActionListener(this);
    aboutMenu.add(versionItem);
    
    menuBar.add(fileMenu);
    menuBar.add(aboutMenu);
    frame.setJMenuBar(menuBar);
    
    setupControlPanel();
    setupRegisterPanel();
    setupDevicePanel();
    setupMemoryPanel();
    
    regTable.getModel().addTableModelListener(this);
    






    frame.getContentPane().setLayout(new java.awt.GridBagLayout());
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    
    fill = 1;
    gridx = 0;
    gridy = 0;
    gridwidth = 2;
    weighty = 1.0D;
    
    gridwidth = 0;
    frame.getContentPane().add(controlPanel, localGridBagConstraints);
    
    localGridBagConstraints = new GridBagConstraints();
    gridx = 0;
    gridy = 1;
    
    gridwidth = 1;
    gridheight = 1;
    weightx = 0.0D;
    fill = 2;
    frame.getContentPane().add(registerPanel, localGridBagConstraints);
    
    localGridBagConstraints = new GridBagConstraints();
    gridx = 0;
    gridy = 2;
    weightx = 0.0D;
    
    gridheight = 1;
    gridwidth = 1;
    
    fill = 1;
    frame.getContentPane().add(devicePanel, localGridBagConstraints);
    
    localGridBagConstraints = new GridBagConstraints();
    gridx = 1;
    gridy = 1;
    

    gridheight = 2;
    gridwidth = 0;
    fill = 1;
    weightx = 1.0D;
    frame.getContentPane().add(memoryPanel, localGridBagConstraints);
    
    frame.setSize(new java.awt.Dimension(700, 725));
    frame.setDefaultCloseOperation(3);
    frame.pack();
    frame.setVisible(true);
    
    scrollToPC();
  }
  
  public void clearCommandLine()
  {
    commandPanel.clear();
  }
  
  public void scrollToIndex(int paramInt) {
    memTable.scrollRectToVisible(memTable.getCellRect(paramInt, 0, true));
  }
  
  public void scrollToPC() {
    scrollToPC(0);
  }
  
  public void scrollToPC(int paramInt) {
    int i = mac.getRegisterFile().getPC().getValue() + paramInt;
    memTable.scrollRectToVisible(memTable.getCellRect(i, 0, true));
  }
  
  public void tableChanged(javax.swing.event.TableModelEvent paramTableModelEvent) {
    if (!mac.isContinueMode()) {}
  }
  




  public void confirmExit()
  {
    Object[] arrayOfObject = { "Yes", "No" };
    int i = javax.swing.JOptionPane.showOptionDialog(frame, "Are you sure you want to quit?", "Quit verification", 0, 3, null, arrayOfObject, arrayOfObject[1]);
    





    if (i == 0) {
      ErrorLog.logClose();
      
      System.exit(0);
    }
  }
  



  public void actionPerformed(java.awt.event.ActionEvent paramActionEvent)
  {
    try
    {
      if ("Next".equals(paramActionEvent.getActionCommand())) {
        mac.executeNext();
      } else if ("Step".equals(paramActionEvent.getActionCommand())) {
        mac.executeStep();
      } else if ("Continue".equals(paramActionEvent.getActionCommand())) {
        mac.executeMany();
      } else if ("Quit".equals(paramActionEvent.getActionCommand())) {
        confirmExit();
      } else if ("Stop".equals(paramActionEvent.getActionCommand())) {
        CommandLinePanel.writeToConsole(mac.stopExecution(true));
      } else if ("OutputWindow".equals(paramActionEvent.getActionCommand())) {
        commandOutputWindow.setVisible(true);
      } else if ("Version".equals(paramActionEvent.getActionCommand())) {
        javax.swing.JOptionPane.showMessageDialog(frame, LC3Simulator.getVersion(), "Version", 1);
      } else if ("Open".equals(paramActionEvent.getActionCommand())) {
        int i = fileChooser.showOpenDialog(frame);
        if (i == 0) {
          java.io.File localFile = fileChooser.getSelectedFile();
          CommandLinePanel.writeToConsole(mac.loadObjectFile(localFile));
        } else {
          System.out.println("Open command cancelled by user.");
        }
      }
    } catch (ExceptionException localExceptionException) {
      localExceptionException.showMessageDialog(frame);
    }
  }
  
  public static void initLookAndFeel() {
    String str = null;
    JFrame.setDefaultLookAndFeelDecorated(true);
    if (LOOKANDFEEL != null) {
      if (LOOKANDFEEL.equals("Metal")) {
        str = javax.swing.UIManager.getCrossPlatformLookAndFeelClassName();
      } else if (LOOKANDFEEL.equals("System")) {
        str = javax.swing.UIManager.getSystemLookAndFeelClassName();
      } else if (LOOKANDFEEL.equals("Motif")) {
        str = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
      } else if (LOOKANDFEEL.equals("GTK+")) {
        str = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
      } else {
        ErrorLog.logError("Unexpected value of LOOKANDFEEL specified: " + LOOKANDFEEL);
        
        str = javax.swing.UIManager.getCrossPlatformLookAndFeelClassName();
      }
      try
      {
        javax.swing.UIManager.setLookAndFeel(str);
      } catch (ClassNotFoundException localClassNotFoundException) {
        ErrorLog.logError("Couldn't find class for specified look and feel:" + str);
        
        ErrorLog.logError("Did you include the L&F library in the class path?");
        ErrorLog.logError("Using the default look and feel.");
      } catch (javax.swing.UnsupportedLookAndFeelException localUnsupportedLookAndFeelException) {
        ErrorLog.logError("Can't use the specified look and feel (" + str + ") on this platform.");
        

        ErrorLog.logError("Using the default look and feel.");
      } catch (Exception localException) {
        ErrorLog.logError("Couldn't get specified look and feel (" + str + "), for some reason.");
        

        ErrorLog.logError("Using the default look and feel.");
        ErrorLog.logError(localException);
      }
    }
  }
  


  public JFrame getFrame()
  {
    return frame;
  }
  



  public void setStatusLabelRunning()
  {
    statusLabel.setText("    Running ");
    statusLabel.setForeground(runningColor);
  }
  

  public void setStatusLabelSuspended()
  {
    statusLabel.setText("Suspended ");
    statusLabel.setForeground(suspendedColor);
  }
  

  public void setStatusLabelHalted()
  {
    statusLabel.setText("       Halted ");
    statusLabel.setForeground(haltedColor);
  }
  


  public void setStatusLabel(boolean paramBoolean)
  {
    if (paramBoolean) {
      setStatusLabelSuspended();
    } else {
      setStatusLabelHalted();
    }
  }
  
  public void setTextConsoleEnabled(boolean paramBoolean) {
    ioPanel.setEnabled(paramBoolean);
  }
}
