package com.gamma.gervermod.mixin.fixes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gamma.gervermod.speedups.DataWatcherSpeedups;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

@Mixin(DataWatcher.class)
public abstract class DataWatcherSpeedupMixin {

    @Shadow
    @Final
    private Entity field_151511_a;
    @Shadow
    private boolean isBlank;
    @Shadow
    private boolean objectChanged;
    @Shadow
    private ReadWriteLock lock;

    @Unique
    private final Int2ObjectMap<DataWatcher.WatchableObject> gervermod$newWatchedObjects = new Int2ObjectLinkedOpenHashMap<>();

    @Redirect(
        method = "addObject",
        at = @At(value = "NEW", target = "(IILjava/lang/Object;)Lnet/minecraft/entity/DataWatcher$WatchableObject;"))
    public DataWatcher.WatchableObject addObject(int type, int id, Object obj) {
        return switch (type) {
            case 0 -> new DataWatcherSpeedups.ByteWatchableObject(id, ((Number) obj).byteValue());
            case 1 -> new DataWatcherSpeedups.ShortWatchableObject(id, ((Number) obj).shortValue());
            case 2 -> new DataWatcherSpeedups.IntWatchableObject(id, ((Number) obj).intValue());
            case 3 -> new DataWatcherSpeedups.FloatWatchableObject(id, ((Number) obj).floatValue());
            default -> new DataWatcher.WatchableObject(type, id, obj);
        };
    }

    /**
     * @author BallOfEnergy01
     * @reason Speedups.
     */
    @Overwrite
    public void addObjectByDataType(int id, int type) {
        DataWatcher.WatchableObject watchableobject = new DataWatcher.WatchableObject(type, id, null);
        this.lock.writeLock()
            .lock();
        gervermod$newWatchedObjects.put(id, watchableobject);
        this.lock.writeLock()
            .unlock();
        this.isBlank = false;
    }

    /**
     * @author BallOfEnergy01
     * @reason Speedups.
     */
    @Overwrite
    public byte getWatchableObjectByte(int id) {
        return ((DataWatcherSpeedups.ByteWatchableObject) this.getWatchedObject(id)).getByteValue();
    }

    /**
     * @author BallOfEnergy01
     * @reason Speedups.
     */
    @Overwrite
    public short getWatchableObjectShort(int id) {
        return ((DataWatcherSpeedups.ShortWatchableObject) this.getWatchedObject(id)).getShortValue();
    }

    /**
     * @author BallOfEnergy01
     * @reason Speedups.
     */
    @Overwrite
    public int getWatchableObjectInt(int id) {
        return ((DataWatcherSpeedups.IntWatchableObject) this.getWatchedObject(id)).getIntValue();
    }

    /**
     * @author BallOfEnergy01
     * @reason Speedups.
     */
    @Overwrite
    public float getWatchableObjectFloat(int id) {
        return ((DataWatcherSpeedups.FloatWatchableObject) this.getWatchedObject(id)).getFloatValue();
    }

    /**
     * @author BallOfEnergy01
     * @reason Speedups.
     */
    @Overwrite
    public String getWatchableObjectString(int p_75681_1_) {
        return (String) this.getWatchedObject(p_75681_1_)
            .getObject();
    }

    /**
     * @author BallOfEnergy01
     * @reason Speedups.
     */
    @Overwrite
    public ItemStack getWatchableObjectItemStack(int p_82710_1_) {
        return (ItemStack) this.getWatchedObject(p_82710_1_)
            .getObject();
    }

    /**
     * @author BallOfEnergy01
     * @reason Speedups.
     */
    @Overwrite
    public List<DataWatcher.WatchableObject> getChanged() {
        ArrayList<DataWatcher.WatchableObject> arraylist = null;

        if (this.objectChanged) {
            this.lock.readLock()
                .lock();

            for (DataWatcher.WatchableObject watchableobject : gervermod$newWatchedObjects.values()) {

                if (watchableobject.isWatched()) {
                    watchableobject.setWatched(false);

                    if (arraylist == null) {
                        arraylist = new ArrayList<>();
                    }

                    arraylist.add(watchableobject);
                }
            }

            this.lock.readLock()
                .unlock();
        }

        this.objectChanged = false;
        return arraylist;
    }

    @Redirect(
        method = { "func_151509_a", "addObject" },
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/DataWatcher;watchedObjects:Ljava/util/Map;",
            opcode = Opcodes.GETFIELD))
    public Map redirected1(DataWatcher instance) {
        return gervermod$newWatchedObjects;
    }

    /**
     * @author BallOfEnergy01
     * @reason Speedups.
     */
    @Overwrite
    public List<DataWatcher.WatchableObject> getAllWatched() {
        ArrayList<DataWatcher.WatchableObject> arraylist = null;
        this.lock.readLock()
            .lock();

        for (DataWatcher.WatchableObject watchableObject : gervermod$newWatchedObjects.values()) {
            if (arraylist == null) {
                arraylist = new ArrayList<>();
            }
            arraylist.add(watchableObject);
        }

        this.lock.readLock()
            .unlock();
        return arraylist;
    }

    /**
     * @author BallOfEnergy01
     * @reason Speedups.
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public void updateWatchedObjectsFromList(List<DataWatcher.WatchableObject> list) {
        this.lock.writeLock()
            .lock();

        for (DataWatcher.WatchableObject newWatchableObject : list) {
            DataWatcher.WatchableObject previousWatchableObject = gervermod$newWatchedObjects
                .get(newWatchableObject.getDataValueId());

            if (previousWatchableObject == null) continue;

            if (newWatchableObject instanceof DataWatcherSpeedups.IPrimitiveWatchableObject) {
                switch (newWatchableObject.getObjectType()) {
                    case 0:
                        gervermod$newWatchedObjects.put(
                            newWatchableObject.getDataValueId(),
                            new DataWatcherSpeedups.ByteWatchableObject(newWatchableObject));
                        break;
                    case 1:
                        gervermod$newWatchedObjects.put(
                            newWatchableObject.getDataValueId(),
                            new DataWatcherSpeedups.ShortWatchableObject(newWatchableObject));
                        break;
                    case 2:
                        gervermod$newWatchedObjects.put(
                            newWatchableObject.getDataValueId(),
                            new DataWatcherSpeedups.IntWatchableObject(newWatchableObject));
                        break;
                    case 3:
                        gervermod$newWatchedObjects.put(
                            newWatchableObject.getDataValueId(),
                            new DataWatcherSpeedups.FloatWatchableObject(newWatchableObject));
                        break;
                }
            } else {
                previousWatchableObject.setObject(newWatchableObject.getObject());
            }

            this.field_151511_a.func_145781_i(newWatchableObject.getDataValueId());
        }

        this.lock.writeLock()
            .unlock();
        this.objectChanged = true;
    }

    /**
     * @author BallOfEnergy01
     * @reason Speedups.
     */
    @Overwrite
    private DataWatcher.WatchableObject getWatchedObject(int p_75691_1_) {
        this.lock.readLock()
            .lock();
        DataWatcher.WatchableObject watchableobject;

        try {
            watchableobject = gervermod$newWatchedObjects.get(p_75691_1_);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting synched entity data");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched entity data");
            crashreportcategory.addCrashSection("Data ID", Integer.valueOf(p_75691_1_));
            throw new ReportedException(crashreport);
        }

        this.lock.readLock()
            .unlock();
        return watchableobject;
    }
}
