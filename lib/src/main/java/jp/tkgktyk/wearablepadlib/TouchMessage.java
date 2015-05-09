/*
 * Copyright 2015 Takagi Katsuyuki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.tkgktyk.wearablepadlib;

import android.os.Parcel;
import android.os.Parcelable;

import java.nio.ByteBuffer;

/**
 * Created by tkgktyk on 2015/05/01.
 */
public class TouchMessage implements Parcelable {
    public static final byte EVENT_UNKNOWN = (byte) 0x00;

    public static final byte EVENT_SHOW_CURSOR = (byte) 0x01;
    public static final byte EVENT_END_STROKE = (byte) 0x02;
    public static final byte EVENT_PRESS = (byte) 0x03;
    public static final byte EVENT_MOVE = (byte) 0x04;
    public static final byte EVENT_START_DRAG = (byte) 0x05;
    public static final byte EVENT_DRAG = (byte) 0x06;

    public static final byte EVENT_ACTION_TAP = (byte) 0x10;
    public static final byte EVENT_TAP_COUNT_MASK = (byte) 0x0F;

    public static byte makeTapEvent(int tapCount) {
        return (byte) (EVENT_ACTION_TAP | (tapCount & EVENT_TAP_COUNT_MASK));
    }

    public static final byte EVENT_ACTION_BACK = (byte) 0x80;
    public static final byte EVENT_ACTION_TASKS = (byte) 0x90;
    public static final byte EVENT_ACTION_HOME = (byte) 0xA0;
    public static final byte EVENT_ACTION_EXIT = (byte) 0xB0;

    public byte getMaskedEvent() {
        if ((event & ~EVENT_TAP_COUNT_MASK) == EVENT_ACTION_TAP) {
            return EVENT_ACTION_TAP;
        }
        return event;
    }

    public byte getTapCount() {
        return (byte) (event & EVENT_TAP_COUNT_MASK);
    }

    public byte event;
    public short x;
    public short y;

    public static final Creator<TouchMessage> CREATOR = new Creator<TouchMessage>() {
        @Override
        public TouchMessage createFromParcel(Parcel source) {
            return new TouchMessage(source);
        }

        @Override
        public TouchMessage[] newArray(int size) {
            return new TouchMessage[size];
        }
    };

    private TouchMessage(Parcel source) {
        event = source.readByte();
        x = readShort(source);
        y = readShort(source);
    }

    public TouchMessage() {
    }

    private short readShort(Parcel source) {
        final byte[] buf = new byte[Short.SIZE / Byte.SIZE];
        source.readByteArray(buf);
        return ByteBuffer.wrap(buf).getShort();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(event);
        dest.writeByteArray(ByteBuffer.allocate(Short.SIZE / Byte.SIZE).putShort(x).array());
        dest.writeByteArray(ByteBuffer.allocate(Short.SIZE / Byte.SIZE).putShort(y).array());
    }
}
