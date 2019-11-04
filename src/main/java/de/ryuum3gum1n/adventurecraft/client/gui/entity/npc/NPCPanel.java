package de.ryuum3gum1n.adventurecraft.client.gui.entity.npc;

import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADPanel;
import de.ryuum3gum1n.adventurecraft.entity.NPC.NPCData;

public abstract class NPCPanel extends QADPanel {

	protected NPCData data;
	protected int width, height;

	public NPCPanel(NPCData data, int width, int height) {
		setBackgroundColor(2);
		this.data = data;
		this.width = width;
		this.height = height;
	}

	public abstract void save(NPCData data);

}