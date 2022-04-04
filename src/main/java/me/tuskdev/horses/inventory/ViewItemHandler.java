package me.tuskdev.horses.inventory;

@FunctionalInterface
public interface ViewItemHandler {

    void handle(ViewSlotContext context);

}
