package com.gamma.gervermod.speedups;

import net.minecraft.entity.DataWatcher;

public class DataWatcherSpeedups {

    public interface IPrimitiveWatchableObject {
    }

    public static class ByteWatchableObject extends DataWatcher.WatchableObject implements IPrimitiveWatchableObject {

        private byte value;

        @Deprecated
        public ByteWatchableObject(int id, Object obj) {
            super(0, id, null);
            value = ((Byte) obj).byteValue();
        }

        public ByteWatchableObject(int id, byte value) {
            super(0, id, null);
            this.value = value;
        }

        public ByteWatchableObject(DataWatcher.WatchableObject watchableObject) {
            super(0, watchableObject.getDataValueId(), null);
            this.value = ((ByteWatchableObject) watchableObject).getByteValue();
        }

        @Override
        @Deprecated
        public void setObject(Object obj) {
            this.value = ((Byte) obj).byteValue();
        }

        @Override
        @Deprecated
        public Object getObject() {
            return Byte.valueOf(this.value);
        }

        public void setByteValue(byte value) {
            this.value = value;
        }

        public byte getByteValue() {
            return this.value;
        }
    }

    public static class ShortWatchableObject extends DataWatcher.WatchableObject implements IPrimitiveWatchableObject {

        private short value;

        @Deprecated
        public ShortWatchableObject(int id, Object obj) {
            super(1, id, null);
            value = ((Short) obj).shortValue();
        }

        public ShortWatchableObject(int id, short value) {
            super(1, id, null);
            this.value = value;
        }

        public ShortWatchableObject(DataWatcher.WatchableObject watchableObject) {
            super(0, watchableObject.getDataValueId(), null);
            this.value = ((ShortWatchableObject) watchableObject).getShortValue();
        }

        @Override
        @Deprecated
        public void setObject(Object obj) {
            this.value = ((Short) obj).shortValue();
        }

        @Override
        @Deprecated
        public Object getObject() {
            return Short.valueOf(this.value);
        }

        public void setShortValue(short value) {
            this.value = value;
        }

        public short getShortValue() {
            return this.value;
        }
    }

    public static class IntWatchableObject extends DataWatcher.WatchableObject implements IPrimitiveWatchableObject {

        private int value;

        @Deprecated
        public IntWatchableObject(int id, Object obj) {
            super(2, id, null);
            value = ((Integer) obj).intValue();
        }

        public IntWatchableObject(int id, int value) {
            super(2, id, null);
            this.value = value;
        }

        public IntWatchableObject(DataWatcher.WatchableObject watchableObject) {
            super(0, watchableObject.getDataValueId(), null);
            this.value = ((IntWatchableObject) watchableObject).getIntValue();
        }

        @Override
        @Deprecated
        public void setObject(Object obj) {
            this.value = ((Integer) obj).intValue();
        }

        @Override
        @Deprecated
        public Object getObject() {
            return Integer.valueOf(this.value);
        }

        public void setIntValue(int value) {
            this.value = value;
        }

        public int getIntValue() {
            return this.value;
        }
    }

    public static class FloatWatchableObject extends DataWatcher.WatchableObject implements IPrimitiveWatchableObject {

        private float value;

        @Deprecated
        public FloatWatchableObject(int id, Object obj) {
            super(3, id, null);
            value = ((Float) obj).floatValue();
        }

        public FloatWatchableObject(int id, float value) {
            super(3, id, null);
            this.value = value;
        }

        public FloatWatchableObject(DataWatcher.WatchableObject watchableObject) {
            super(0, watchableObject.getDataValueId(), null);
            this.value = ((FloatWatchableObject) watchableObject).getFloatValue();
        }

        @Override
        @Deprecated
        public void setObject(Object obj) {
            this.value = ((Float) obj).floatValue();
        }

        @Override
        @Deprecated
        public Object getObject() {
            return Float.valueOf(this.value);
        }

        public void setFloatValue(float value) {
            this.value = value;
        }

        public float getFloatValue() {
            return this.value;
        }
    }
}
