package de.ryuum3gum1n.adventurecraft.client.gui.qad;

import java.util.Collection;

public interface QADComponentContainer {
	int getContainerWidth();

	int getContainerHeight();

	<T extends QADComponent> T addComponent(T c);

	Collection<QADComponent> getComponents();

	QADComponent getComponentByName(String name);

	void removeAllComponents();

	/*
	 * If getLayout returns NULL, this method is a NO-OP. Else, the
	 * 'layout()'-method of the QADLayoutManager is invoked. This method ignores the
	 * isLayoutDirty-method.
	 */
	void forceRebuildLayout();

	/*
	 * The layout should only be rebuilt if this flag returns true. Note: The
	 * forceRebuildLayout-method does NOT check this flag.
	 */
	boolean isLayoutDirty();

	/*
	 * Returns the QADLayoutManager for this container. If there is none, NULL will
	 * be returned.
	 */
	QADLayoutManager getLayout();

	/*
	 * Changes the layout manager to the given new layout manager. This will
	 * immediately call the forceRebuildLayout-method.
	 */
	void setLayout(QADLayoutManager newLayout);
}
