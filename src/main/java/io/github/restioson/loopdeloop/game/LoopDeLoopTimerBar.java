package io.github.restioson.loopdeloop.game;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import xyz.nucleoid.plasmid.api.game.common.GlobalWidgets;
import xyz.nucleoid.plasmid.api.game.common.widget.BossBarWidget;

public final class LoopDeLoopTimerBar {
    private final BossBarWidget bar;

    public LoopDeLoopTimerBar(GlobalWidgets widgets) {
        Text title = Text.literal("Waiting for the game to start...");
        this.bar = widgets.addBossBar(title, BossBar.Color.GREEN, BossBar.Style.NOTCHED_10);
    }

    public void update(long ticksUntilEnd, long totalTicksUntilEnd) {
        if (ticksUntilEnd % 20 == 0) {
            this.bar.setTitle(this.getText(ticksUntilEnd));
            this.bar.setProgress((float) ticksUntilEnd / totalTicksUntilEnd);
        }
    }

    private Text getText(long ticksUntilEnd) {
        long secondsUntilEnd = ticksUntilEnd / 20;

        long minutes = secondsUntilEnd / 60;
        long seconds = secondsUntilEnd % 60;
        String time = String.format("%02d:%02d left", minutes, seconds);

        return Text.literal(time);
    }
}
