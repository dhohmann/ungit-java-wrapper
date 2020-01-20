package io.github.dhohmann.ungit;

import java.util.EventListener;

public interface StateListener extends EventListener {

	public void onStateChange(StateChangeEvent e);
	
}
