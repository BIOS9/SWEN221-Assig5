// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package gui;

import javafx.scene.shape.Circle;
import model.ResourceCounter;

public class CounterImg extends Circle {

	private ResourceCounter number;

	public CounterImg(Double size) {
		super(size);
	}

	public void setNumber(ResourceCounter n) {
		this.number = n;
	}

	public Integer getNumber() {
		return this.number.getNumber();
	}

}
