package ru.luvas.multiutils.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@AllArgsConstructor
public class Section {
    
    @Getter
    protected String owner;
    
    public final void invalidate() {
        PlayerDatas.getCachedSection(this).invalidate(owner);
    }
    
}
