package de.ryuum3gum1n.adventurecraft.client.commands;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import de.ryuum3gum1n.adventurecraft.Reference;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.AdventureCraftBlocks;
import de.ryuum3gum1n.adventurecraft.AdventureCraftCommands;
import de.ryuum3gum1n.adventurecraft.AdventureCraftItems;
import de.ryuum3gum1n.adventurecraft.proxy.ClientProxy;

public final class FullDebugPrintCommand extends CommandBase {
	@Override
	public String getName() {
		return "acc_fulldebugprint";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		ClientProxy.shedule(new Runnable() {
			@Override
			public void run() {
				fullPrint();
			}
		});
	}

	private void fullPrint() {
		AdventureCraft.logger.info("Printing information about everything AC to console NOW ...");

		AdventureCraft.logger.info("AdventureCraft - " + Reference.MOD_VERSION);
		AdventureCraft.logger.info("--------------------------------------------------");
		AdventureCraft.logger.info("Client Proxy: " + Reference.CLIENT_PROXY);
		AdventureCraft.logger.info("Server Proxy: " + Reference.SERVER_PROXY);
		AdventureCraft.logger.info("INSTANCE: " + AdventureCraft.instance);
		AdventureCraft.logger.info("Proxy: " + AdventureCraft.proxy);
		AdventureCraft.logger.info("Logger: " + AdventureCraft.logger);
		AdventureCraft.logger.info("Network: " + AdventureCraft.network);
		AdventureCraft.logger.info("Container: " + AdventureCraft.container);
		AdventureCraft.logger.info("Script Manager: " + AdventureCraft.globalScriptManager);
		AdventureCraft.logger.info("Event Handler: " + AdventureCraft.eventHandler);

		AdventureCraft.logger.info("--------------------------------------------------");

		AdventureCraft.logger.info("Printing information about ALL blocks ...");

		List<Block> blocks = AdventureCraftBlocks.blocks;
		for(Block block : blocks) {
			AdventureCraft.logger.info("Block -> " + block.getUnlocalizedName() + ", TE?" + block.hasTileEntity(block.getDefaultState()) + ", STATE?" + block.getDefaultState());
		}

		AdventureCraft.logger.info("--------------------------------------------------");

		AdventureCraft.logger.info("Printing information about ALL items (including block items)...");

		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		List<Item> items = AdventureCraftItems.ALL_AC_ITEMS;
		for(Item item : items) {
			AdventureCraft.logger.info("Item -> " + item + " / " + item.getUnlocalizedName());

			NonNullList<ItemStack> stacks = NonNullList.create();
			item.getSubItems(null, stacks);

			for(ItemStack stack : stacks) {
				AdventureCraft.logger.info("ItemStack -> " + stack.getUnlocalizedName());

				IBakedModel model = mesher.getItemModel(stack);
				AdventureCraft.logger.info("ItemStack.Model -> " + model);
			}
		}

		AdventureCraft.logger.info("--------------------------------------------------");

		AdventureCraft.logger.info("Printing information about ALL commands (including client commands)...");

		List<CommandBase> commands = Lists.newArrayList();

		commands.addAll(AdventureCraftCommands.getCommandList());
		commands.addAll(AdventureCraftClientCommands.getCommandList());

		for(CommandBase command : commands) {
			AdventureCraft.logger.info("Command -> " + command.getName() + ", " + command.getRequiredPermissionLevel());
		}

		AdventureCraft.logger.info("--------------------------------------------------");

		AdventureCraft.logger.info("Statistics:");
		AdventureCraft.logger.info("Blocks    : " + blocks.size());
		AdventureCraft.logger.info("Items     : " + blocks.size() + " + " + (items.size()-blocks.size()) + " = " + items.size());
		AdventureCraft.logger.info("Commands  : " + commands.size());

	}
}