package pl.edu.agh.lab.pkruczkie.frames;

import pl.edu.agh.lab.pkruczkie.simulationEngine.Parameters;

import javax.swing.*;
import java.awt.*;

public class SimulationFrame extends JFrame {
    private final SimulationPanel simulationPanel;
    private final ButtonsPanel buttonsPanel;
    private final StatisticsPanel statisticsPanel;

    public SimulationFrame(Parameters parameters){
        Image icon = new ImageIcon("src/main/resources/images/myszojelen.png").getImage();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        simulationPanel = new SimulationPanel(parameters);
        this.add(simulationPanel, BorderLayout.CENTER);

        buttonsPanel = new ButtonsPanel(simulationPanel.getSIM_WIDTH(), 50);
        simulationPanel.setButtonsPanel(buttonsPanel);
        this.add(buttonsPanel, BorderLayout.PAGE_END);


        statisticsPanel = new StatisticsPanel(simulationPanel.getSIM_HEIGHT(), simulationPanel.getStats());
        simulationPanel.setStatisticsPanel(statisticsPanel);
        this.add(statisticsPanel, BorderLayout.LINE_START);



        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.setIconImage(icon);
        this.setTitle("Evolution game");
        this.setResizable(true);    //zmien to za chwile
    }
}
