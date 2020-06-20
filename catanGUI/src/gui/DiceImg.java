package gui;

import javafx.scene.image.Image;


public class DiceImg
{

    final Image dice1 = new Image(getClass().getResourceAsStream("/icon/dice1.png"));
    final Image dice2 = new Image(getClass().getResourceAsStream("/icon/dice2.png"));
    final Image dice3 = new Image(getClass().getResourceAsStream("/icon/dice3.png"));
    final Image dice4 = new Image(getClass().getResourceAsStream("/icon/dice4.png"));
    final Image dice5 = new Image(getClass().getResourceAsStream("/icon/dice5.png"));
    final Image dice6 = new Image(getClass().getResourceAsStream("/icon/dice6.png"));

    final Image[] images = new Image[6];

    public DiceImg()
    {
        images[0] = dice1;
        images[1] = dice2;
        images[2] = dice3;
        images[3] = dice4;
        images[4] = dice5;
        images[5] = dice6;
    }

    public Image[] getImages()
    {
        return images;
    }
}