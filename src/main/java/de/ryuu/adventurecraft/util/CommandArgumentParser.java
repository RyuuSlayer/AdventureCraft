package de.ryuu.adventurecraft.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CommandArgumentParser {
    // configurable data
    public Vec3d commandSenderPosition;
    public Entity commandSenderEntity;
    private int index;
    private final String[] arguments;

    public CommandArgumentParser(String[] arguments, int start) {
        this.arguments = arguments;
        this.index = start;
    }

    public CommandArgumentParser(String[] arguments) {
        this(arguments, 0);
    }

    private boolean outbounds(int index) {
        // only check if index is out of bounds upwards
        return !(index < arguments.length);
    }

    public int remaining() {
        return arguments.length - index;
    }

    public String consume_string(String errorText) throws CommandException {
        if (outbounds(index)) {
            throw new SyntaxErrorException(errorText, new RuntimeException("Index out of bounds!"));
        } else {
            return arguments[index++];
        }
    }

    public int consume_int(String errorText) throws CommandException {
        String str = consume_string(errorText);

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorText, e);
        }
    }

    public int consume_int(String errorText, int max) throws CommandException {
        String str = consume_string(errorText);

        try {
            int l = Integer.parseInt(str);

            if (l > max) {
                throw new NumberFormatException("Given value '" + l + "' is higher than maximum value '" + max + "'.");
            }

            return l;
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorText, e);
        }
    }

    public int consume_int(String errorText, int min, int max) throws CommandException {
        String str = consume_string(errorText);

        try {
            int l = Integer.parseInt(str);

            if (l > max) {
                throw new NumberFormatException("Given value '" + l + "' is higher than maximum value '" + max + "'.");
            }

            if (l < min) {
                throw new NumberFormatException("Given value '" + l + "' is smaller than minimum value '" + min + "'.");
            }

            return l;
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorText, e);
        }
    }

    public long consume_long(String errorText) throws CommandException {
        String str = consume_string(errorText);

        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorText, e);
        }
    }

    public long consume_long(String errorText, long max) throws CommandException {
        String str = consume_string(errorText);

        try {
            long l = Long.parseLong(str);

            if (l > max) {
                throw new NumberFormatException("Given value '" + l + "' is higher than maximum value '" + max + "'.");
            }

            return l;
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorText, e);
        }
    }

    public long consume_long(String errorText, long min, long max) throws CommandException {
        String str = consume_string(errorText);

        try {
            long l = Long.parseLong(str);

            if (l > max) {
                throw new NumberFormatException("Given value '" + l + "' is higher than maximum value '" + max + "'.");
            }

            if (l < min) {
                throw new NumberFormatException("Given value '" + l + "' is smaller than minimum value '" + min + "'.");
            }

            return l;
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorText, e);
        }
    }

    public float consume_float(String errorText) throws CommandException {
        String str = consume_string(errorText);

        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorText, e);
        }
    }

    public float consume_float(String errorText, float max) throws CommandException {
        String str = consume_string(errorText);

        try {
            float l = Float.parseFloat(str);

            if (l > max) {
                throw new NumberFormatException("Given value '" + l + "' is higher than maximum value '" + max + "'.");
            }

            return l;
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorText, e);
        }
    }

    public float consume_float(String errorText, float min, float max) throws CommandException {
        String str = consume_string(errorText);

        try {
            float l = Float.parseFloat(str);

            if (l > max) {
                throw new NumberFormatException("Given value '" + l + "' is higher than maximum value '" + max + "'.");
            }

            if (l < min) {
                throw new NumberFormatException("Given value '" + l + "' is smaller than minimum value '" + min + "'.");
            }

            return l;
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorText, e);
        }
    }

    public double consume_double(String errorText) throws CommandException {
        String str = consume_string(errorText);

        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorText, e);
        }
    }

    public double consume_double(String errorText, double max) throws CommandException {
        String str = consume_string(errorText);

        try {
            double l = Double.parseDouble(str);

            if (l > max) {
                throw new NumberFormatException("Given value '" + l + "' is higher than maximum value '" + max + "'.");
            }

            return l;
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorText, e);
        }
    }

    public double consume_double(String errorText, double min, double max) throws CommandException {
        String str = consume_string(errorText);

        try {
            double l = Double.parseDouble(str);

            if (l > max) {
                throw new NumberFormatException("Given value '" + l + "' is higher than maximum value '" + max + "'.");
            }

            if (l < min) {
                throw new NumberFormatException("Given value '" + l + "' is smaller than minimum value '" + min + "'.");
            }

            return l;
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorText, e);
        }
    }

    public BlockPos consume_blockpos(String errorText) throws CommandException {
        String xPosStr = consume_string(errorText);
        String yPosStr = consume_string(errorText);
        String zPosStr = consume_string(errorText);

        if (xPosStr.equals("~"))
            xPosStr = xPosStr.concat("0");
        if (yPosStr.equals("~"))
            yPosStr = yPosStr.concat("0");
        if (zPosStr.equals("~"))
            zPosStr = zPosStr.concat("0");

        boolean xPosr = xPosStr.charAt(0) == '~';
        boolean yPosr = yPosStr.charAt(0) == '~';
        boolean zPosr = zPosStr.charAt(0) == '~';

        if (xPosr)
            xPosStr = xPosStr.substring(1);
        if (yPosr)
            yPosStr = yPosStr.substring(1);
        if (zPosr)
            zPosStr = zPosStr.substring(1);

        int xPos = parseInt(xPosStr, errorText);
        int yPos = parseInt(yPosStr, errorText);
        int zPos = parseInt(zPosStr, errorText);

        if (commandSenderPosition != null) {
            if (xPosr)
                xPos += Math.floor(commandSenderPosition.x);
            if (yPosr)
                yPos += Math.floor(commandSenderPosition.y);
            if (zPosr)
                zPos += Math.floor(commandSenderPosition.z);
        }

        return new BlockPos(xPos, yPos, zPos);
    }

    public Block consume_blocktype(String errorText) throws CommandException {
        // Structures:
        // - stone
        // - stone/0
        // - minecraft:stone
        // - minecraft:stone/0
        // ---

        String str = consume_string(errorText);

        String typeStr = str;
        // String metaStr = "-1";

        if (str.indexOf('/') != -1) {
            int ix = str.indexOf('/');
            typeStr = str.substring(0, ix);
            // metaStr = str.substring(ix +1);
        }

        ResourceLocation location = new ResourceLocation(typeStr);

        return Block.REGISTRY.getObject(location);
    }

    public IBlockState consume_blockstate(String errorText) throws CommandException {
        // Structures:
        // - stone
        // - stone/0
        // - minecraft:stone
        // - minecraft:stone/0
        // ---

        String str = consume_string(errorText);

        String typeStr = str;
        String metaStr = "-1";

        if (str.indexOf('/') != -1) {
            int ix = str.indexOf('/');
            typeStr = str.substring(0, ix);
            metaStr = str.substring(ix + 1);
        }

        ResourceLocation location = new ResourceLocation(typeStr);
        Block type = Block.REGISTRY.getObject(location);

        int meta = parseInt(metaStr, "Invalid meta-value string.");
        IBlockState state = type.getDefaultState();

        if (meta >= 0 && meta < 16) {
            state = type.getStateFromMeta(meta);
        }

        return state;
    }

    private int parseInt(String valueStr, String errorText) throws CommandException {
        try {
            return Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorText, e);
        }
    }

}