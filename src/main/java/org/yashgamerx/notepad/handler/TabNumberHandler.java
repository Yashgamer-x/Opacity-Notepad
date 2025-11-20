package org.yashgamerx.notepad.handler;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
