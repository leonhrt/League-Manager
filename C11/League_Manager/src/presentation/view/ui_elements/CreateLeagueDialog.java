package presentation.view.ui_elements;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Class that creates the panel that allows the user create a league
 */
public class CreateLeagueDialog extends JDialog {

    // Attributes
    private  JTextField jtfName;
    private JTextField jtfStartHour;
    private JTextField jtfStartDate;
    private JLabel jlError;
    private JButton jbNext;
    private JButton jbCancel;
    private JButton jbUploadPhoto;
    private JFileChooser jFileChooser;
    private String image_path;

    // Constants
    public static final String NEXT_PRESSED = "NEXT_PRESSED";
    public static final String CANCEL_PRESSED = "CANCEL_PRESSED";
    public static final String UPLOAD_PHOTO_PRESSED = "UPLOAD_PHOTO_PRESSED";


    /**
     * Constructor method
     * Creates the panel with the buttons to move across the different options
     * @param mainView Frame that represents the main view of the application
     */
    public CreateLeagueDialog(Frame mainView){
        super(mainView);
        setModal(true);
        setTitle("Fill the form: ");
        setSize(700,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        createTitle();
        createForm();
        createButtons();
        createFileChooser();
        addTextListeners();

    }

    /**
     * Method that creates the title of the panel
     */
    private void createTitle() {
        JPanel jpNorth = new JPanel();
        jpNorth.setLayout(new BorderLayout());
        jpNorth.setBorder(BorderFactory.createEmptyBorder(0,260,0,10));
        JLabel jlTitle = new JLabel("CREATE A NEW LEAGUE");
        ImageIcon iiUpload = new ImageIcon("resources/general/upload.png");

        jbUploadPhoto= new JButton(iiUpload);
        jbUploadPhoto.setOpaque(false);
        jbUploadPhoto.setContentAreaFilled(false);
        jbUploadPhoto.setBorderPainted(false);
        jbUploadPhoto.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        jlTitle.setFont(new Font("Arial", Font.BOLD, 15));
        jlTitle.setForeground(Color.orange);
        jpNorth.add(jlTitle,BorderLayout.CENTER);
        jpNorth.add(jbUploadPhoto,BorderLayout.EAST);

        jbUploadPhoto.setActionCommand(UPLOAD_PHOTO_PRESSED);

        add(jpNorth, BorderLayout.NORTH);
    }

    /**
     * Method that creates the form to fill
     */
    private void createForm() {
        JPanel jpForm = new JPanel();
        jpForm.setBorder(BorderFactory.createEmptyBorder(20,10,10,20));
        jpForm.setLayout(new BoxLayout(jpForm, BoxLayout.Y_AXIS));

        JPanel jpName = new JPanel();
        jpName.setLayout(new BorderLayout());
        jpName.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        JLabel jlName = new JLabel("Name:");
        jlName.setBorder(BorderFactory.createEmptyBorder(0,0,0,46));
        jpName.add(jlName,BorderLayout.WEST);
        jtfName = new JTextField();
        jpName.add(jtfName,BorderLayout.CENTER);

        JPanel jpStartDate = new JPanel();
        jpStartDate.setLayout(new BorderLayout());
        jpStartDate.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        JLabel jlStartDate = new JLabel("Start date:");
        jlStartDate.setBorder(BorderFactory.createEmptyBorder(0,0,0,23));
        jpStartDate.add(jlStartDate,BorderLayout.WEST);
        jtfStartDate = new JTextField();
        jpStartDate.add(jtfStartDate,BorderLayout.CENTER);

        JPanel jpEndDate = new JPanel();
        jpEndDate.setLayout(new BorderLayout());
        jpEndDate.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        JLabel jlEndDate = new JLabel("Start hour:");
        jlEndDate.setBorder(BorderFactory.createEmptyBorder(0,0,0,27));
        jpEndDate.add(jlEndDate,BorderLayout.WEST);
        jtfStartHour = new JTextField();
        jpEndDate.add(jtfStartHour,BorderLayout.CENTER);

        jpForm.add(jpName);
        jpForm.add(Box.createRigidArea(new Dimension(0,15)));
        jpForm.add(jpStartDate);
        jpForm.add(Box.createRigidArea(new Dimension(0,15)));
        jpForm.add(jpEndDate);

        add(jpForm, BorderLayout.CENTER);
    }

    /**
     * Method that creates the buttons to move across the different options
     */
    private void createButtons() {
        JPanel jpSouth = new JPanel();
        jpSouth.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        jpSouth.setLayout(new BoxLayout(jpSouth,BoxLayout.X_AXIS));

        jlError = new JLabel();
        jlError.setForeground(Color.RED);

        jbNext = new JButton("Next");
        jbNext.setBackground(Color.decode("#E7853A"));
        jbNext.setForeground(Color.WHITE);
        jbNext.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        jbCancel = new JButton("Cancel");
        jbCancel.setBackground(Color.BLUE);
        jbCancel.setForeground(Color.WHITE);
        jbCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        jpSouth.add(jlError);
        jpSouth.add(Box.createHorizontalGlue());
        jpSouth.add(jbNext);

        jpSouth.add(Box.createRigidArea(new Dimension(20,0)));
        jpSouth.add(jbCancel);

        jbNext.setActionCommand(NEXT_PRESSED);
        jbCancel.setActionCommand(CANCEL_PRESSED);
        add(jpSouth, BorderLayout.SOUTH);
    }

    /**
     * Method that creates the file chooser to upload the photo
     */
    private void createFileChooser() {
        jFileChooser = new JFileChooser();
        //Eliminem el filtre general i creem un filtre pels fitxer .json
        jFileChooser.removeChoosableFileFilter(jFileChooser.getFileFilter());
        jFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("image", "png","jpg","jpeg"));
    }

