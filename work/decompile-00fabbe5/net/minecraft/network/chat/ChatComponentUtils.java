package net.minecraft.network.chat;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.DataFixUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.EnumChatFormat;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.world.entity.Entity;

public class ChatComponentUtils {

    public static final String DEFAULT_SEPARATOR_TEXT = ", ";
    public static final IChatBaseComponent DEFAULT_SEPARATOR = (new ChatComponentText(", ")).a(EnumChatFormat.GRAY);
    public static final IChatBaseComponent DEFAULT_NO_STYLE_SEPARATOR = new ChatComponentText(", ");

    public ChatComponentUtils() {}

    public static IChatMutableComponent a(IChatMutableComponent ichatmutablecomponent, ChatModifier chatmodifier) {
        if (chatmodifier.g()) {
            return ichatmutablecomponent;
        } else {
            ChatModifier chatmodifier1 = ichatmutablecomponent.getChatModifier();

            return chatmodifier1.g() ? ichatmutablecomponent.setChatModifier(chatmodifier) : (chatmodifier1.equals(chatmodifier) ? ichatmutablecomponent : ichatmutablecomponent.setChatModifier(chatmodifier1.setChatModifier(chatmodifier)));
        }
    }

    public static Optional<IChatMutableComponent> a(@Nullable CommandListenerWrapper commandlistenerwrapper, Optional<IChatBaseComponent> optional, @Nullable Entity entity, int i) throws CommandSyntaxException {
        return optional.isPresent() ? Optional.of(filterForDisplay(commandlistenerwrapper, (IChatBaseComponent) optional.get(), entity, i)) : Optional.empty();
    }

    public static IChatMutableComponent filterForDisplay(@Nullable CommandListenerWrapper commandlistenerwrapper, IChatBaseComponent ichatbasecomponent, @Nullable Entity entity, int i) throws CommandSyntaxException {
        if (i > 100) {
            return ichatbasecomponent.mutableCopy();
        } else {
            IChatMutableComponent ichatmutablecomponent = ichatbasecomponent instanceof ChatComponentContextual ? ((ChatComponentContextual) ichatbasecomponent).a(commandlistenerwrapper, entity, i + 1) : ichatbasecomponent.g();
            Iterator iterator = ichatbasecomponent.getSiblings().iterator();

            while (iterator.hasNext()) {
                IChatBaseComponent ichatbasecomponent1 = (IChatBaseComponent) iterator.next();

                ichatmutablecomponent.addSibling(filterForDisplay(commandlistenerwrapper, ichatbasecomponent1, entity, i + 1));
            }

            return ichatmutablecomponent.c(a(commandlistenerwrapper, ichatbasecomponent.getChatModifier(), entity, i));
        }
    }

    private static ChatModifier a(@Nullable CommandListenerWrapper commandlistenerwrapper, ChatModifier chatmodifier, @Nullable Entity entity, int i) throws CommandSyntaxException {
        ChatHoverable chathoverable = chatmodifier.getHoverEvent();

        if (chathoverable != null) {
            IChatBaseComponent ichatbasecomponent = (IChatBaseComponent) chathoverable.a(ChatHoverable.EnumHoverAction.SHOW_TEXT);

            if (ichatbasecomponent != null) {
                ChatHoverable chathoverable1 = new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT, filterForDisplay(commandlistenerwrapper, ichatbasecomponent, entity, i + 1));

                return chatmodifier.setChatHoverable(chathoverable1);
            }
        }

        return chatmodifier;
    }

    public static IChatBaseComponent a(GameProfile gameprofile) {
        return gameprofile.getName() != null ? new ChatComponentText(gameprofile.getName()) : (gameprofile.getId() != null ? new ChatComponentText(gameprofile.getId().toString()) : new ChatComponentText("(unknown)"));
    }

    public static IChatBaseComponent a(Collection<String> collection) {
        return a(collection, (s) -> {
            return (new ChatComponentText(s)).a(EnumChatFormat.GREEN);
        });
    }

    public static <T extends Comparable<T>> IChatBaseComponent a(Collection<T> collection, Function<T, IChatBaseComponent> function) {
        if (collection.isEmpty()) {
            return ChatComponentText.EMPTY;
        } else if (collection.size() == 1) {
            return (IChatBaseComponent) function.apply((Comparable) collection.iterator().next());
        } else {
            List<T> list = Lists.newArrayList(collection);

            list.sort(Comparable::compareTo);
            return b(list, function);
        }
    }

    public static <T> IChatBaseComponent b(Collection<? extends T> collection, Function<T, IChatBaseComponent> function) {
        return a(collection, ChatComponentUtils.DEFAULT_SEPARATOR, function);
    }

    public static <T> IChatMutableComponent a(Collection<? extends T> collection, Optional<? extends IChatBaseComponent> optional, Function<T, IChatBaseComponent> function) {
        return a(collection, (IChatBaseComponent) DataFixUtils.orElse(optional, ChatComponentUtils.DEFAULT_SEPARATOR), function);
    }

    public static IChatBaseComponent a(Collection<? extends IChatBaseComponent> collection, IChatBaseComponent ichatbasecomponent) {
        return a(collection, ichatbasecomponent, Function.identity());
    }

    public static <T> IChatMutableComponent a(Collection<? extends T> collection, IChatBaseComponent ichatbasecomponent, Function<T, IChatBaseComponent> function) {
        if (collection.isEmpty()) {
            return new ChatComponentText("");
        } else if (collection.size() == 1) {
            return ((IChatBaseComponent) function.apply(collection.iterator().next())).mutableCopy();
        } else {
            ChatComponentText chatcomponenttext = new ChatComponentText("");
            boolean flag = true;

            for (Iterator iterator = collection.iterator(); iterator.hasNext(); flag = false) {
                T t0 = iterator.next();

                if (!flag) {
                    chatcomponenttext.addSibling(ichatbasecomponent);
                }

                chatcomponenttext.addSibling((IChatBaseComponent) function.apply(t0));
            }

            return chatcomponenttext;
        }
    }

    public static IChatMutableComponent a(IChatBaseComponent ichatbasecomponent) {
        return new ChatMessage("chat.square_brackets", new Object[]{ichatbasecomponent});
    }

    public static IChatBaseComponent a(Message message) {
        return (IChatBaseComponent) (message instanceof IChatBaseComponent ? (IChatBaseComponent) message : new ChatComponentText(message.getString()));
    }
}
