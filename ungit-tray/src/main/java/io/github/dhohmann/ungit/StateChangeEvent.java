package io.github.dhohmann.ungit;

import java.util.EventObject;

public class StateChangeEvent extends EventObject {

	private static final long serialVersionUID = -7879187277037766389L;

	public StateChangeEvent(Ungit ungit) {
		super(ungit);
	}
	

}
