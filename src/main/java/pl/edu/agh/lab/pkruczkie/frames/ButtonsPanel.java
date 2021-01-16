package pl.edu.agh.lab.pkruczkie.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonsPanel extends JPanel implements ActionListener {
    private final int WIDTH;
    private final int HEIGHT;
    private int BUTTON_WIDTH = 150;
    private int BUTTON_HEIGHT = 40;

    boolean start_clicked = true;
    boolean genome_clicked = false;

    JButton startButton = new JButton("start");
    JButton stopButton = new JButton("stop");
    JButton genomeButton = new JButton("genome");

    public ButtonsPanel(int width, int height){
        this.WIDTH = width;
        this.HEIGHT = height;

        this.setLayout(null);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setButton(startButton, WIDTH/3-BUTTON_WIDTH/2, false);
        this.setButton(stopButton, 2*WIDTH/3-BUTTON_WIDTH/2, true);
        this.setButton(genomeButton, WIDTH-BUTTON_WIDTH/2, false);
        this.setLayout(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==startButton){
            start_clicked = true;
            changeEnablementSet();
        }
        if (e.getSource()==stopButton){
            start_clicked = false;
            changeEnablementSet();
        }
        if (e.getSource()==genomeButton){
            genome_clicked = !genome_clicked;
        }
    }

    private void changeEnablementSet(){
        startButton.setEnabled(!start_clicked);
        stopButton.setEnabled(start_clicked);
        genomeButton.setEnabled(!start_clicked);
    }

    private void setButton(JButton button, int x, boolean isEnabled){
        button.setBounds(x, (HEIGHT-BUTTON_HEIGHT)/2, BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setFocusable(false);
        button.setFont(new Font("Helvetica", Font.ITALIC, 25));
        button.addActionListener(this);
        button.setEnabled(isEnabled);
        this.add(button);
    }

    public boolean isStart_clicked() {
        return start_clicked;
    }

    public boolean isGenome_clicked() {
        return genome_clicked;
    }
}

