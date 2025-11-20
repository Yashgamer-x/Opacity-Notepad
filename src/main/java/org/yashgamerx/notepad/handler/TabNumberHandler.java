package org.yashgamerx.notepad.handler;


import lombok.Getter;

public class TabNumberHandler {
    @Getter
    private static int tabNumber = 1;

    public static int preIncrement(){
        return ++tabNumber;
    }

    public static int postIncrement(){
        return tabNumber++;
    }
}
