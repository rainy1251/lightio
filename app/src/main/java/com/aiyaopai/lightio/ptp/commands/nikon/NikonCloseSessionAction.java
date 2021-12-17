/**
 * Copyright 2013 Nils Assbeck, Guersel Ayaz and Michael Zoech
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiyaopai.lightio.ptp.commands.nikon;

import com.aiyaopai.lightio.ptp.NikonCamera;
import com.aiyaopai.lightio.ptp.PtpAction;
import com.aiyaopai.lightio.ptp.PtpCamera;
import com.aiyaopai.lightio.ptp.PtpConstants;
import com.aiyaopai.lightio.ptp.commands.CloseSessionCommand;
import com.aiyaopai.lightio.ptp.commands.SetDevicePropValueCommand;

public class NikonCloseSessionAction implements PtpAction {

    private final NikonCamera camera;

    public NikonCloseSessionAction(NikonCamera camera) {
        this.camera = camera;
    }

    @Override
    public void exec(PtpCamera.IO io) {
        SetDevicePropValueCommand setRecordingMedia = new SetDevicePropValueCommand(camera,
                PtpConstants.Property.NikonRecordingMedia, 0,
                PtpConstants.Datatype.uint8);
        io.handleCommand(setRecordingMedia);

        if (setRecordingMedia.getResponseCode() == PtpConstants.Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
            return;
        }

        io.handleCommand(new CloseSessionCommand(camera));
        camera.onSessionClosed();
    }

    @Override
    public void reset() {
    }
}
