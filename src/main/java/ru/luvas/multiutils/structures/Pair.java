package ru.luvas.multiutils.structures;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@Data
@AllArgsConstructor
public class Pair<A, B> {

    private A first;
    
    private B second;
    
}
