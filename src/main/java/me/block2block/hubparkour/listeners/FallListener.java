package me.block2block.hubparkour.listeners;

import me.block2block.hubparkour.Main;
import me.block2block.hubparkour.entities.HubParkourPlayer;
import me.block2block.hubparkour.managers.CacheManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@SuppressWarnings("ALL")
public class FallListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {

            Player p = (Player) e.getEntity();
            if (CacheManager.isParkour(p)) {
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    if (!Main.getInstance().getConfig().getBoolean("Settings.Teleport.On-Fall.Enabled") || p.getFallDistance() < Main.getInstance().getConfig().getDouble("Settings.Teleport.On-Fall.Minimum-Distance")) {
                        if (Main.getInstance().getConfig().getBoolean("Settings.Cancel-Fall-Damage")) {
                            e.setCancelled(true);
                        }
                        return;
                    }
                } else if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    if (!Main.getInstance().getConfig().getBoolean("Settings.Teleport.On-Void")) {
                        return;
                    }
                } else {
                    if (Main.getInstance().getConfig().getBoolean("Settings.Health.Disable-Damage")) {
                        e.setCancelled(true);
                    }
                    return;
                }

                e.setCancelled(true);
                p.setFallDistance(0);
                HubParkourPlayer player = CacheManager.getPlayer(p);

                Location l = player.getParkour().getRestartPoint().getLocation().clone();
                if (player.getLastReached() != 0) {
                    l = player.getParkour().getCheckpoint(player.getLastReached()).getLocation().clone();
                }
                l.setX(l.getX() + 0.5);
                l.setY(l.getY() + 0.5);
                l.setZ(l.getZ() + 0.5);
                double health = p.getHealth();
                Main.getInstance().getLogger().info("[DEBUG] USER HAS BEEN TELEPORTED, REASON: " + e.getCause().name() + ", LOCATION: " + p.getLocation().toString());
                p.teleport(l);
                p.sendMessage(Main.c(true, Main.getInstance().getConfig().getString("Messages.Parkour.Teleport")));
            }
        }
    }
}
