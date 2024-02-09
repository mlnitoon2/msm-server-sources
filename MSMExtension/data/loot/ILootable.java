package com.bigbluebubble.mysingingmonsters.data.loot;

import java.util.List;
import java.util.Random;

public interface ILootable {
   List<LootResult> pull(Random var1, int var2, int var3);
}
