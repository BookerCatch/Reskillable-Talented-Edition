package codersafterdark.reskillable.common.network;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.common.Reskillable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageDataSync implements IMessage, IMessageHandler<MessageDataSync, IMessage> {
    public NBTTagCompound cmp;

    public MessageDataSync() {
    }

    public MessageDataSync(PlayerData data) {
        this.cmp = new NBTTagCompound();
        data.saveToNBT(this.cmp);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.cmp = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.cmp);
    }

    @Override
    public IMessage onMessage(MessageDataSync message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handleMessage(message));
        return null;
    }

    @SuppressWarnings("static-method")
	@SideOnly(Side.CLIENT)
    public void handleMessage(MessageDataSync message) {
        PlayerData data = PlayerDataHandler.get(Reskillable.proxy.getClientPlayer());
        data.loadFromNBT(message.cmp);
    }
}