package ru.job4j.store;

import org.junit.Test;

import ru.job4j.models.*;

import static org.junit.Assert.*;

public class PsqlTrackerTest {
    @Test
    public void whenSaveNewItem() {
        User user = User.of("NewUser1", "newPassword1");
        String itemDesc = "new task";
        String[] cIds = {"1"};
        PsqlTracker.instOf().saveUser(user);
        PsqlTracker.instOf().save(user, itemDesc, cIds);
        assertEquals(itemDesc, PsqlTracker.instOf().findAll(user).get(0).getDescription());
    }
}