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
package com.aiyaopai.lightio.ptp.commands;

import com.aiyaopai.lightio.ptp.Camera;
import com.aiyaopai.lightio.ptp.PtpAction;
import com.aiyaopai.lightio.ptp.PtpCamera;
import com.aiyaopai.lightio.ptp.PtpConstants;
import com.aiyaopai.lightio.ptp.model.StorageInfo;
import com.aiyaopai.lightio.util.MyLog;

public class GetStorageInfosAction implements PtpAction {

    private final PtpCamera camera;
    private final Camera.StorageInfoListener listener;

    public GetStorageInfosAction(PtpCamera camera, Camera.StorageInfoListener listener) {
        this.camera = camera;
        this.listener = listener;
    }

    @Override
    public void exec(PtpCamera.IO io) {
        GetStorageIdsCommand getStorageIds = new GetStorageIdsCommand(camera);
        io.handleCommand(getStorageIds);

        if (getStorageIds.getResponseCode() != PtpConstants.Response.Ok) {
            listener.onAllStoragesFound();
            return;
        }

        int ids[] = getStorageIds.getStorageIds();
        for (int i = 0; i < ids.length; ++i) {
            int storageId = ids[i];
            GetStorageInfoCommand c = new GetStorageInfoCommand(camera, storageId);
            io.handleCommand(c);

            if (c.getResponseCode() != PtpConstants.Response.Ok) {
                listener.onAllStoragesFound();
                return;
            }

            StorageInfo info = c.getStorageInfo();
//            StringBuffer infoValue =new StringBuffer();
//            infoValue.append("accessCapability:"+info.accessCapability+"\n");
//            infoValue.append("filesystemType:"+info.filesystemType+"\n");
//            infoValue.append("storageDescription:"+info.storageDescription+"\n");
//            infoValue.append("volumeLabel:"+info.volumeLabel+"\n");
//            infoValue.append("storageType:"+info.storageType+"\n");
//            infoValue.append("maxCapacity:"+info.maxCapacity+"\n");
//            infoValue.append("freeSpaceInBytes:"+info.freeSpaceInBytes+"\n");
//            infoValue.append("freeSpaceInImages:"+info.freeSpaceInImages+"\n");
//            MyLog.show(infoValue.toString());

            String label = c.getStorageInfo().volumeLabel.isEmpty() ? c.getStorageInfo().storageDescription : c
                    .getStorageInfo().volumeLabel;
            if (label == null || label.isEmpty()) {
                label = "Storage " + i;
            }
            if (info.maxCapacity > 0 && info.freeSpaceInBytes>0) {
                listener.onStorageFound(storageId, label);
            }
        }

        listener.onAllStoragesFound();
    }

    @Override
    public void reset() {
    }
}
