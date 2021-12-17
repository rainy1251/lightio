/**
 * Copyright 2013 Nils Assbeck, Guersel Ayaz and Michael Zoech
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiyaopai.lightio.ptp.commands;

import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.aiyaopai.lightio.ptp.PtpCamera;
import com.aiyaopai.lightio.ptp.PtpConstants;

/**
 * Read file data from camera with specified {@code objectHandle}.
 */
public class GetObjectCommand extends Command {

    private static final String TAG = GetObjectCommand.class.getSimpleName();

    private final int objectHandle;

    private final BitmapFactory.Options options;
    private Bitmap inBitmap;
    private ByteBuffer buffer;
    private int mLength;

    private boolean outOfMemoryError;

    public GetObjectCommand(PtpCamera camera, int objectHandle, int sampleSize) {
        super(camera);
        this.objectHandle = objectHandle;
        options = new BitmapFactory.Options();
        if (sampleSize >= 1 && sampleSize <= 4) {
            options.inSampleSize = sampleSize;
        } else {
            options.inSampleSize = 2;
        }
    }

    public Bitmap getBitmap() {
        return inBitmap;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public int getLength() {
        return mLength;
    }

    public boolean isOutOfMemoryError() {
        return outOfMemoryError;
    }

    @Override
    public void exec(PtpCamera.IO io) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        super.reset();
        inBitmap = null;
        outOfMemoryError = false;
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, PtpConstants.Operation.GetObject, objectHandle);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        try {
            // 12 == offset of data header
            inBitmap = BitmapFactory.decodeByteArray(b.array(), 12, length - 12, options);
            buffer = b;
            mLength = length;
        } catch (RuntimeException e) {
            Log.i(TAG, "exception on decoding picture : " + e.toString());
        } catch (OutOfMemoryError e) {
            System.gc();
            outOfMemoryError = true;
        }
    }
}
