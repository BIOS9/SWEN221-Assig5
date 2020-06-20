package gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Dice
{
    ImageView diceFace;
    Image[] images;

    public Dice(Image[] images)
    {
        this.images = images;
        diceFace = new ImageView(this.images[0]);//set default to image 0
    }

    public Dice(Image[] images, int diceFaceValue)
    {
        //Need to catch for values less than 1 and greater than 6!
        this.images = images;
        diceFace = new ImageView(this.images[diceFaceValue - 1]);
    }

    public ImageView getdiceFace()
    {
        return diceFace;
    }

    public void setdiceFace(int diceFaceValue)
    {
        //Need to catch for values less than 1 and greater than 6!
        diceFace.setImage(this.images[diceFaceValue - 1]);
    }
}