package net.minecraft.world.level.storage.loot.entries;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.function.Consumer;
import net.minecraft.util.ChatDeserializer;
import net.minecraft.world.level.storage.loot.LootCollector;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public abstract class LootEntryChildrenAbstract extends LootEntryAbstract {

    protected final LootEntryAbstract[] children;
    private final LootEntryChildren composedChildren;

    protected LootEntryChildrenAbstract(LootEntryAbstract[] alootentryabstract, LootItemCondition[] alootitemcondition) {
        super(alootitemcondition);
        this.children = alootentryabstract;
        this.composedChildren = this.a((LootEntryChildren[]) alootentryabstract);
    }

    @Override
    public void a(LootCollector lootcollector) {
        super.a(lootcollector);
        if (this.children.length == 0) {
            lootcollector.a("Empty children list");
        }

        for (int i = 0; i < this.children.length; ++i) {
            this.children[i].a(lootcollector.b(".entry[" + i + "]"));
        }

    }

    protected abstract LootEntryChildren a(LootEntryChildren[] alootentrychildren);

    @Override
    public final boolean expand(LootTableInfo loottableinfo, Consumer<LootEntry> consumer) {
        return !this.a(loottableinfo) ? false : this.composedChildren.expand(loottableinfo, consumer);
    }

    public static <T extends LootEntryChildrenAbstract> LootEntryAbstract.Serializer<T> a(final LootEntryChildrenAbstract.a<T> lootentrychildrenabstract_a) {
        return new LootEntryAbstract.Serializer<T>() {
            public void a(JsonObject jsonobject, T t0, JsonSerializationContext jsonserializationcontext) {
                jsonobject.add("children", jsonserializationcontext.serialize(t0.children));
            }

            @Override
            public final T deserializeType(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootItemCondition[] alootitemcondition) {
                LootEntryAbstract[] alootentryabstract = (LootEntryAbstract[]) ChatDeserializer.a(jsonobject, "children", jsondeserializationcontext, LootEntryAbstract[].class);

                return lootentrychildrenabstract_a.create(alootentryabstract, alootitemcondition);
            }
        };
    }

    @FunctionalInterface
    public interface a<T extends LootEntryChildrenAbstract> {

        T create(LootEntryAbstract[] alootentryabstract, LootItemCondition[] alootitemcondition);
    }
}
