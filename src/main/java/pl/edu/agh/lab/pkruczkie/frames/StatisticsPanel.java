package pl.edu.agh.lab.pkruczkie.frames;

import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.CurrentSituationObserversAggregate;

import javax.swing.*;
import java.awt.*;

public class StatisticsPanel extends JPanel {
    private final int simHeight;
    private final CurrentSituationObserversAggregate stats;

    private final String[] statsNames = {"Number of animals: ", "Number of plants: ", "Dominating genome: ",
            "Average energy: ", "Average lifespan", "Average children per animal: "};
    private String[] statsValues;
    private JLabel[] labelsToUpdate;

    private final int numberOfRows = statsNames.length + 1;

    public StatisticsPanel(int height, CurrentSituationObserversAggregate stats){
        this.simHeight = height;
        this.stats = stats;

        this.setPreferredSize(new Dimension(200, simHeight));
        this.setLayout(new GridLayout(numberOfRows,1));

        JLabel title = new JLabel("Statistics");
        title.setFont(new Font(null, Font.BOLD, 30));
        title.setHorizontalAlignment(0);
        this.add(title);

        this.labelsToUpdate = new JLabel[statsNames.length];
        this.statsValues = stats.getResults();
        for (int i = 0; i < statsValues.length; i++) {
            JPanel panel = makePanel(i);
            this.add(panel);
        }
        this.setBackground(new Color(123,123,231));
        this.setOpaque(true);
        this.setVisible(true);
        this.setFocusable(true);

    }

    private JPanel makePanel(int panelNumber){
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200, simHeight/numberOfRows));
        panel.setLayout(new GridLayout(2,1));

        JLabel label = new JLabel(statsNames[panelNumber]);
        label.setFont(new Font(null, Font.PLAIN, 15));
        panel.add(label);

        JLabel data = new JLabel(statsValues[panelNumber]);
        data.setFont(new Font(null, Font.PLAIN, 15));
        data.setHorizontalAlignment(0);
        labelsToUpdate[panelNumber] = data;
        panel.add(data);
        panel.setVisible(true);
        return panel;
    }

    public void updateValues(){
        statsValues = stats.getResults();
        for (int i = 0; i < statsValues.length; i++)
            labelsToUpdate[i].setText(statsValues[i]);
    }
}