    /**
     * Method that sets the visibility of the panel
     */
    public void setVisible(){
        setVisible(true);
    }

    /**
     * Method that registers the controller to the buttons
     * @param l controller
     */
    public void registerController(ActionListener l){
        jbNext.addActionListener(l);
        jbCancel.addActionListener(l);
        jbUploadPhoto.addActionListener(l);
    }

    /**
     * Method that add the listeners to the text fields
     */
    private void addTextListeners() {
        jtfStartDate.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (e.getDocument().getLength() == 2 || e.getDocument().getLength() == 5) {
                        try {
                            jtfStartDate.getDocument().insertString(jtfStartDate.getCaretPosition(), "/", null);
                        } catch (BadLocationException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }

            @Override
            public void removeUpdate(DocumentEvent e) { }

            @Override
            public void changedUpdate(DocumentEvent e) { }
        });

        jtfStartHour.getDocument().addDocumentListener(new DocumentListener() {


            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if(e.getDocument().getLength() == 2 ){
                        try {
                            jtfStartHour.getDocument().insertString(jtfStartHour.getCaretPosition(), ":", null);
                        } catch (BadLocationException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }

            @Override
            public void removeUpdate(DocumentEvent e) { }

            @Override
            public void changedUpdate(DocumentEvent e) { }
        });

    }

    /**
     * Method that allows to select a file
     * @return the option selected
     */
    public int selectFile() {
        return jFileChooser.showOpenDialog(this);
    }

    /**
     * Method that returns the file selected
     * @return the file selected
     */
    public File getFile() {
        return jFileChooser.getSelectedFile();
    }

    /**
     * Method that returns the start date
     * @return the start date
     */
    public String getStartDate(){
        return jtfStartDate.getText();
    }

    /**
     * Method that returns the start hour
     * @return the start hour
     */
    public String getStartHour(){
        return jtfStartHour.getText();
    }

    /**
     * Method that returns the name of the league
     * @return the name of the league
     */
    public String getLeagueName(){
        return jtfName.getText();
    }

    /**
     * Method that resets the text fields
     */
    public void resetTextFields(){
        jtfName.setText("");
        jtfStartDate.setText("");
        jtfStartHour.setText("");
        jlError.setVisible(true);
        jlError.setText("");
        image_path = null;
    }

    /**
     * Method that sets the error message
     * @param error error message
     */
    public void setError(String error){
        jlError.setText(error);
    }

/**
     * Method that sets the image path
     * @param path image path
     */
    public void setImage(String path) {
        image_path = path;
    }

    /**
     * Method that returns the image path
     * @return the image path
     */
    public String getImagePath(){
        return image_path;
    }


    /**
     * Method that checks if all the fields are filled
     * @return true if all the fields are filled, false otherwise
     */
    public boolean notAllFilled(){
        return jtfName.getText().length() == 0 || jtfStartHour.getText().length() == 0 || jtfStartDate.getText().length() == 0;
    }
}
