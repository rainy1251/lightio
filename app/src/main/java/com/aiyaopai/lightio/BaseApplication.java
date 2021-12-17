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
package com.aiyaopai.lightio;


import androidx.multidex.MultiDexApplication;
import com.aiyaopai.lightio.util.SPUtils;
import com.tencent.bugly.Bugly;

public class BaseApplication extends MultiDexApplication {


    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SPUtils.instance(getApplicationContext());
        Bugly.init(getApplicationContext(), "50b31392fb", true);

    }

    public static BaseApplication getInstance() {
        return instance;
    }

}
