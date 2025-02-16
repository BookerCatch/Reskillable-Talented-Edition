package codersafterdark.reskillable.common.network;

import java.util.Objects;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerProfessionInfo;
import codersafterdark.reskillable.api.data.PlayerTalentInfo;
import codersafterdark.reskillable.api.event.UpgradeTalentEvent;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.api.talent.Talent;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpgradeTalent implements IMessage, IMessageHandler<MessageUpgradeTalent, IMessage> {
    private ResourceLocation profession;
    private ResourceLocation talent;

    public MessageUpgradeTalent() {
    }

    public MessageUpgradeTalent(ResourceLocation profession, ResourceLocation talent) {
        this.profession = profession;
        this.talent = talent;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.profession = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        this.talent = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.profession.toString());
        ByteBufUtils.writeUTF8String(buf, this.talent.toString());
    }

    @Override
    public IMessage onMessage(MessageUpgradeTalent message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(message, ctx));
        return null;
    }

    @SuppressWarnings("static-method")
	public IMessage handleMessage(MessageUpgradeTalent message, MessageContext context) {
        EntityPlayer player = context.getServerHandler().player;
        Profession profession = ReskillableRegistries.PROFESSIONS.getValue(message.profession);
        Talent talent = Objects.requireNonNull(ReskillableRegistries.TALENTS.getValue(message.talent));
        PlayerData data = PlayerDataHandler.get(player);
        PlayerProfessionInfo info = data.getProfessionInfo(profession);
        PlayerTalentInfo talentInfo = data.getTalentInfo(talent);
        if (!talentInfo.isCapped() && info.getProfessionPoints() >= talent.getCost()) {
            int oldRank = talentInfo.getLevel();
            if (!MinecraftForge.EVENT_BUS.post(new UpgradeTalentEvent.Pre(player, talent, oldRank + 1, oldRank))) {
                talentInfo.levelUp();
                info.spendSkillPoints(talentInfo.getLevelUpCost());
                data.saveAndSync();
                MinecraftForge.EVENT_BUS.post(new UpgradeTalentEvent.Post(player, talent, talentInfo.getLevel(), oldRank));
            }
        }
        return null;
    }

}
