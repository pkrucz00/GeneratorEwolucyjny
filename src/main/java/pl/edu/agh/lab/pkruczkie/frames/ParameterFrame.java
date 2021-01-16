package pl.edu.agh.lab.pkruczkie.frames;

import pl.edu.agh.lab.pkruczkie.simulationEngine.Parameters;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParameterFrame extends JFrame implements ActionListener {
    private final String[] paramNames = {"Map width", "Map height", "Jungle ratio (percent)", "Start energy", "Move energy", "Plant energy", "Number of initial animals"};
    private final int n = paramNames.length;
    private final int WIDTH = 400;
    private final int PANEL_HEIGHT = 50;

    private final JButton submitButton;
    private final JTextField[] textFields;
    private final JCheckBox twoMapsCheckBox;

    private int[] parametersArr;

    public ParameterFrame(){
        Image icon = new ImageIcon("src/main/resources/images/myszojelen.png").getImage();

        textFields = new JTextField[n];
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        twoMapsCheckBox = new JCheckBox("Two maps");

        JPanel[] panels = this.createPanels(paramNames);

        this.setIconImage(icon);
        this.setTitle("Evolution game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(WIDTH, (n+3)*PANEL_HEIGHT));
        for (JPanel panel: panels){
            this.add(panel);
        }
        this.pack();
        this.setLayout(new FlowLayout());
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }

    private JPanel[] createPanels(String[] paramNames){
        int n = paramNames.length;
        JPanel[] panels = new JPanel[n+2];
        for (int i = 0; i < n; i++){
            JLabel paramName = new JLabel(paramNames[i]);
            paramName.setFont(new Font(null, Font.PLAIN, 15));

            JPanel textFieldPanel = new JPanel();
            textFieldPanel.setPreferredSize(new Dimension(WIDTH/3, PANEL_HEIGHT-20));

            textFields[i] = new JTextField();
            textFields[i].setPreferredSize(new Dimension(WIDTH/3, PANEL_HEIGHT-20));
            textFields[i].setHorizontalAlignment(JTextField.CENTER);
            textFields[i].setFont(new Font(null, Font.PLAIN, 25));

            textFieldPanel.add(textFields[i]);
            textFieldPanel.setBounds(WIDTH/2, PANEL_HEIGHT/2, WIDTH/3, PANEL_HEIGHT-20);
            textFieldPanel.setLayout(new GridLayout());
            textFieldPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1,2));
            panel.add(paramName);
            panel.add(textFieldPanel);
            panel.setPreferredSize(new Dimension(WIDTH, PANEL_HEIGHT));
            panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
            panels[i] = panel;
        }

        twoMapsCheckBox.setFont(new Font(null, Font.PLAIN, 15));

        addSinglePanel(1,2,WIDTH, PANEL_HEIGHT/3, twoMapsCheckBox,
                BorderFactory.createEmptyBorder(0, 20, 0, 0), panels, n);
        addSinglePanel(1,1,WIDTH, PANEL_HEIGHT, submitButton,
                BorderFactory.createEmptyBorder(0, 20, 0, 20), panels, n+1);
        return panels;

    }

    private void addSinglePanel(int gLayoutRows, int gLayoutCols, int width, int height,
                               Component component, Border border, JPanel[] panels, int i){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(gLayoutRows, gLayoutCols));
        panel.setPreferredSize(new Dimension(width, height));
        panel.add(component);
        panel.setBorder(border);
        panels[i] = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == submitButton){
            parametersArr = new int[n];
            for (int i = 0; i < this.n; i++){
                String tmp = textFields[i].getText();
                parametersArr[i] = Integer.parseInt(tmp);
            }


            Parameters parameters = new Parameters(parametersArr);
            if (parameters.width/parameters.height >= 2)
                JOptionPane.showMessageDialog(this,
                        "Warning! Width of the window may exceed the limits of the monitor");

            this.dispose();
            new SimulationFrame(parameters);
            if (twoMapsCheckBox.isSelected()){
                new SimulationFrame(parameters);
            }
        }

    }
}
