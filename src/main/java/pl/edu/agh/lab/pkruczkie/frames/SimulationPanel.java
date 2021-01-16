package pl.edu.agh.lab.pkruczkie.frames;

import pl.edu.agh.lab.pkruczkie.animal.Genome;
import pl.edu.agh.lab.pkruczkie.animal.IAnimal;
import pl.edu.agh.lab.pkruczkie.maps.IWorldMap;
import pl.edu.agh.lab.pkruczkie.auxiliary.Vector2d;
import pl.edu.agh.lab.pkruczkie.observers.AllAverageObserver;
import pl.edu.agh.lab.pkruczkie.observers.animalPickerObservers.AnimalPicker;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.CurrentSituationObserversAggregate;
import pl.edu.agh.lab.pkruczkie.simulationEngine.Engine;
import pl.edu.agh.lab.pkruczkie.simulationEngine.Parameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;
import java.util.SortedSet;

import static java.lang.Integer.max;
import static java.lang.Math.min;

public class SimulationPanel extends JPanel implements ActionListener, MouseListener {
    Timer timer;
    private int SIM_HEIGHT = 600;    //we need to change it once - otherwise white line on the left would appear
    private final int SIM_WIDTH;
    private final int UNIT_SIZE;
    private final int DELAY = 200;

    private final String filename = "src/jsonEndFiles/endStatistics.json";

    private final IWorldMap map;
    private final Engine engine;
    private final CurrentSituationObserversAggregate stats; //dynamic statistics on left side of the screen
    private final AllAverageObserver endStats;          //statistics at the end (averaged out) saved to json

    private ButtonsPanel buttonsPanel;
    private StatisticsPanel statisticsPanel;
    private AnimalPicker animalPicker;


    private boolean running = false;

    public SimulationPanel(Parameters parameters) {
        UNIT_SIZE = SIM_HEIGHT / parameters.height;       //divided by height of the map
        SIM_WIDTH = UNIT_SIZE * parameters.width;       //multiplied by height of map
        SIM_HEIGHT = UNIT_SIZE * parameters.height;        //divided by width

        this.engine = new Engine(parameters);
        this.map = engine.getMap();
        this.stats = new CurrentSituationObserversAggregate(engine);
        this.endStats = new AllAverageObserver(stats);

        this.setPreferredSize(new Dimension(SIM_WIDTH, SIM_HEIGHT));
        this.setBackground(Color.cyan);
        this.addMouseListener(this);
        this.setFocusable(true);
        startSimulation();
    }

    public void startSimulation() {
        timer = new Timer(DELAY, this);
        timer.start();
        running = true;
    }

    private void endSimulation() {
        running = false;
        repaint();      //print of "All animals are dead" sign
        timer.stop();
        int n = showYesNoPrompt("Save statistics to " + filename + " ?", "Saving statistics");
        if (n == 0) {       //user clicked yes
            endStats.saveJson(filename, this);
        }
    }

    public void paint(Graphics g) {
        if (running) {
            drawArea(g, this.map.getMapBounds(), Color.decode("#a9ee27"));
            drawArea(g, this.map.getJungleBounds(), Color.decode("#47c443"));

            drawAnimals(g, this.map.getAnimalsPositions());
            drawPlants(g, this.map.getPlantsPositions());
        } else {
            endScreen(g);
        }
    }

