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
package com.shulie.instrument.simulator.core.util.matcher.structure;

/**
 * 成员结构
 */
public class MemberStructure {

    private final Access access;
    private final String name;
    private final ClassStructure declaringClassStructure;

    public MemberStructure(final Access access,
                           final String name,
                           final ClassStructure declaringClassStructure) {
        this.access = access;
        this.name = name;
        this.declaringClassStructure = declaringClassStructure;
    }

    public String getName() {
        return name;
    }

    public ClassStructure getDeclaringClassStructure() {
        return declaringClassStructure;
    }

    public Access getAccess() {
        return access;
    }
}
