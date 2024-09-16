package presentation.view.ui_elements;

import business.model.entities.Player;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Class that creates the panel that allows the user move across the different options
 */
public class FileDialog extends JDialog {

    // Constants
    public static final String UPLOAD_FILE_PRESSED = "UPLOAD_FILE_PRESSED";
    public static final String CANCEL_FILE_PRESSED = "CANCEL_FILE_PRESSED";
    public static final String SHOW_PLAYERS = "SHOW_PLAYERS";
    public static final String ASK_FILE = "ASK_FILE";

    // Components
    private  JLabel jlError;
    private JButton jbUpload;
    private JButton jbCancel;
    private CardLayout cardLayout;
    private JPanel jpCardLayout;
    private JPanel jpAskFile;
    private JPanel jpShowPlayers;
    private JPanel jpPlayers;
    private JFileChooser jFileChooser;

    /**
     * Constructor method
     * @param mainView Frame that represents the main view of the application
     */
    public FileDialog(Frame mainView){
        super(mainView);
        setModal(true);
        cardLayout = new CardLayout();
        jpCardLayout = new JPanel(cardLayout);

        setTitle("Choose teams file");
        setSize(400,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createAskFilePanel();
        createScrollPaneNewPlayers();
        createFileChooser();

        jpCardLayout.add(jpAskFile, ASK_FILE);
        jpCardLayout.add(jpShowPlayers,SHOW_PLAYERS);
        add(jpCardLayout);


        setActionCommands();
    }

    /**
     * Method that creates the file chooser to upload the file
     */
    private void createFileChooser() {
        jFileChooser = new JFileChooser();
        //Eliminem el filtre general i creem un filtre pels fitxer .json
        jFileChooser.removeChoosableFileFilter(jFileChooser.getFileFilter());
        jFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("json", "json"));
    }

    /**
     * Method that creates a scroll pane to show the new players
     */
    private void createScrollPaneNewPlayers() {
        jpShowPlayers = new JPanel();
        jpShowPlayers.setLayout(new BorderLayout());
        jpShowPlayers.setBackground(Color.MAGENTA);

        jpShowPlayers.add(new JLabel("New user accounts have been created: "), BorderLayout.NORTH);

        jpPlayers = new JPanel();
        jpPlayers.setLayout(new BoxLayout(jpPlayers, BoxLayout.Y_AXIS));
        jpPlayers.setBorder(BorderFactory.createEmptyBorder(15,0,0,0));
        JScrollPane jspPlayers = new JScrollPane(jpPlayers, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        jspPlayers.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
        jpShowPlayers.add(jspPlayers);
    }

    /**
     * Method that creates the panel that asks the user to upload the file
     */
    private void createAskFilePanel() {
        jpAskFile = new JPanel(new BorderLayout());

        JPanel jpSouth = new JPanel();
        jlError = new JLabel();
        jpSouth.add(jlError, BorderLayout.SOUTH);
        jpSouth.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        JPanel jpCentre = new JPanel();
        jpCentre.setBorder(BorderFactory.createEmptyBorder(120,0,0,0));
        JPanel jpCentreSuperior = new JPanel();
        JPanel jpCentreCentre = new JPanel();

        jpCentre.setOpaque(false);
        jpCentreSuperior.setOpaque(false);
        jpCentreCentre.setOpaque(false);

        JLabel jLabel = new JLabel("To create a new team, please upload the file with the players");
        jpCentreSuperior.add(jLabel);

        jbUpload = new JButton("Upload");
        jbUpload.setForeground(Color.WHITE);
        jbUpload.setBackground(Color.decode("#E7853A"));

        jbCancel= new JButton("Cancel");
        jbCancel.setForeground(Color.WHITE);
        jbCancel.setBackground(Color.decode("#29417C"));

        jpCentreCentre.setLayout(new FlowLayout(FlowLayout.CENTER,20,0));
        jpCentreCentre.add(jbUpload);
        jpCentreCentre.add(jbCancel);

        jpCentre.add(jpCentreSuperior,BorderLayout.NORTH);
        jpCentre.add(jpCentreCentre,BorderLayout.CENTER);

        jpAskFile.add(jpCentre, BorderLayout.CENTER);
        jpAskFile.add(jpSouth,BorderLayout.SOUTH);

    }

    /**
     * Method that sets the action commands of the buttons
     */
    private void setActionCommands() {
        jbUpload.setActionCommand(UPLOAD_FILE_PRESSED);
        jbCancel.setActionCommand(CANCEL_FILE_PRESSED);
    }

    /**
     * Method that registers the action listener of the buttons
     * @param l Action listener
     */
    public void registerController(ActionListener l){
        jbUpload.addActionListener(l);
        jbCancel.addActionListener(l);
    }

    /**
     * Method that shows the dialog
     */
    public void setVisible(){
        jlError.setText("");
        setVisible(true);
    }

    /**
     * Method that allows to set the text of the error label
     * @param text Text to be shown
     * @param isError Boolean that indicates if the text is an error or not
     */
    public void setText(String text, boolean isError) {
        if(!isError){
            jlError.setForeground(Color.decode("#4FEC5DFF"));
        }
        else{
            jlError.setForeground(Color.RED);

        }
        jlError.setText(text);
    }

    /**
     * Method that shows the new players
     * @param new_players List of new players
     */
    public void showNewPlayers(ArrayList<Player> new_players) {
        cardLayout.show(jpCardLayout,SHOW_PLAYERS);

        for (Player new_player : new_players) {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));
            jPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
            jPanel.add(new JLabel("Username: " + new_player.getUsername()));
            jPanel.add(new JLabel("Password: " + new_player.getPassword()));
            jpPlayers.add(jPanel);

        }
    }

    /**
     * Method that shows the file chooser
     * @return int that indicates if the user has selected a file or not
     */
    public int selectFile() {
        return jFileChooser.showOpenDialog(this);
    }

    /**
     * Method that returns the selected file
     * @return File that represents the selected file
     */
    public File getFile() {
        return jFileChooser.getSelectedFile();
    }
}
