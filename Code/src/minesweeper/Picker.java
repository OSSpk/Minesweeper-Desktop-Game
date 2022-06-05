package minesweeper;

import java.util.ArrayList;
import java.util.HashMap;

import java.awt.event.ItemEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import javax.imageio.ImageIO;

public class Picker extends JDialog {
    public static final String COLOR_PICKER = "color";
    public static final String IMAGE_PICKER = "image";

    private String pickerState;
    private JFileChooser imagePicker;
    private CustomColorPicker colorPicker;

    private JPanel upperPanel;
    private JPanel lowerPanel;
    private ButtonGroup selectionType;
    private HashMap<String, Container> mode;
    private ArrayList<JRadioButton> selectionButtons;

    public Picker(JFrame parent) {
        super(parent, "Resource Picker");
        initPicker(COLOR_PICKER);

        addSelectionType(COLOR_PICKER, colorPicker);
        addSelectionType(IMAGE_PICKER, imagePicker);

        changePickerPanel();

    }

    public Picker(JFrame parent, String mode) {
        super(parent, "Resource Picker");
        if (mode.equals(COLOR_PICKER)) {
            initPicker(COLOR_PICKER);
            addSelectionType(COLOR_PICKER, colorPicker);
        } else if (mode.equals(IMAGE_PICKER)) {
            initPicker(IMAGE_PICKER);
            addSelectionType(IMAGE_PICKER, imagePicker);
        }
        changePickerPanel();
    }

    public Picker(JFrame parent, String[] modes) {
        super(parent, "Resource Picker");
        initPicker(modes[0]);
        for (String mode : modes) {
            switch (mode) {
                case COLOR_PICKER:
                    addSelectionType(COLOR_PICKER, colorPicker);
                    break;
                case IMAGE_PICKER:
                    addSelectionType(IMAGE_PICKER, imagePicker);
                    break;
            }
        }
        changePickerPanel();
    }

    public void addSelectionType(String selectionType, Container selectionPanel) {
        JRadioButton button = new JRadioButton(selectionType, pickerState.equalsIgnoreCase(selectionType));
        button.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED && !pickerState.equalsIgnoreCase(selectionType)) {
                pickerState = selectionType.toLowerCase();
                changePickerPanel();
            }
        });
        mode.put(selectionType.toLowerCase(), selectionPanel);
        selectionButtons.add(button);
        this.selectionType.add(button);
        upperPanel.add(button);
    }

    public JFileChooser getImagePicker() {
        return imagePicker;
    }

    public CustomColorPicker getColorPicker() {
        return colorPicker;
    }

    public String getPickerState() {
        return pickerState;
    }

    public void setPickerState(String state) {
        pickerState = state;
        changePickerPanel();
    }

    private void changePickerPanel() {
        lowerPanel.removeAll();
        lowerPanel.add(mode.get(pickerState));
        lowerPanel.revalidate();
        lowerPanel.repaint();
        pack();
    }

    private void initPicker(String initialMode) {
        pickerState = initialMode;
        mode = new HashMap<String, Container>();

        TitledBorder selectionBorder = BorderFactory.createTitledBorder("Type: ");
        selectionBorder.setTitleJustification(TitledBorder.LEFT);

        upperPanel = new JPanel();
        upperPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), selectionBorder));

        selectionButtons = new ArrayList<JRadioButton>();
        selectionType = new ButtonGroup();

        lowerPanel = new JPanel();

        colorPicker = new CustomColorPicker();

        imagePicker = new JFileChooser();
        imagePicker.setFileSelectionMode(JFileChooser.FILES_ONLY);
        imagePicker.setFileFilter(new FileNameExtensionFilter("Image Files", ImageIO.getReaderFileSuffixes()));

        setLayout(new BorderLayout());
        add(upperPanel, BorderLayout.NORTH);
        add(lowerPanel, BorderLayout.CENTER);
        pack();
    }
}

class CustomColorPicker extends JColorChooser {
    private JButton confirmButton;
    private JButton cancelButton;

    CustomColorPicker() {
        super();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        confirmButton = new JButton("Confirm");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JButton getConfirmButton() {
        return confirmButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
}
