/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shulie.instrument.simulator.module.util;

import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;

public class Decompiler {

    /**
     * @param classFilePath class文件路径
     * @return 返回反编译的字符串
     */
    public static String decompile(String classFilePath, String version) {
        if (version == null) {
            version = "1.0.0";
        }
        PlainTextOutput pto = new PlainTextOutput();
        DecompilerSettings settings = DecompilerSettings.javaDefaults();
        settings.setForceExplicitImports(true);
        settings.setOutputFileHeaderText("Generated with Simulator v" + version + " by ShuLie Technology.");
        com.strobel.decompiler.Decompiler.decompile(classFilePath, pto, settings);
        return pto.toString();
    }

}
