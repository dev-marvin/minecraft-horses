package me.tuskdev.horses.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import me.tuskdev.horses.controller.CustomHorseController;
import me.tuskdev.horses.model.CustomHorse;

import java.time.Duration;
import java.util.UUID;

public class CustomHorseCache {

    private final LoadingCache<UUID, CustomHorse> CACHE;

    public CustomHorseCache(CustomHorseController customHorseController) {
        CACHE = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(10)).build(customHorseController::select);
    }

    public CustomHorse select(UUID uuid) {
        return CACHE.get(uuid);
    }

    public void insert(CustomHorse customHorse) {
        CACHE.put(customHorse.getUniqueId(), customHorse);
    }

    public void delete(UUID uuid) {
        CACHE.invalidate(uuid);
    }

}
