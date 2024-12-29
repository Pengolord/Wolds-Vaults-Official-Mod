package xyz.iwolfking.woldsvaults.mixins.vaulthunters.fixes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import iskallia.vault.block.GateLockBlock;
import iskallia.vault.block.entity.GateLockTileEntity;
import iskallia.vault.block.render.GateLockRenderer;
import iskallia.vault.client.ClientStatisticsData;
import iskallia.vault.config.VaultModifierOverlayConfig;
import iskallia.vault.core.vault.modifier.VaultModifierStack;
import iskallia.vault.core.vault.modifier.registry.VaultModifierRegistry;
import iskallia.vault.core.vault.overlay.ModifiersRenderer;
import iskallia.vault.init.ModConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(value = GateLockRenderer.class, remap = false)
public abstract class MixinGateRenderer {
    @Shadow public abstract void renderLine(Component component, boolean centered, PoseStack matrices, MultiBufferSource source, int light);

    @Shadow protected abstract boolean check(List<ItemStack> items, ItemStack stack, boolean simulate);

    @Shadow public abstract void renderItemLine(ItemStack stack, Component text, Component count, boolean centered, PoseStack matrices, MultiBufferSource source, int light);

    /**
     * @author iwolfking
     * @reason You tell me not to overwrite, I say, shame on you, saving years off my life here
     */
    @Overwrite
    public void render(GateLockTileEntity entity, float pPartialTick, PoseStack matrices, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (entity.getGod() != null) {
            BlockState blockstate = entity.getBlockState();
            matrices.pushPose();
            float f = 0.6666667F;
            Vec3i vector = blockstate.getValue(GateLockBlock.FACING).getNormal();
            matrices.translate(0.5 + 0.9 * vector.getX(), 0.5 + 0.9 * vector.getY(), 0.5 + 0.9 * vector.getZ());
            float f4 = - blockstate.getValue(GateLockBlock.FACING).toYRot();
            matrices.mulPose(Vector3f.YP.rotationDegrees(f4));
            matrices.translate(0.0, -0.3125, -0.4375);
            matrices.translate(0.0, 0.3333333432674408, 0.046666666865348816);
            matrices.scale(0.010416667F, -0.010416667F, 0.010416667F);
            matrices.pushPose();
            matrices.pushPose();
            matrices.scale(2.0F, 2.0F, 2.0F);
            this.renderLine((new TextComponent(entity.getName())).withStyle(Style.EMPTY.withColor(entity.getColor())), true, matrices, pBufferSource, pPackedLight);
            matrices.popPose();
            Minecraft minecraft = Minecraft.getInstance();
            List<ItemStack> items = minecraft.player.inventoryMenu.getItems().stream().map(ItemStack::copy).toList();
            int count = entity.getModifiers().size();
            int reputation;
            if (count > 0) {
                matrices.translate(0.0, 30.0, 0.0);
                matrices.pushPose();
                int right = minecraft.getWindow().getGuiScaledWidth();
                reputation = minecraft.getWindow().getGuiScaledHeight();
                matrices.translate(-right, -reputation, 0.0);
                VaultModifierOverlayConfig config = ModConfigs.VAULT_MODIFIER_OVERLAY;
                matrices.translate(config.spacingX + (config.size + config.spacingX) * count / 2.0, 0.0, 0.0);
                ModifiersRenderer.renderVaultModifiers(entity.getModifiers(), matrices);
                matrices.popPose();
            }

            AtomicInteger index = new AtomicInteger(1);

            for (VaultModifierStack stack: entity.getModifiers()) {
                VaultModifierRegistry.getOpt(stack.getModifierId()).ifPresent(modifier -> {
                    matrices.pushPose();
                    matrices.translate(0.0, 10.0 * index.get(), 0.0);
                    String modifierName = modifier.getChatDisplayNameComponent(stack.getSize()).getString();
                    modifierName = modifierName.replaceAll(":.*: *", "");
                    Style style = Style.EMPTY.withColor(modifier.getDisplayTextColor());
                    this.renderLine(new TextComponent(modifierName).withStyle(style), true, matrices, pBufferSource, pPackedLight);
                    matrices.popPose();
                    index.getAndIncrement();
                });
            }

            if (entity.getReputationCost() > 0) {
                matrices.translate(0.0, 10.0 * index.get(), 0.0);
                matrices.pushPose();
                reputation = ClientStatisticsData.getReputation(entity.getGod());
                ChatFormatting form = reputation >= entity.getReputationCost() ? ChatFormatting.WHITE : ChatFormatting.RED;
                this.renderLine((new TextComponent("")).append((new TextComponent(entity.getReputationCost() + " ")).withStyle(form)).append((new TextComponent(entity.getGod().getName())).withStyle(entity.getGod().getChatColor())).append((new TextComponent(" Reputation")).withStyle(form)), true, matrices, pBufferSource, pPackedLight);
                matrices.popPose();
            }

            for (ItemStack stack : entity.getCost()) {
                matrices.translate(0.0, 45.0, 0.0);
                ChatFormatting color = this.check(items, stack.copy(), true) && this.check(items, stack.copy(), false) ? ChatFormatting.WHITE : ChatFormatting.RED;
                this.renderItemLine(stack, stack.getHoverName().copy().withStyle(color), (new TextComponent(stack.getCount() < 10 ? " " : "" + stack.getCount())).withStyle(color), true, matrices, pBufferSource, pPackedLight);
            }

            matrices.popPose();
            matrices.popPose();
        }
    }
}
