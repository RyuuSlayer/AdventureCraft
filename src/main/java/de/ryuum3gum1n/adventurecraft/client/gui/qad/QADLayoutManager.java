package de.ryuum3gum1n.adventurecraft.client.gui.qad;

import java.util.List;

import de.ryuum3gum1n.adventurecraft.util.Vec2i;

/*
	(Abstract interface for declaring layout managers)
	
	A layout-manager is a class that lays out the component within a container,
	according to coded in rules and parameters given to the layout-manager.
*/
public interface QADLayoutManager {

	public void layout(QADComponentContainer container, List<QADComponent> components, Vec2i newContainerSize);

}