package io.github.restioson.loopdeloop.game;

import io.github.restioson.loopdeloop.game.map.LoopDeLoopMap;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameMode;
import xyz.nucleoid.plasmid.api.game.GameOpenException;
import xyz.nucleoid.plasmid.api.game.player.JoinAcceptor;
import xyz.nucleoid.plasmid.api.game.player.JoinAcceptorResult;

import java.util.Set;


public final class LoopDeLoopSpawnLogic {
    private final ServerWorld world;
    private final LoopDeLoopMap map;

    public LoopDeLoopSpawnLogic(ServerWorld world, LoopDeLoopMap map) {
        this.world = world;
        this.map = map;
    }

    public JoinAcceptorResult acceptPlayer(JoinAcceptor offer, GameMode gameMode) {
        return offer.teleport(this.world, this.generateSpawn(this.world.getRandom()))
                .thenRunForEach(player -> this.resetPlayer(player, gameMode));
    }

    public void resetPlayer(ServerPlayerEntity player, GameMode gameMode) {
        player.changeGameMode(gameMode);

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.NIGHT_VISION,
                StatusEffectInstance.INFINITE,
                1,
                true,
                false
        ));
    }

    public void spawnPlayer(ServerPlayerEntity player) {
        var spawn = this.generateSpawn(player.getRandom());

        player.teleport(this.world, spawn.x, spawn.y, spawn.z, Set.of(), 0.0F, 0.0F, false);
    }

    private Vec3d generateSpawn(Random random) {
        BlockPos spawn = this.map.getSpawn();
        if (spawn == null) {
            throw new GameOpenException(Text.literal("Cannot spawn player! No spawn defined in map!"));
        }

        float radius = 2.5f;
        double x = spawn.getX() + MathHelper.nextDouble(random, -radius, radius);
        double z = spawn.getZ() + 1 + MathHelper.nextDouble(random, -radius, radius);
        return new Vec3d(x, spawn.getY(), z);
    }
}
