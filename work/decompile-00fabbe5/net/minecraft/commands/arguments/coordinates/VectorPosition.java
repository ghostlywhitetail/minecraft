package net.minecraft.commands.arguments.coordinates;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.world.phys.Vec2F;
import net.minecraft.world.phys.Vec3D;

public class VectorPosition implements IVectorPosition {

    private final ArgumentParserPosition x;
    private final ArgumentParserPosition y;
    private final ArgumentParserPosition z;

    public VectorPosition(ArgumentParserPosition argumentparserposition, ArgumentParserPosition argumentparserposition1, ArgumentParserPosition argumentparserposition2) {
        this.x = argumentparserposition;
        this.y = argumentparserposition1;
        this.z = argumentparserposition2;
    }

    @Override
    public Vec3D a(CommandListenerWrapper commandlistenerwrapper) {
        Vec3D vec3d = commandlistenerwrapper.getPosition();

        return new Vec3D(this.x.a(vec3d.x), this.y.a(vec3d.y), this.z.a(vec3d.z));
    }

    @Override
    public Vec2F b(CommandListenerWrapper commandlistenerwrapper) {
        Vec2F vec2f = commandlistenerwrapper.i();

        return new Vec2F((float) this.x.a((double) vec2f.x), (float) this.y.a((double) vec2f.y));
    }

    @Override
    public boolean a() {
        return this.x.a();
    }

    @Override
    public boolean b() {
        return this.y.a();
    }

    @Override
    public boolean c() {
        return this.z.a();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof VectorPosition)) {
            return false;
        } else {
            VectorPosition vectorposition = (VectorPosition) object;

            return !this.x.equals(vectorposition.x) ? false : (!this.y.equals(vectorposition.y) ? false : this.z.equals(vectorposition.z));
        }
    }

    public static VectorPosition a(StringReader stringreader) throws CommandSyntaxException {
        int i = stringreader.getCursor();
        ArgumentParserPosition argumentparserposition = ArgumentParserPosition.a(stringreader);

        if (stringreader.canRead() && stringreader.peek() == ' ') {
            stringreader.skip();
            ArgumentParserPosition argumentparserposition1 = ArgumentParserPosition.a(stringreader);

            if (stringreader.canRead() && stringreader.peek() == ' ') {
                stringreader.skip();
                ArgumentParserPosition argumentparserposition2 = ArgumentParserPosition.a(stringreader);

                return new VectorPosition(argumentparserposition, argumentparserposition1, argumentparserposition2);
            } else {
                stringreader.setCursor(i);
                throw ArgumentVec3.ERROR_NOT_COMPLETE.createWithContext(stringreader);
            }
        } else {
            stringreader.setCursor(i);
            throw ArgumentVec3.ERROR_NOT_COMPLETE.createWithContext(stringreader);
        }
    }

    public static VectorPosition a(StringReader stringreader, boolean flag) throws CommandSyntaxException {
        int i = stringreader.getCursor();
        ArgumentParserPosition argumentparserposition = ArgumentParserPosition.a(stringreader, flag);

        if (stringreader.canRead() && stringreader.peek() == ' ') {
            stringreader.skip();
            ArgumentParserPosition argumentparserposition1 = ArgumentParserPosition.a(stringreader, false);

            if (stringreader.canRead() && stringreader.peek() == ' ') {
                stringreader.skip();
                ArgumentParserPosition argumentparserposition2 = ArgumentParserPosition.a(stringreader, flag);

                return new VectorPosition(argumentparserposition, argumentparserposition1, argumentparserposition2);
            } else {
                stringreader.setCursor(i);
                throw ArgumentVec3.ERROR_NOT_COMPLETE.createWithContext(stringreader);
            }
        } else {
            stringreader.setCursor(i);
            throw ArgumentVec3.ERROR_NOT_COMPLETE.createWithContext(stringreader);
        }
    }

    public static VectorPosition a(double d0, double d1, double d2) {
        return new VectorPosition(new ArgumentParserPosition(false, d0), new ArgumentParserPosition(false, d1), new ArgumentParserPosition(false, d2));
    }

    public static VectorPosition a(Vec2F vec2f) {
        return new VectorPosition(new ArgumentParserPosition(false, (double) vec2f.x), new ArgumentParserPosition(false, (double) vec2f.y), new ArgumentParserPosition(true, 0.0D));
    }

    public static VectorPosition d() {
        return new VectorPosition(new ArgumentParserPosition(true, 0.0D), new ArgumentParserPosition(true, 0.0D), new ArgumentParserPosition(true, 0.0D));
    }

    public int hashCode() {
        int i = this.x.hashCode();

        i = 31 * i + this.y.hashCode();
        i = 31 * i + this.z.hashCode();
        return i;
    }
}
