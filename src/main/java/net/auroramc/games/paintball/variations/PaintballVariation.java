/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.games.paintball.variations;

import net.auroramc.engine.api.games.GameVariation;
import net.auroramc.engine.api.players.AuroraMCGamePlayer;
import net.auroramc.games.paintball.Paintball;
import org.bukkit.entity.Projectile;

public abstract class PaintballVariation extends GameVariation<Paintball> {
    public PaintballVariation(Paintball game) {
        super(game);
    }

    public abstract void onThrow(AuroraMCGamePlayer player);
}