    private void drawArea(Graphics g, Vector2d[] bounds, Color color) {
        Vector2d upperLeft = bounds[0];
        Vector2d lowerRight = bounds[1];
        g.setColor(color);
        for (int i = upperLeft.x; i <= lowerRight.x; i++) {
            for (int j = lowerRight.y; j <= upperLeft.y; j++) {
                g.fillRect(i * UNIT_SIZE, j * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    private void drawAnimals(Graphics g, Set<Vector2d> positions) {
        for (Vector2d position : positions) {
            IAnimal strongestAnimal = this.map.animalsAt(position).first();
            int currEnergy = strongestAnimal.getEnergy();
            int startEnergy = engine.getParams().startEnergy;
            int blueness = max(255 * min(startEnergy, currEnergy) / startEnergy, 0);

            g.setColor(new Color(255 - blueness, 0, blueness));
            int x = position.x;
            int y = position.y;
            g.fillOval(x * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

            if (animalPicker != null)
                drawPickedAnimal(g, strongestAnimal, x, y);
            if (buttonsPanel.isGenome_clicked())
                drawAnimalWithDominantGenome(g, strongestAnimal, x, y);


        }
    }

    private void drawPickedAnimal(Graphics g, IAnimal animal, int x, int y){
        IAnimal pickedAnimal = animalPicker.getPickedAnimal();
        if (animal.equals(pickedAnimal)) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.decode("#03f8fc"));
            g2.setStroke(new BasicStroke(3));
            g2.drawArc(x * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, 0, 360);
        }
    }
    private void drawAnimalWithDominantGenome(Graphics g, IAnimal animal, int x, int y){
        Genome animalsGenome = animal.getGenome();
        Genome dominantGenome = stats.getDominantGenome();
        if (animalsGenome.equals(dominantGenome)){
            g.setColor(Color.WHITE);
            int xCoord = x * UNIT_SIZE + UNIT_SIZE/4;
            int yCoord = y * UNIT_SIZE + UNIT_SIZE/4;
            g.fillOval(xCoord, yCoord , UNIT_SIZE/2, UNIT_SIZE/2);
        }
    }

    private void drawPlants(Graphics g, Set<Vector2d> positions) {
        g.setColor(Color.GREEN);
        for (Vector2d position : positions) {
            int x = position.x;
            int y = position.y;
            g.fillRoundRect(x * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, UNIT_SIZE/2, UNIT_SIZE/2);
        }
    }

    private void endScreen(Graphics g) {
        drawArea(g, map.getMapBounds(), Color.decode("#c9ed37"));

        g.setColor(Color.BLUE);
        g.setFont(new Font("Helvetica", Font.ITALIC, 60));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("All animals died", (SIM_WIDTH - metrics2.stringWidth("All animals died")) / 2, SIM_HEIGHT / 2);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && buttonsPanel.isStart_clicked()) {
            engine.simulateDay();
            updateObservers();
        }
        if (this.map.areAnimalsDead()) {
            endSimulation();
        }
        repaint();

    }

    public void updateObservers(){
        statisticsPanel.updateValues();
        endStats.update();
        if (animalPicker != null) {
            animalPicker.update();
        }
    }

    private int showYesNoPrompt(String message, String title){
        return JOptionPane.showOptionDialog(this,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, null,null);

    }



    private IAnimal findClickedAnimal(int x, int y){
        Vector2d position = new Vector2d(x/UNIT_SIZE, y/UNIT_SIZE);
        SortedSet<IAnimal> animalsAtPosition = engine.getMap().animalsAt(position);
        if (animalsAtPosition != null && !animalsAtPosition.isEmpty())
            return animalsAtPosition.first();

        return null;
    }

    public void setStatisticsPanel(StatisticsPanel statisticsPanel) {
        this.statisticsPanel = statisticsPanel;
    }

    public void setButtonsPanel(ButtonsPanel buttonsPanel){
        this.buttonsPanel = buttonsPanel;
    }

    public void setAnimalPicker(AnimalPicker animalPicker){
        this.animalPicker = animalPicker;
    }

    public int getSIM_HEIGHT() {
        return SIM_HEIGHT;
    }

    public int getSIM_WIDTH() {
        return SIM_WIDTH;
    }

    public CurrentSituationObserversAggregate getStats(){ return stats;}

    public Engine getEngine() {
        return engine;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!buttonsPanel.isStart_clicked()){
            IAnimal pickedAnimal = findClickedAnimal(e.getX(), e.getY());
            if (pickedAnimal != null) {
                String message = "You picked animal with genome " + pickedAnimal.getGenome() +"\n"
                        +"If you want to observe it for n days, write a number down below";

                String n = (String) JOptionPane.showInputDialog(
                        this,
                        message,
                        "Observe animal",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "0");
                if (n != null)
                    this.setAnimalPicker(new AnimalPicker(pickedAnimal, this, Integer.parseInt(n)));
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}
