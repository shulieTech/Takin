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
package com.shulie.instrument.simulator.core.manager.impl;

import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.*;

/**
 * Created by xiaobin on 2017/1/19.
 */
public class ASMBytecodeDisassembler {

    private final int cwFlag;
    private final int crFlag;
    private final String dumpPath;

    public ASMBytecodeDisassembler(String basePath) {
        this(basePath, 0, 0);
    }

    public ASMBytecodeDisassembler(String basePath, int crFlag, int cwFlag) {
        this.cwFlag = cwFlag;
        this.crFlag = crFlag;
        this.dumpPath = basePath;
    }

    public File dumpBytecode(final String path, final String jvmClassName, final byte[] bytecode) {
        if (bytecode == null) {
            throw new NullPointerException("bytecode must not be null");
        }
        return writeBytes(path, jvmClassName, bytecode);
    }


    public String dumpASM(byte[] bytecode) {
        if (bytecode == null) {
            throw new NullPointerException("bytecode must not be null");
        }

        return writeBytecode(bytecode, new ASMifier());
    }

    private String writeBytecode(byte[] bytecode, Printer printer) {

        final StringWriter out = new StringWriter();
        final PrintWriter writer = new PrintWriter(out);

        accept(bytecode, printer, writer);

        return out.toString();
    }

    private File writeBytes(String path, String className, byte[] bytecode) {
        className = className.replace('.', '/') + ".class";
        if (StringUtils.isBlank(path)) {
            path = dumpPath + File.separator + "dump";
        }
        File file = new File(path, className);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytecode, 0, bytecode.length);
        } catch (Exception e) {
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
        return file;
    }

    private void accept(byte[] bytecode, Printer printer, PrintWriter writer) {

        final ClassReader cr = new ClassReader(bytecode);
        final ClassWriter cw = new ClassWriter(this.cwFlag);
        final TraceClassVisitor tcv = new TraceClassVisitor(cw, printer, writer);
        cr.accept(tcv, this.crFlag);
    }

    public String dumpVerify(byte[] bytecode, ClassLoader classLoader) {
        if (bytecode == null) {
            throw new NullPointerException("bytecode must not be null");
        }
        if (classLoader == null) {
            throw new NullPointerException("classLoader must not be null");
        }

        final StringWriter out = new StringWriter();
        final PrintWriter writer = new PrintWriter(out);

        final ClassReader cr = new ClassReader(bytecode);
        CheckClassAdapter.verify(cr, classLoader, true, writer);

        return out.toString();
    }


}

