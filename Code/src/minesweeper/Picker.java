package minesweeper;

import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.awt.event.ItemEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

public class Picker extends JFrame implements ItemListener {
    private String pickerState;

    private int res;
    private int panelWidth = 620;
    private int panelHeight = 325;

    private JPanel upperPanel;
    private JPanel lowerPanel;

    private JFileChooser imagePicker;

    private JColorChooser colorPicker;

    private ArrayList<JRadioButton> selectionButtons;

    private ButtonGroup selectionType;

    public Picker() {
        // super(parent, "Resource Picker");
        pickerState = "image";

        TitledBorder selectionBorder = BorderFactory.createTitledBorder("Type: ");
        selectionBorder.setTitleJustification(TitledBorder.LEFT);

        upperPanel = new JPanel();
        upperPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), selectionBorder));

        selectionButtons = new ArrayList<JRadioButton>();
        selectionType = new ButtonGroup();

        lowerPanel = new JPanel();
        Dimension s = new Dimension(panelWidth, panelHeight);
        lowerPanel.setPreferredSize(s);

        colorPicker = new JColorChooser();

        // FIXME: Browse item then preview image under it. Only if user press browse,
        // then we will open up imagePicker.showOpenDialog();
        imagePicker = new JFileChooser();
        imagePicker.setFileSelectionMode(JFileChooser.FILES_ONLY);
        imagePicker.setFileFilter(new FileNameExtensionFilter("Image Files", ImageIO.getReaderFileSuffixes()));
        // ------------------------------- //

        changePickerPanel();

        setLayout(new BorderLayout());
        add(upperPanel, BorderLayout.NORTH);
        add(lowerPanel, BorderLayout.CENTER);
    }

    public void addSelectionType(String selectionType) {
        JRadioButton button = new JRadioButton(selectionType, pickerState.equalsIgnoreCase(selectionType));
        button.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED && !pickerState.equalsIgnoreCase(selectionType)) {
                pickerState = selectionType.toLowerCase();
                changePickerPanel();
            }
        });
        selectionButtons.add(button);
        this.selectionType.add(button);
        upperPanel.add(button);
    }

    private void changePickerPanel() {
        lowerPanel.removeAll();
        if (pickerState.equalsIgnoreCase("color")) {
            lowerPanel.add(colorPicker);
        } else if (pickerState.equalsIgnoreCase("image")) {
            lowerPanel.add(imagePicker);
        }
        lowerPanel.revalidate();
        lowerPanel.repaint();
    }

    public static void main(String[] args) {
        Picker p = new Picker();

        p.addSelectionType("color");
        p.addSelectionType("image");

        p.pack();
        p.setResizable(false);
        p.setVisible(true);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}
